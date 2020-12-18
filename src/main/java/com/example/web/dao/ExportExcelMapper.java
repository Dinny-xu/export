package com.example.web.dao;

import com.example.web.pojo.KnowledgeLaw;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExportExcelMapper {
    //mapper接口代码
    @Select(" SELECT k.number      AS number,\n" +
            "               k.NAME        AS name,\n" +
            "               k.type        AS type,\n" +
            "               k.issued_time AS issuedTime,\n" +
            "               k.issued_by   AS issuedBy\n" +
            "        FROM knowledge_law k\n")
    List<KnowledgeLaw> teacherinfor();
}
