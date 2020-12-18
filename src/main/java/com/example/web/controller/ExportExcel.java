package com.example.web.controller;

import com.example.web.dao.KnowledgeLawMapper;
import com.example.web.pojo.KnowledgeLaw;
import com.example.web.service.ExportExcelService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Autowired
    private KnowledgeLawMapper knowledgeLawMapper;

    /**
     * @description 导出excel
     * @author Xu·yan
     * @date 2020/12/15 2:34 下午
     */
    @ApiOperation(value = "导出excel")
    @ApiOperationSupport(author = "xyy")
    @RequestMapping(value = "UserExcelDownloads", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void downloadAllClassmate(@RequestBody List<String> ids, HttpServletResponse response) throws IOException {
        long start = System.currentTimeMillis();
        List<KnowledgeLaw> list = knowledgeLawMapper.selectByIds(ids);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("信息表");

//        List<KnowledgeLaw> classmateList = exportExcel.teacherinfor();

        String fileName = "法律法规" + ".xls";//设置要导出的文件的名字
        //新增数据行，并且设置单元格数据

        int rowNum = 1;

        //headers表示excel表中第一行的表头
        String[] headers = {"法律法规编号", "法律法规名称", "法律法规类型", "颁发时间", "颁发单位名称"};

        HSSFRow row = sheet.createRow(0);

        //在excel表中添加表头
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        //在表中存放查询到的数据放入对应的列
        for (KnowledgeLaw people : list) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(people.getNumber());
            row1.createCell(1).setCellValue(people.getName());
            row1.createCell(2).setCellValue(people.getType());
            row1.createCell(3).setCellValue(people.getIssuedTime());
            row1.createCell(4).setCellValue(people.getIssuedBy());
            rowNum++;
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
        System.out.println("导出文件耗时" + (System.currentTimeMillis() - start) + "毫秒---");
    }
}
