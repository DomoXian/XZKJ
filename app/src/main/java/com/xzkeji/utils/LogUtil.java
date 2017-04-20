package com.xzkeji.utils;

import android.util.Log;

/**
 * Created by xianguo on 19/4/17.
 * 日志打印
 */

public class LogUtil {
    private static final String TAG = "xzkeji";

    public static void e(String content){
        Log.e(TAG,content);
    }

    public static void v(String content){
        Log.v(TAG,content);
    }

    public static void debug(String content){
        Log.d(TAG,content);
    }
}
