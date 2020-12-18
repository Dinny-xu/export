package com.example.web.controller;

import com.example.web.service.ImportExcelService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
public class ImportExcelController {

    @Autowired
    private ImportExcelService importExcelService;

    @PostMapping("/import")
    @ApiImplicitParam(name = "file", value = "文件", required = true)
    @ApiOperation(value = "导入excel")
    public String addUser(@RequestParam("file") MultipartFile file) throws Exception {
        return importExcelService.batchImport(file.getOriginalFilename(), file);
    }
}
