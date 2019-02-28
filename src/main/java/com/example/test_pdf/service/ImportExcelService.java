package com.example.test_pdf.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImportExcelService {
    boolean batchImport(String fileName, MultipartFile file) throws Exception;
}
