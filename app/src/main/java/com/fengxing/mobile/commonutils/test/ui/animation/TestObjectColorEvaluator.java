package com.fengxing.mobile.commonutils.test.ui.animation;

import android.animation.TypeEvaluator;

public class TestObjectColorEvaluator implements TypeEvaluator {

    private int mCurrentRed;
    private int mCurrentBlue;
    private int mCurrentGreen;

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        //#000000

        String startColor = (String) startValue;
        String endColor = (String) endValue;

        String startRed = startColor.substring(1, 3);
        String startBlue = startColor.substring(3, 5);
        String startGreen = startColor.substring(5, 7);

        String endRed = endColor.substring(1, 3);
        String endBlue = endColor.substring(3, 5);
        String endGreen = endColor.substring(5, 7);

        int startRedRadix = Integer.parseInt(startRed, 16);
        int startBlueRadix = Integer.parseInt(startBlue, 16);
        int startGreenRadix = Integer.parseInt(startGreen, 16);


        mCurrentRed = startRedRadix;
        mCurrentBlue = startBlueRadix;
        mCurrentGreen = startGreenRadix;

        int endRedRadix = Integer.parseInt(endRed, 16);
        int endBlueRadix = Integer.parseInt(endBlue, 16);
        int endGreenRadix = Integer.parseInt(endGreen, 16);

        int redDiff = Math.abs(endRedRadix - startRedRadix);
        int blueDiff = Math.abs(endBlueRadix - startBlueRadix);
        int greenDiff = Math.abs(endGreenRadix - startGreenRadix);

        int colorDiff = redDiff + blueDiff + greenDiff;

        if (mCurrentRed != endRedRadix) {
            mCurrentRed = getCurrentColor(startRedRadix, endRedRadix, colorDiff, 0, fraction);
        } else if (mCurrentBlue != endBlueRadix) {
            mCurrentBlue = getCurrentColor(startBlueRadix, endBlueRadix, colorDiff, 0, fraction);
        } else if (mCurrentGreen != endGreenRadix) {
            mCurrentGreen = getCurrentColor(startGreenRadix, endGreenRadix, colorDiff, 0, fraction);
        }

        String currentColor = "#" + getHexString(mCurrentRed) + getHexString(mCurrentBlue) + getHexString(mCurrentGreen);

        return currentColor;
    }

    private int getCurrentColor(int start, int end, int colorDiff, int offset, float fraction) {
        int currentColor;

        if (start > end) {
            currentColor = (int) (start - (fraction * colorDiff - offset));
            if (currentColor < end) {
                currentColor = end;
            }
        } else {
            currentColor = (int) (start + (fraction * colorDiff - offset));
            if (currentColor > end) {
                currentColor = end;
            }
        }

        return currentColor;
    }

    private String getHexString(int value) {
        String s = Integer.toHexString(value);
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }
}
