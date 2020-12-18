package com.example.web.excelUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作Excel的处理器类(含导入和导出功能)
 *
 * @author fangshixiang
 * @description //
 * @date 2018/11/13 21:17
 */
public class ExcelHandler {

    private Workbook workbook;

    /**
     * 构造函数 调用者只需要传入excel的文件流,并告知Excel版本即可（使用得最多）
     *
     * @param is      the is
     * @param version the version
     * @throws IOException
     */
    public ExcelHandler(InputStream is, WorkbookVersion version) throws IOException {
        if (version == WorkbookVersion.XLS) {
            workbook = new HSSFWorkbook(is);
        } else {
            workbook = new XSSFWorkbook(is);
        }
    }

    /**
     * 需要导出的时候 调用这个构造函数  可以创建一个Excel文件 并且调用对应的方法
     *
     * @param version
     */
    public ExcelHandler(WorkbookVersion version) {
        if (version == WorkbookVersion.XLS) {
            workbook = new HSSFWorkbook();
        } else {
            workbook = new XSSFWorkbook();
        }
    }

    /**
     * 也支持调用者外部解析 然后把workbook直接传给我就行
     * <p>
     * 一般用于导出，外部建立好传进来就成
     *
     * @param workboook the workboook
     */
    public ExcelHandler(Workbook workboook) {
        this.workbook = workboook;
    }

    /**
     * excel的版本
     */
    public enum WorkbookVersion {
        XLS, XLSX
    }

    /**
     * 行类型  是否是表头
     */
    public enum RowType {
        HEADER, NORMER
    }

    //////////////////////////静态方法 start//////////////////////////

    /**
     * 根据后缀判断是否为 Excel 文件，后缀匹配xls和xlsx
     *
     * @param pathName the pathName
     * @return boolean boolean
     */
    public static boolean isExcel(String pathName) {
        if (pathName == null) {
            return false;
        }
        //return pathName.endsWith(".xls") || pathName.endsWith(".xlsx");
        return getExcelVersion(pathName) != null;
    }

    /**
     * 获取该文件的Excel版本
     *
     * @param pathName the path name
     * @return the excel version
     */
    public static WorkbookVersion getExcelVersion(String pathName) {
        if (isExcel2003(pathName)) {
            return WorkbookVersion.XLS;
        } else if (isExcel2007(pathName)) {
            return WorkbookVersion.XLSX;
        } else {
            return null;
        }
    }

    /**
     * 是否是xls格式的Excel文件
     *
     * @param pathName the file path
     * @return the boolean
     */
    public static boolean isExcel2003(String pathName) {
        return pathName.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * 是否是xlsx格式的Excel文件
     *
     * @param pathName the file path
     * @return the boolean
     */
    public static boolean isExcel2007(String pathName) {
        return pathName.matches("^.+\\.(?i)(xlsx)$");
    }

    //////////////////////////静态方法 end//////////////////////////

    /**
     * 读取 Excel 第一页所有数据（第一个sheet，一般导入都只有一个sheet，所以这个最常用）
     *
     * @return list list
     */
    public List<List<String>> read() {
        return read(0, 1, getRowCount(0) - 1);
    }

    /**
     * 读取指定sheet 页所有数据
     *
     * @param sheetIx 指定 sheet 页，从 0 开始
     * @return list list
     */
    public List<List<String>> read(int sheetIx) {
        return read(sheetIx, 0, getRowCount(sheetIx) - 1);
    }

    /**
     * 读取指定sheet 页指定行数据
     *
     * @param sheetIx 指定 sheet 页，从 0 开始
     * @param start   指定开始行，从 0 开始
     * @param end     指定结束行，从 0 开始
     * @return list list
     */
    public List<List<String>> read(int sheetIx, int start, int end) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        List<List<String>> list = new ArrayList<>();

        if (end > getRowCount(sheetIx)) {
            end = getRowCount(sheetIx);
        }

        //int cols = sheet.getRow(0).getLastCellNum(); // 第一行总列数（第一行总列数决定了列的总数）

        //兼容操作：防止第一列没填，所以最多往下找10列 再没有就报错吧  找到就当作表头
        int cols = 0;
        //优化 为了表面有的人可能第一列空着 所以此处改为找第一个不为null的行为止（遍历10行吧）
        for (int i = 0; i < 10; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                cols = row.getLastCellNum();
                break;
            }
        }
        if (cols == 0) {
            throw new RuntimeException("请把表格的前10行填充数据(表头)~");
        }

        for (int i = start; i <= end; i++) {
            List<String> rowList = new ArrayList<>();
            Row row = sheet.getRow(i);
            for (int j = 0; j < cols; j++) {
                if (row == null) {
                    rowList.add(null);
                    continue;
                }
                rowList.add(getCellValueToString(row.getCell(j)));
            }
            list.add(rowList);
        }

