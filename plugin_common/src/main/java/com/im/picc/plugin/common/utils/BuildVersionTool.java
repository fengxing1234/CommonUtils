package com.im.picc.plugin.common.utils;

import android.os.Build;

public class BuildVersionTool {

    /**
     * 大于等于21
     *
     * @return
     */
    public static boolean dd21LOLLIPOP5_0() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 大于等于19
     *
     * @return
     */
    public static boolean dd19KITKAT4_4() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean dd23M6_0() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
