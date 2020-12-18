package com.example.web.dao;

import com.example.web.pojo.WlwDevice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GetAddressMapper {

    @Select("SELECT\n" +
            "\tid AS id,\n" +
            "\tdevice_address AS deviceAddress \n" +
            "FROM\n" +
            "\twlw_device WHERE lng IS NULL")
    List<WlwDevice> selectAll();

    @Update("UPDATE wlw_device SET lng =#{req.lng},lat =#{req.lat} WHERE id = #{id}")
    void updateById(@Param("req") WlwDevice wlwDevice1, @Param("id") String id);
}
