package com.fengxing.mobile.plugin.common.utils;

import android.util.Log;

public class LogUtil {

    private static final String TAG = "LogUtil";

    public static void d(String tag, String msg) {
        log(tag, msg);
    }

    public static void d(String tag, int msg) {
        log(tag, msg + "");
    }

    private static void log(String tag, String msg) {
        Log.d(tag, msg);
    }
}
