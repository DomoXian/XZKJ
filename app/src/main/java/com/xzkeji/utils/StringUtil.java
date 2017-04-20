package com.xzkeji.utils;

/**
 * Created by xianguo on 20/4/17.
 * 字符串工具
 */

@SuppressWarnings("unused")
public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
