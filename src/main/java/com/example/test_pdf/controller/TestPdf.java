package com.example.test_pdf.controller;

import com.example.test_pdf.util.PDFUtil;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/")
public class TestPdf {
    private Logger logger = LoggerFactory.getLogger(TestPdf.class);
    @RequestMapping(value = "projectExport", method = RequestMethod.GET)
    public void projectExport(HttpServletRequest request, HttpServletResponse response) {
        try {

            Map map=new HashMap<String,Object>();
            map.put("test","测试");

            ByteArrayOutputStream baos = PDFUtil.createPDF("templates/project.html", map);
            //设置response文件头

            PDFUtil.renderPdf(response, baos.toByteArray(), "pdf文件");
            baos.close();
        } catch (Exception e) {
            logger.info("导出报错",e);
        }
    }
}
