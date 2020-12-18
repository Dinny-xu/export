package com.example.web.dao;

import com.example.web.pojo.ImportExcelPeople;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImportExcelMapper {
    void addUser(ImportExcelPeople sysUser);

    int updateUserByName(ImportExcelPeople sysUser);

    int selectByName(String name);
}
