package com.example.web.controller;

import com.example.web.util.DelTagsUtil;
import com.example.web.util.PDFUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/")
public class TestPdf {
    private Logger logger = LoggerFactory.getLogger(TestPdf.class);

    @ApiOperation(value = "富文本转PDF")
    @RequestMapping(value = "projectExport", method = RequestMethod.GET)
    public void projectExport(HttpServletResponse response) {
        try {

            Map map = new HashMap<String, Object>();
            //自定义标签去除
            String htmlStr = "<p>dataHtml</p>\n" +
                    "\n" +
                    "<h1 class=\"rich-head\">第一个<a name=\"0\"></a><a name=\"0\"></a><a name=\"0\"></a><a name=\"0\"></a><a name=\"0\"></a><a name=\"0\"></a><a name=\"0\"></a><a name=\"0\"></a><a name=\"0\"></a></h1>\n" +
                    "\n" +
                    "<p>&nbsp;</p>\n" +
                    "\n" +
                    "<h2 class=\"rich-head\">dataHtml<a href=\"http://192.168.10.59:9000/gzxf12/gzxf/2020-12-03/fe1329b119e948da86cb34a25a927339.pdf\" target=\"_blank\">fe1329b119e948da86cb34a25a927339.pdf</a><a name=\"1\"></a><a name=\"1\"></a><a name=\"1\"></a><a name=\"1\"></a><a name=\"1\"></a><a name=\"1\"></a><a name=\"1\"></a><a name=\"1\"></a><a name=\"1\"></a><a name=\"1\"></a></h2>\n" +
                    "\n" +
                    "<h3 class=\"rich-head\">第二个<a name=\"2\"></a><a name=\"2\"></a><a name=\"2\"></a><a name=\"2\"></a><a name=\"2\"></a><a name=\"2\"></a><a name=\"2\"></a><a name=\"2\"></a><a name=\"2\"></a><a name=\"2\"></a></h3>\n" +
                    "\n" +
                    "<p>1</p>\n" +
                    "\n" +
                    "<h2 class=\"rich-head\">第三个<a name=\"3\"></a><a name=\"3\"></a><a name=\"3\"></a></h2>\n" +
                    "\n" +
                    "<img height=\"439\" src=\"http://192.168.10.59:9000/gzxf12/gzxf/2020-12-08/deac6dc72b0f4c8b8fedc90a2d08dafc.png\" width=\"992\"/>\n";

           /* Whitelist user_content_filter = Whitelist.basicWithImages();
            Whitelist whitelist = user_content_filter.removeProtocols("img", "src", "http", "https");
            String textFromHtml = Jsoup.clean(htmlStr, whitelist);*/

            String textFromHtml = DelTagsUtil.getTextFromHtml(htmlStr);

            map.put("test", textFromHtml);
            ByteArrayOutputStream baos = PDFUtil.createPDF("templates/project.html", map);
            //设置response文件头

            PDFUtil.renderPdf(response, baos.toByteArray(), "pdf文件");
            baos.close();
        } catch (Exception e) {
            logger.info("导出报错", e);
        }
    }
}
