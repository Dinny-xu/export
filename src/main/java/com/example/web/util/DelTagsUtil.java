package com.example.web.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 去除文章内容页页面代码里的HTML标签
 * Created by yanyl on 2018/6/4.
 */
public class DelTagsUtil {
    /**
     * 去除html代码中含有的标签
     *
     * @param htmlStr
     * @return
     */
    public static String delHtmlTags(String htmlStr) {

        //定义script的正则表达式，去除js可以防止注入
//        String scriptRegex = "<script[^>]*?>[\\s\\S]*?</script>";
        //定义style的正则表达式，去除style样式，防止css代码过多时只截取到css样式代码
//        String styleRegex = "<style[^>]*?>[\\s\\S]*?</style>";
        //定义HTML标签的正则表达式，去除标签，只提取文字内容
//        String htmlRegex="<[^>]+>";
//        String htmlRegex = "</?(?!img|h\\\\d)[^>]+>";
//        String htmlRegex = "</?(?!img|h\\\\d)[^>]+>";
        //定义空格,回车,换行符,制表符
//        String spaceRegex = "\\s*|\t|\r|\n";

        // 过滤script标签
//        htmlStr = htmlStr.replaceAll(scriptRegex, "");
        // 过滤style标签
//        htmlStr = htmlStr.replaceAll(styleRegex, "");
        // 过滤html标签
//        htmlStr = htmlStr.replaceAll(htmlRegex, "");
        // 过滤空格等
//        htmlStr = htmlStr.replaceAll(spaceRegex, "");

        return htmlStr.trim(); // 返回文本字符串
    }

    /**
     * 获取HTML代码里的内容
     *
     * @param htmlStr
     * @return
     */
    public static String getTextFromHtml(String htmlStr) {
        //去除html标签
        htmlStr = delHtmlTags(htmlStr);
        //去除空格" "
        htmlStr = htmlStr.replaceAll(" ", "");
        return htmlStr;
    }

    public static Set<String> getImgStr(String htmlStr) {
        Set<String> pics = new HashSet<>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }


    public static void main(String[] args) {
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

     /*   Whitelist user_content_filter = Whitelist.basicWithImages();
//        Whitelist whitelist = user_content_filter.removeProtocols("img", "src", "http", "https");
        String textFromHtml = Jsoup.clean(htmlStr, user_content_filter);
        System.out.println(textFromHtml);*/
        String textFromHtml = getTextFromHtml(htmlStr);
        System.out.println(textFromHtml);

//        String cc = "/<img.*?src=[\\\"|\\']?(.*?)[\\\"|\\']?\\s.*?>/i";
//         String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
  /*      String regEx_img = "<img.*src\\\\s*=\\\\s*(.*?)[^>]*?>";

        if (Pattern.matches(regEx_img, htmlStr)) {
            String replace = textFromHtml.replace(regEx_img, "/");
            System.out.println(replace);
        }
*/
       /* String img;
        Pattern p_image;
        Matcher m_image;
        // String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
//        String regEx_img = "<img.*src\\\\s*=\\\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile("^data:image/(png|gif|jpg|jpeg|bmp|tif|psd|ICO);base64,.*");
        //p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            System.out.println(img);
        }*/


 /*       String textFromHtml = getTextFromHtml(htmlStr);
        String replace = textFromHtml.replace("\\>$/", "/>");
        System.out.println(replace);*/

//        System.out.println(getTextFromHtml(htmlStr));


      /*  String[] split = textFromHtml.split("\"");

        for (int i = 0; i < split.length; i++) {

            String s = split[i];

            String x = ".*(png)|(jpg)|(jpeg).*";

            if (Pattern.matches(x, s)) {

                System.out.println(s);
            }

        }*/


//        Whitelist user_content_filter = Whitelist.basicWithImages();
//        Whitelist whitelist = user_content_filter.removeProtocols("img", "src", "http", "https");
//        System.out.println(Jsoup.clean(htmlStr, Whitelist.basicWithImages()));


    }


}