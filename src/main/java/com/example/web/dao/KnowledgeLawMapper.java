package com.example.web.dao;


import com.example.web.pojo.KnowledgeLaw;

import java.util.List;

public interface KnowledgeLawMapper  {


    void addinsert(KnowledgeLaw knowledgeLaw);

    KnowledgeLaw selectById(String id);

    List<KnowledgeLaw> selectByIds(List<String> ids);
}
