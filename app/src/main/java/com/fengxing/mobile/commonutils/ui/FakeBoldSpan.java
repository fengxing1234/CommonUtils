package com.fengxing.mobile.commonutils.ui;

import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.CharacterStyle;

public class FakeBoldSpan extends CharacterStyle {
    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setFakeBoldText(true);
        tp.setStyle(Paint.Style.FILL_AND_STROKE);
    }
}
