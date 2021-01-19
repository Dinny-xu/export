package com.example.web;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import com.example.web.dao.UserMapper;
import com.example.web.pojo.TestReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Xu·yan
 * @description mybatis-plus测试
 * @date 2020/12/22 11:24 上午
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MpTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testInsert() {

        TestReq testReq = new TestReq();
        testReq.setId(1);
        testReq.setName("小张");
        testReq.setAge(18);
        testReq.setEmail("865760708@qq.com");

//        userMapper.insert(testReq);
        userMapper.updateById(testReq);

    }


    @Test
    public void image() throws IOException {
        // 反转图片颜色  黑白
//        ImgUtil.gray(FileUtil.file("/Users/xu/Pictures/avatar6.png"), FileUtil.file("/Users/xu/Pictures/avatar7.png"));

//        pressText 添加文字水印
       /* ImgUtil.pressText(//
                FileUtil.file("/Users/xu/Pictures/avatar6.png"), //
                FileUtil.file("/Users/xu/Pictures/avatar7.png"), //
                "版权所有", Color.WHITE, //文字
                new Font("黑体", Font.BOLD, 100), //字体
                0, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                50, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                0.8f//透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
        );*/

//        pressImage 添加图片水印
 /*       ImgUtil.pressImage(
                FileUtil.file("/Users/xu/Pictures/4.jpeg"),
                FileUtil.file("/Users/xu/Pictures/5.jpeg"),
                ImgUtil.read(FileUtil.file("/Users/xu/Pictures/2.jpeg")), //水印图片
                0, //x坐标修正值。 默认在中间，偏移量相对于中间偏移
                0, //y坐标修正值。 默认在中间，偏移量相对于中间偏移
                0.1f
        );*/

//        rotate 旋转图片
        // 旋转180度
/*        BufferedImage image = (BufferedImage) ImgUtil.rotate(ImageIO.read(FileUtil.file("/Users/xu/Pictures/4.jpeg")), 180);
        ImgUtil.write(image, FileUtil.file("/Users/xu/Pictures/5.jpeg"));*/

//        flip 水平翻转图片  镜像
        ImgUtil.flip(FileUtil.file("/Users/xu/Pictures/4.jpeg"), FileUtil.file("/Users/xu/Pictures/5.jpeg"));
    }

}
