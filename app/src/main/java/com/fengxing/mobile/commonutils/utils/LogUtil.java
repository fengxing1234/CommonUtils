package com.fengxing.mobile.commonutils.utils;

import android.util.Log;

public class LogUtil {

    private static final String TAG = "LogUtil";

    public static void e(String tag, String msg) {
        log(tag, msg);
    }

    private static void log(String tag, String msg) {
        Log.d(tag, msg);
    }
}
