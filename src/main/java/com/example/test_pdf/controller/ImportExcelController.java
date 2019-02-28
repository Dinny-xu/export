package com.example.test_pdf.controller;

import com.example.test_pdf.service.ImportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
public class ImportExcelController {
    @Autowired
    private ImportExcelService importExcelService;

    @PostMapping("/import")
    public boolean addUser(@RequestParam("file") MultipartFile file) {
        boolean a = false;
        String fileName = file.getOriginalFilename();
        try {
            a = importExcelService.batchImport(fileName, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  a;
    }
}
