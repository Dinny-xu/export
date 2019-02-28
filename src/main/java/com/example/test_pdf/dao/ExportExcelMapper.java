package com.example.test_pdf.dao;

import com.example.test_pdf.pojo.People;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExportExcelMapper {
    //mapper接口代码
    @Select("select * from people")
    List<People> teacherinfor();
}
