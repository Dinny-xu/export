package com.example.test_pdf.controller;

import com.example.test_pdf.pojo.People;
import com.example.test_pdf.service.ExportExcelService;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/")
public class ExportExcel {
    @Autowired
    ExportExcelService exportExcel;
    @RequestMapping(value = "UserExcelDownloads", method = RequestMethod.GET)
    public void downloadAllClassmate(HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("信息表");

        List<People> classmateList = exportExcel.teacherinfor();

        String fileName = "userinf"  + ".xls";//设置要导出的文件的名字
        //新增数据行，并且设置单元格数据

        int rowNum = 1;

        String[] headers = { "序号", "姓名", "年龄", "性别"};
        //headers表示excel表中第一行的表头

        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头

        for(int i=0;i<headers.length;i++){
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //在表中存放查询到的数据放入对应的列
        for (People people : classmateList) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(people.getId());
            row1.createCell(1).setCellValue(people.getName());
            row1.createCell(2).setCellValue(people.getAge());
            row1.createCell(3).setCellValue(people.getSex());
            rowNum++;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }
}
