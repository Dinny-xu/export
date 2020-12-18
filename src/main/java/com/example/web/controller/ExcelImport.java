package com.example.web.controller;

import com.example.web.dao.KnowledgeLawMapper;
import com.example.web.excelUtil.ExcelHandler;
import com.example.web.pojo.KnowledgeLaw;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
* @description excel 导入
* @author Xu·yan
* @date 2020/12/16 3:20 下午
*/
@Api(tags = "excel")
@RestController
public class ExcelImport {

    @Autowired
    private KnowledgeLawMapper knowledgeLawMapper;

    /**
     * @description 导入excel
     * @author Xu·yan
     * @date 2020/12/15 2:27 下午
     */
    @ApiIgnore
    @PostMapping("/import/excel")
    @ApiOperation(value = "导入excel")
    @ApiOperationSupport(author = "xyy")
    @ApiImplicitParam(name = "file", value = "文件", required = true)
    Object testImport(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        if (!ExcelHandler.isExcel(fileName)) {
            throw new RuntimeException("导入只支持Excel表格");
        }
        InputStream is = file.getInputStream();
        //解析Excel
        ExcelHandler excelHandler = new ExcelHandler(is, ExcelHandler.getExcelVersion(fileName));

        List<List<String>> read = excelHandler.read();
        //关流
        excelHandler.close();

        for (int i = 0; i < read.size(); i++) {
            List<String> strings = read.get(i);
            String s = strings.get(0);
            System.out.println(s);
        }

        return read;
    }

    /**
     * @description 导出excel
     * @author Xu·yan
     * @date 2020/12/15 2:27 下午
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperation(value = "导出excel")
    @ApiOperationSupport(author = "xyy")
    @ApiImplicitParam(name = "ids", value = "id集合", required = true)
    void testExport(@RequestBody List<String> ids, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        List<KnowledgeLaw> list = knowledgeLawMapper.selectByIds(ids);

        List<List<String>> data = new ArrayList<>();
        data.add(new ArrayList<String>() {{
            add("法律法规编号");
            add("法律法规名称");
            add("法律法规类型");
            add("颁发时间");
            add("颁发单位名称");
        }});
        list.forEach(x -> data.add(new ArrayList<String>() {{
            add(x.getNumber());
            add(x.getName());
            add(x.getType());
            add(x.getIssuedTime());
            add(x.getIssuedBy());
        }}));
        ExcelHandler excelHandler = new ExcelHandler(ExcelHandler.WorkbookVersion.XLSX);
        //给创建一个sheet 并且吧数据写进去  创建一个新的sheet
        excelHandler.write(data, "sheet名称", true);
        //定制一些样式  一般只需要维护表头即可
        excelHandler.setRowStyle(0, 0, excelHandler.makeStyle(ExcelHandler.RowType.HEADER));
        //写入数据后 把数据再写到response即可实现导出了
        excelHandler.flushIntoResponseForDownload(response, "测试导出.xlsx");
        //关流
        excelHandler.close();
        System.out.println("导出文件耗时" + (System.currentTimeMillis() - start) + "毫秒---");
    }
}
