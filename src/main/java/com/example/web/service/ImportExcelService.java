package com.example.web.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImportExcelService {

    String batchImport(String fileName, MultipartFile file) throws Exception;
}
