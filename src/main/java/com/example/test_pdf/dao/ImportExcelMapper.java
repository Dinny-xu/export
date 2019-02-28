package com.example.test_pdf.dao;

import com.example.test_pdf.pojo.ImportExcelPeople;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImportExcelMapper {
    void addUser(ImportExcelPeople sysUser);

    int updateUserByName(ImportExcelPeople sysUser);

    int selectByName(String name);
}