        return list;
    }

    /**
     * 将数据写入到 Excel 默认第一页中，从第1行开始写入（导出可以使用）
     *
     * @param rowData 数据
     * @return boolean boolean
     */
    public boolean write(List<List<String>> rowData) {
        return write(0, rowData, 0);
    }

    /**
     * 将数据写入到 Excel 新创建的 Sheet 页（导出）
     *
     * @param rowData    数据
     * @param sheetName  长度为1-31，不能包含后面任一字符: ：\ / ? * [ ]
     * @param isNewSheet the is new sheet
     * @return boolean boolean
     * @throws IOException the io exception
     */
    public boolean write(List<List<String>> rowData, String sheetName, boolean isNewSheet) {
        Sheet sheet = null;
        if (isNewSheet) {
            sheet = workbook.createSheet(sheetName);
        } else {
            sheet = workbook.createSheet();
        }
        int sheetIx = workbook.getSheetIndex(sheet);
        return write(sheetIx, rowData, 0);
    }

    /**
     * 将数据追加到sheet页最后
     *
     * @param sheetIx  指定 Sheet 页，从 0 开始
     * @param rowData  数据
     * @param isAppend 是否追加,true 追加，false 重置sheet再添加
     * @return boolean boolean
     */
    public boolean write(int sheetIx, List<List<String>> rowData, boolean isAppend) {
        if (isAppend) {
            return write(sheetIx, rowData, getRowCount(sheetIx));
        } else { // 清空再添加
            clearSheet(sheetIx);
            return write(sheetIx, rowData, 0);
        }
    }

    /**
     * 把该workBook写进resp，提供下载的请求头
     * <p>
     * 提示浏览器下载此文件
     *
     * @param response resp
     * @param fileName excel文件的名字
     * @return boolean
     */
    public boolean flushIntoResponseForDownload(HttpServletResponse response, String fileName) {
        fileName = fileName == null ? "test.xlsx" : fileName;
        try {
            response.setContentType("application/vnd.ms-excel");
            //response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 将数据写入到 Excel 指定 Sheet 页指定开始行中,指定行后面数据向后移动
     *
     * @param sheetIx  指定 Sheet 页，从 0 开始
     * @param rowData  数据
     * @param startRow 指定开始行，从 0 开始
     * @return boolean boolean
     */
    public boolean write(int sheetIx, List<List<String>> rowData, int startRow) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        int dataSize = rowData.size();
        if (getRowCount(sheetIx) > 0) { // 如果小于等于0，则一行都不存在
            sheet.shiftRows(startRow, getRowCount(sheetIx), dataSize);
        }
        for (int i = 0; i < dataSize; i++) {
            Row row = sheet.createRow(i + startRow);
            for (int j = 0; j < rowData.get(i).size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(rowData.get(i).get(j) + "");
            }
        }
        return true;
    }

    ///////////////////////////下为设置Excel的样式...////////////////////////////

    /**
     * 给一行设置样式（每个单元格都一样）
     *
     * @param sheetIx  指定 Sheet 页，从 0 开始
     * @param rowIndex the row index
     * @param style    the style
     * @return boolean
     */
    public boolean setRowStyle(int sheetIx, int rowIndex, CellStyle style) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        Row row = sheet.getRow(rowIndex);
        int columnCount = row.getLastCellNum();
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i, true);// 设置列宽度自适应
            sheet.setColumnWidth(i, 4000);

            //拿到这个cell 设置一样的样式
            Cell cell = row.getCell(i);
            cell.setCellStyle(style);
        }
        return true;
    }

    /**
     * 设置cell 样式
     *
     * @param sheetIx  指定 Sheet 页，从 0 开始
     * @param rowIndex the row index
     * @param colIndex 指定列，从 0 开始
     * @param style    the style
     * @return style style
     */
    public boolean setCellStyle(int sheetIx, int rowIndex, int colIndex, CellStyle style) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        // sheet.autoSizeColumn(colIndex, true);// 设置列宽度自适应
        sheet.setColumnWidth(colIndex, 4000);

        Cell cell = sheet.getRow(rowIndex).getCell(colIndex);
        cell.setCellStyle(style);

        return true;
    }

    /**
     * 建立一个样式（比如给标题给出内置特殊样式 type就传1即可）
     *
     * @param rowType 参考枚举@RowType
     * @return cell style
     */
    public CellStyle makeStyle(RowType rowType) {
        CellStyle style = workbook.createCellStyle();
        //内容居中
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        //针对时间格式 进行特殊处理一下
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("@")); // // 内容样式 设置单元格内容格式是文本

        //设置边框样式
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        // 文字样式
        Font font = workbook.createFont();
        if (rowType == RowType.HEADER) {
            //颜色样式
            style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex()); //前景颜色
            style.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());//背景色
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);// 填充方式
            font.setBold(false); //标题文字加粗
            font.setFontHeight((short) 300);
            font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        } else if (rowType == RowType.NORMER) {
            font.setFontHeight((short) 150);
        }
        style.setFont(font);
        return style;
    }

    /**
     * 合并单元格（这个功能很高级）
     *
     * @param sheetIx  指定 Sheet 页，从 0 开始
     * @param firstRow 开始行
     * @param lastRow  结束行
     * @param firstCol 开始列
     * @param lastCol  结束列
     */
    public void region(int sheetIx, int firstRow, int lastRow, int firstCol, int lastCol) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    /**
     * 指定行是否为空
     *
     * @param sheetIx  指定 Sheet 页，从 0 开始
     * @param rowIndex 指定开始行，从 0 开始
     * @return true 不为空，false 不行为空
     */
    public boolean isRowNull(int sheetIx, int rowIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        return sheet.getRow(rowIndex) == null;
    }

    /**
     * 创建行，若行存在，则清空
     *
     * @param sheetIx  指定 sheet 页，从 0 开始
     * @param rowIndex the row index
     * @return boolean boolean
     */
    public boolean createRow(int sheetIx, int rowIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        sheet.createRow(rowIndex);
        return true;
    }

    /**
     * 指定单元格是否为空
     *
     * @param sheetIx  指定 Sheet 页，从 0 开始
     * @param rowIndex 指定开始行，从 0 开始
     * @param colIndex 指定开始列，从 0 开始
     * @return true 行不为空，false 行为空
     */
    public boolean isCellNull(int sheetIx, int rowIndex, int colIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        if (!isRowNull(sheetIx, rowIndex)) {
            return false;
        }
        Row row = sheet.getRow(rowIndex);
        return row.getCell(colIndex) == null;
    }

    /**
     * 创建单元格
     *
     * @param sheetIx  指定 sheet 页，从 0 开始
     * @param rowIndex 指定行，从 0 开始
     * @param colIndex 指定创建列，从 0 开始
     * @return true 列为空，false 行不为空
     */
    public boolean createCell(int sheetIx, int rowIndex, int colIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        Row row = sheet.getRow(rowIndex);
        row.createCell(colIndex);
        return true;
    }

    /**
     * 返回sheet 中的行数
     *
     * @param sheetIx 指定 Sheet 页，从 0 开始
     * @return row count
     */
    public int getRowCount(int sheetIx) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        if (sheet.getPhysicalNumberOfRows() == 0) {
            return 0;
        }
        return sheet.getLastRowNum() + 1;

    }

    /**
     * 返回所在行的列数
     *
     * @param sheetIx  指定 Sheet 页，从 0 开始
     * @param rowIndex 指定行，从0开始
     * @return 返回 -1 表示所在行为空
     */
    public int getColumnCount(int sheetIx, int rowIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        Row row = sheet.getRow(rowIndex);
        return row == null ? -1 : row.getLastCellNum();

    }

    /**
     * 设置row 和 column 位置的单元格值
     *
     * @param sheetIx  指定 Sheet 页，从 0 开始
     * @param rowIndex 指定行，从0开始
     * @param colIndex 指定列，从0开始
     * @param value    值
     * @return value at
     */
    public boolean setValueAt(int sheetIx, int rowIndex, int colIndex, String value) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        sheet.getRow(rowIndex).getCell(colIndex).setCellValue(value);
        return true;
    }

    /**
     * 返回 row 和 column 位置的单元格值
     *
     * @param sheetIx  指定 Sheet 页，从 0 开始
     * @param rowIndex 指定行，从0开始
     * @param colIndex 指定列，从0开始
     * @return value at
     */
    public String getValueAt(int sheetIx, int rowIndex, int colIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        return getCellValueToString(sheet.getRow(rowIndex).getCell(colIndex));
    }

    /**
     * 重置指定行的值
     *
     * @param sheetIx  指定 Sheet 页，从 0 开始
     * @param rowData  数据
     * @param rowIndex 指定行，从0开始
     * @return row value
     */
    public boolean setRowValue(int sheetIx, List<String> rowData, int rowIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        Row row = sheet.getRow(rowIndex);
        for (int i = 0; i < rowData.size(); i++) {
            row.getCell(i).setCellValue(rowData.get(i));
        }
        return true;
    }

    /**
     * 返回指定行的值的集合
     *
     * @param sheetIx  指定 Sheet 页，从 0 开始
     * @param rowIndex 指定行，从0开始
     * @return row value
     */
    public List<String> getRowValue(int sheetIx, int rowIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        Row row = sheet.getRow(rowIndex);
        List<String> list = new ArrayList<>();
        if (row == null) {
            list.add(null);
        } else {
            for (int i = 0; i < row.getLastCellNum(); i++) {
                list.add(getCellValueToString(row.getCell(i)));
            }
        }
        return list;
    }

    /**
     * 返回列的值的集合
     *
     * @param sheetIx  指定 Sheet 页，从 0 开始
     * @param rowIndex 指定行，从0开始
     * @param colIndex 指定列，从0开始
     * @return column value
     */
    public List<String> getColumnValue(int sheetIx, int rowIndex, int colIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        List<String> list = new ArrayList<>();
        for (int i = rowIndex; i < getRowCount(sheetIx); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                list.add(null);
                continue;
            }
            list.add(getCellValueToString(sheet.getRow(i).getCell(colIndex)));
        }
        return list;
    }

    /**
     * 获取excel 中sheet 总页数
     *
     * @return sheet count
     */
    public int getSheetCount() {
        return workbook.getNumberOfSheets();
    }

    /**
     * 创建一个sheet
     */
    public void createSheet() {
        workbook.createSheet();
    }

    /**
     * 设置sheet名称，长度为1-31，不能包含后面任一字符: ：\ / ? * [ ]
     *
     * @param sheetIx 指定 Sheet 页，从 0 开始，//
     * @param name    the name
     * @return sheet name
     */
    public boolean setSheetName(int sheetIx, String name) {
        workbook.setSheetName(sheetIx, name);
        return true;
    }

    /**
     * 获取 sheet名称
     *
     * @param sheetIx 指定 Sheet 页，从 0 开始
     * @return sheet name
     */
    public String getSheetName(int sheetIx) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        return sheet.getSheetName();
    }

    /**
     * 获取sheet的索引，从0开始
     *
     * @param name sheet 名称
     * @return -1表示该未找到名称对应的sheet
     */
    public int getSheetIndex(String name) {
        return workbook.getSheetIndex(name);
    }

    /**
     * 删除指定sheet
     *
     * @param sheetIx 指定 Sheet 页，从 0 开始
     * @return boolean boolean
     */
    public boolean removeSheetAt(int sheetIx) {
        workbook.removeSheetAt(sheetIx);
        return true;
    }

    /**
     * 删除指定sheet中行，改变该行之后行的索引
     *
     * @param sheetIx  指定 Sheet 页，从 0 开始
     * @param rowIndex 指定行，从0开始
     * @return boolean boolean
     */
    public boolean removeRow(int sheetIx, int rowIndex) {
        Sheet sheet = workbook.getSheetAt(sheetIx);
        sheet.shiftRows(rowIndex + 1, getRowCount(sheetIx), -1);
        Row row = sheet.getRow(getRowCount(sheetIx) - 1);
        sheet.removeRow(row);
        return true;
    }

    /**
     * 设置sheet 页的索引
     *
     * @param sheetname Sheet 名称
     * @param sheetIx   the sheet ix
     */
    public void setSheetOrder(String sheetname, int sheetIx) {
        workbook.setSheetOrder(sheetname, sheetIx);
    }

    /**
     * 清空指定sheet页（先删除后添加并指定sheetIx）
     *
     * @param sheetIx 指定 Sheet 页，从 0 开始
     * @return boolean boolean
     */
    public boolean clearSheet(int sheetIx) {
        String sheetname = getSheetName(sheetIx);
        removeSheetAt(sheetIx);
        workbook.createSheet(sheetname);
        setSheetOrder(sheetname, sheetIx);
        return true;
    }

    /**
     * 对外提供关闭流的方法（使用完后记得关流）
     */
    public void close() {
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("关流失败~", e);
        }
    }

    /**
     * 转换单元格的类型为String 默认的 <br>
     * 默认的数据类型：CELL_TYPE_BLANK(3), CELL_TYPE_BOOLEAN(4),
     * CELL_TYPE_ERROR(5),CELL_TYPE_FORMULA(2), CELL_TYPE_NUMERIC(0),
     * CELL_TYPE_STRING(1)
     *
     * @param cell the cell
     * @return cell value to string
     */
    private String getCellValueToString(Cell cell) {
        String strCell = "";
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                // 如果数字过长且不是日期格式，则防止当数字过长时以科学计数法显示
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                strCell = cell.toString();
                break;
            case Cell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            default:
                break;
        }
        return strCell;
    }

    @Override
    public String toString() {
        return "共有 " + getSheetCount() + "个sheet 页！";
    }

    /**
     * 可以把指定的sheet等toString输出
     *
     * @param sheetIx the sheet ix
     * @return the string
     */
    public String toString(int sheetIx) {
        return "第 " + (sheetIx + 1) + "个sheet 页，名称： " + getSheetName(sheetIx) + "，共 " + getRowCount(sheetIx) + "行！";
    }

    /**
     * 把Workbook可以提供给外部访问
     *
     * @return Workbook
     */
    public Workbook getWorkbook() {
        return workbook;
    }

}