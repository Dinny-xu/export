package com.example.web.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yang Xu
 * @date 2019/12/31 15:29
 * @description: 驼峰和下划线转换实现类
 */
public class HumpLineUtil {

    private static Pattern humpPattern = Pattern.compile("[A-Z]");
    private static Pattern linePattern = Pattern.compile("(_)(\\w)");

    /**
     * 驼峰转下划线
     *
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        return str.replaceAll("[A-Z]", "_$0").toLowerCase();
    }

    /**
     * 下划线转驼峰
     *
     * @param str
     * @return
     */
    public static String lineToHump(String str) {
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(2).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
