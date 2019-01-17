package com.fengxing.mobile.commonutils.utils;

import android.widget.Toast;

import com.fengxing.mobile.commonutils.FXApplication;

public class ToastUtils {

    public static void toastMsg(String msg) {
        Toast.makeText(FXApplication.getFXApplication().getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void toastLong(String msg) {
        Toast.makeText(FXApplication.getFXApplication().getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static void toastShort(String msg) {
        Toast.makeText(FXApplication.getFXApplication().getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
