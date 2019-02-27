package com.fengxing.mobile.plugin.common.utils;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.ColorUtils;

public class ColorTools {

    /**
     * 判断颜色是否是亮色
     * https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
     * 判断颜色的亮暗
     *
     * @param color
     * @return
     */
    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        if (darkness < 0.5) {
            return false; // It's a light color
        } else {
            return true; // It's a dark color
        }
    }

    /**
     * 同上
     *
     * @param color
     * @return
     */
    public static boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }
}
