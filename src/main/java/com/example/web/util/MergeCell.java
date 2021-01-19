package com.example.web.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

/**
 * 思路：使用嵌套表格对象完成数据，图片左右布局
 * 1、创建document对象。
 * 2、创建表格对象PdfPTable headerTable。两列的表格对象。图片分为一列，数据划分为一列。
 * 3、创建左边数据表格PdfPTable iTable，划分为N列
 * 4、往左边表格中写入数据，加入iTable中
 * 5、创建图片对象,加入headerTable中
 *
 * @author liucong
 */
public class MergeCell {
    public static void main(String[] args) {
        float lineHeight1 = (float) 25.0;
        float lineHeight2 = (float) 25.0;
        try {
            //新建document对象
            Document pdfDoc = new Document(PageSize.A4.rotate(), 36, 36, 24, 36);
            // 构造好的pdf文件输出位置
            PdfWriter.getInstance(pdfDoc, new FileOutputStream("/Users/xu/Desktop/1111/test.pdf"));
            //打开pdf文件---注：只有document 打开后，才能往文件内写入导出信息
            pdfDoc.open();

            //PDFTable类似于html的表格文件，但是只有列的概念，定义列的数量，不能定义行的数量。
            //创建一个两列的表格
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(40);

            //3、创建左边数据表格PdfPTable iTable，划分为N列

            PdfPTable leftTable = new PdfPTable(4);//创建左边表格
            //4、往左边表格中写入数据，加入iTable中
            //4-1表格中可以合并单元格
            PdfPCell leftCell = new PdfPCell(new Paragraph("Hello"));
            leftCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            leftCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            leftCell.setFixedHeight(lineHeight1);
            leftCell.setColspan(4);
            leftTable.addCell(leftCell);
            //4-2填充数据
            for (int i = 0; i < 24; i++) {
                leftCell = new PdfPCell(new Paragraph("data"));
                leftCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                leftCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                leftCell.setFixedHeight(lineHeight2);
                leftTable.addCell(leftCell);
            }
            //将表格加入到第一列中
            PdfPCell cell = new PdfPCell(leftTable);
            cell.setPadding(0);
            headerTable.addCell(cell);

            //5、创建图片对象,加入headerTable中,此处写入图片路径
            Image image = Image.getInstance("https://cdn.xycloud.site/WX20201202-144438.png");
            image.scaleAbsolute(5, 5);//自定义大小
            image.scalePercent(50);//缩放百分比 --- 测试不起作用
            image.scaleToFit(50, 50);//自定义大小
            PdfPCell acell = new PdfPCell(image, false);
            headerTable.addCell(acell);
            //将主要的表格headerTable加入document文档对象中
            pdfDoc.add(headerTable);
            //关闭文档对象，注：当文档对象真正关闭后，数据才会写入文件中。
            pdfDoc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
