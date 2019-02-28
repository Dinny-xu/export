package com.example.test_pdf.service.Impl;

import com.example.test_pdf.dao.ExportExcelMapper;
import com.example.test_pdf.pojo.People;
import com.example.test_pdf.service.ExportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportExcelImpl implements ExportExcelService {
    @Autowired
    private ExportExcelMapper exportExcelMapper;

    @Override
    public List<People> teacherinfor() {
        return exportExcelMapper.teacherinfor();
    }
}
