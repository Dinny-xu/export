package com.example.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.web.pojo.WlwDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GetAddressMapper extends BaseMapper<WlwDevice> {

    @Select("SELECT\n" +
            "id AS id,\n" +
            "lng AS lng,\n" +
            "lat AS lat\n" +
            "FROM\n" +
            "wlw_device ")
    List<WlwDevice> selectAll();

    @Update("UPDATE wlw_device SET lng =#{req.lng},lat =#{req.lat} WHERE id = #{id}")
    void updateById(@Param("req") WlwDevice wlwDevice1, @Param("id") String id);
}
