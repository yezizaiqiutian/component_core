package com.gh.lib_core.utils;

import android.util.Log;

/**
 * @author: gh
 * @description: 日志管理类
 * @date: 2022/3/23.
 * @from: 官网
 * todo 日志工具有很多可完善的地方,如debug不打印日志,堆栈信息打印,线程信息打印,存入文件,等等(参考慕课)
 */
public class LogUtils {
    public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final String TAG = "LogUtils";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            if (tag == null || tag.length() == 0
                    || msg == null || msg.length() == 0)
                return;

            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.d(tag, msg);
            } else {
                while (msg.length() > segmentSize) {// 循环分段打印日志
                    String logContent = msg.substring(0, segmentSize);
                    msg = msg.replace(logContent, "");
                    Log.d(tag, logContent);
                }
                Log.d(tag, msg);// 打印剩余日志
            }
        }

    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }
}
