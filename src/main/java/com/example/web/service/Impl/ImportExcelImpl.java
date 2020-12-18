package com.example.web.service.Impl;

import cn.hutool.core.util.IdUtil;
import com.example.web.dao.KnowledgeLawMapper;
import com.example.web.pojo.KnowledgeLaw;
import com.example.web.service.ImportExcelService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ImportExcelImpl implements ImportExcelService {


    @Autowired
    private KnowledgeLawMapper knowledgeLawMapper;


    /**
    * @description 导入excel
    * @author Xu·yan
    * @date 2020/12/15 2:27 下午
    */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public String batchImport(String fileName, MultipartFile file) throws Exception {

        boolean notNull = false;

        List<KnowledgeLaw> userList = new ArrayList<>();

        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new MyException("上传文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if (sheet != null) {
            notNull = true;
        }
        KnowledgeLaw importExcelPeople;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }

//            importExcelPeople = new ImportExcelPeople();

            importExcelPeople = new KnowledgeLaw();

          /*  if (row.getCell(0).getCellType() != 1) {
                throw new MyException("导入失败(第" + (r + 1) + "行,姓名请设为文本格式)");
            }
            */
            //法律法规编号
            String number = row.getCell(0).getStringCellValue();
          /*  if (name == null || name.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 1) + "行,编号)");
            }*/

            row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
            //法律法规名称校验
            String lawName = row.getCell(1).getStringCellValue();
            if (lawName == null || lawName.isEmpty()) {
                throw new MyException("导入失败(第" + (r + 1) + "行,法律法规名称未填写)");
            }
            //法律法规类型校验
            String type = row.getCell(2).getStringCellValue();
            if (type == null) {
                throw new MyException("导入失败(第" + (r + 1) + "行,法律法规类型未填写)");
            }

            //颁发时间
      /*      Date issuedTime;
            String dates = row.getCell(3).getStringCellValue();
            String DATE_REGEX = "^((([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29))\\\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
            boolean matches =  Pattern.matches(DATE_REGEX, dates);
            if (!matches) {
                throw new MyException("导入失败(第" + (r + 1) + "行,颁发日期格式不正确或未填写)");
            } else {
                issuedTime = row.getCell(3).getDateCellValue();
            }*/
            String issuedTime = row.getCell(3).getStringCellValue();

            String issuedBy = row.getCell(4).getStringCellValue();

            importExcelPeople.setNumber(number);
            importExcelPeople.setName(lawName);
            importExcelPeople.setType(type);
            importExcelPeople.setIssuedTime(issuedTime);
            importExcelPeople.setIssuedBy(issuedBy);

            userList.add(importExcelPeople);
        }


        for (int i = 0; i < userList.size(); i++) {
            KnowledgeLaw importExcel = userList.get(i);
            KnowledgeLaw knowledgeLaw = new KnowledgeLaw()
                    .setId(IdUtil.simpleUUID())
                    .setNumber(importExcel.getNumber())
                    .setName(importExcel.getName())
                    .setType(importExcel.getType())
                    .setIssuedTime(importExcel.getIssuedTime())
                    .setIssuedBy(importExcel.getIssuedBy())
                    .setCreateTime(new Date(new Date().getTime() + 1000 * i));
            knowledgeLawMapper.addinsert(knowledgeLaw);
        }
        return "添加成功";
    }
}
