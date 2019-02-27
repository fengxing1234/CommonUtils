package com.fengxing.mobile.plugin.common.utils;

import android.os.Build;

public class BuildVersionTool {

    /**
     * 大于等于19 4.4
     *
     * @return
     */
    public static boolean dd19KITKAT4_4() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * API 小于19 4.4
     *
     * @return
     */
    public static boolean x19KITKAT4_4() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT;
    }


    /**
     * 大于等于21 5.0
     *
     * @return
     */
    public static boolean dd21LOLLIPOP5_0() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 小于 21 5.0
     *
     * @return
     */
    public static boolean xd21LOLLIPOP5_0() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * API 大于等于 23 6。0
     *
     * @return
     */
    public static boolean dd23M6_0() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * API 小于 23 6。0
     *
     * @return
     */
    public static boolean x23M6_0() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }
}
