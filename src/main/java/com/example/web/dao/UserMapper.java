package com.example.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.web.pojo.TestReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<TestReq> {
}
