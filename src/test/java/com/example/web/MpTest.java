package com.example.web;

import com.example.web.dao.UserMapper;
import com.example.web.pojo.TestReq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
* @description mybatis-plus测试
* @author Xu·yan
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

}
