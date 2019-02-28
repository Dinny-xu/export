package com.example.test_pdf.service;

import com.example.test_pdf.pojo.People;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ExportExcelService {
    //service层代码
    List<People> teacherinfor();
}