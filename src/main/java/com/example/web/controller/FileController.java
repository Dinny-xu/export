package com.example.web.controller;

import cn.hutool.core.util.IdUtil;
import com.example.web.util.MinioUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @program: test_pdf
 * @description: 文件上传
 * @author: Xu·yan
 * @create: 2020-12-24 15:10
 **/
@Api(tags = "文件传输")
@RestController
public class FileController {


    @Autowired
    private MinioUtils minioUtil;

    @Value("${minio.bucketName}")
    private String bucketName;


    public void createBucket(String bucketName) {

        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("    \"Statement\": [\n");
        builder.append("        {\n");
        builder.append("            \"Action\": [\n");
        builder.append("                \"s3:GetBucketLocation\",\n");
        builder.append("                \"s3:ListBucket\"\n");
        builder.append("            ],\n");
        builder.append("            \"Effect\": \"Allow\",\n");
        builder.append("            \"Principal\": \"*\",\n");
        builder.append("            \"Resource\": \"arn:aws:s3:::" + bucketName + "\"\n");
        builder.append("        },\n");
        builder.append("        {\n");
        builder.append("            \"Action\": \"s3:GetObject\",\n");
        builder.append("            \"Effect\": \"Allow\",\n");
        builder.append("            \"Principal\": \"*\",\n");
        builder.append("            \"Resource\": \"arn:aws:s3:::" + bucketName + "/project-file*\"\n");
        builder.append("        }\n");
        builder.append("    ],\n");
        builder.append("    \"Version\": \"2012-10-17\"\n");
        builder.append("}\n");

        try {
            minioUtil.makeBucket(bucketName);
            minioUtil.setBucketPolicy(bucketName, builder.toString());
        } catch (
                Exception e) {
            e.printStackTrace();
            System.out.println("创建存储桶失败：" + e.getMessage());
        }
    }


    /**
     * 文件上传接口
     * @param file  上传的文件对象
     * @return  上传成功返回文件url 图片  txt 文件支持通过url 浏览器直接预览
     */
    @PostMapping("/upload")
    public String MinIOUpload(MultipartFile file) {

        if (file.isEmpty() || file.getSize() == 0) {
            return "文件为空";
        }
        try {
            if (!minioUtil.bucketExists(bucketName)) {
                createBucket(bucketName);
            }
            String fileName = file.getOriginalFilename();
            String newName = "project-file/" + IdUtil.simpleUUID()
                    + fileName.substring(fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();
            minioUtil.putObject(bucketName, newName, inputStream, file.getContentType());
            inputStream.close();
            return minioUtil.getObjectUrl(bucketName, newName);
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败";
        }
    }

    /**
     * 文件下载
     * @param filename   文件名
     * @param httpResponse 接口返回对象
     */
    @GetMapping("download/{filename}")
    public void downloadFiles(@PathVariable("filename") String filename, HttpServletResponse httpResponse) {

        try {
            InputStream object = minioUtil.getObject(bucketName, "/project-file/"+filename);
            byte[] buf = new byte[1024];
            int length = 0;
            httpResponse.reset();
            httpResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            httpResponse.setContentType("application/octet-stream");
            httpResponse.setCharacterEncoding("utf-8");
            OutputStream outputStream = httpResponse.getOutputStream();
            while ((length = object.read(buf)) > 0) {
                outputStream.write(buf, 0, length);
            }
            outputStream.close();
        } catch (Exception ex) {
            System.out.println("文件下载失败：" + ex.getMessage());
        }
    }
}




