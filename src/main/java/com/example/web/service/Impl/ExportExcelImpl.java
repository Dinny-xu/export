package com.example.web.service.Impl;

import com.example.web.dao.ExportExcelMapper;
import com.example.web.pojo.KnowledgeLaw;
import com.example.web.service.ExportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportExcelImpl implements ExportExcelService {
    @Autowired
    private ExportExcelMapper exportExcelMapper;

    @Override
    public List<KnowledgeLaw> teacherinfor() {
        return exportExcelMapper.teacherinfor();
    }
}
