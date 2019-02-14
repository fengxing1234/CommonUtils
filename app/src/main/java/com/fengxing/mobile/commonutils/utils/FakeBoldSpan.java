package com.fengxing.mobile.commonutils.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.CharacterStyle;

public class FakeBoldSpan extends CharacterStyle {

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setFakeBoldText(true);//一种伪粗体效果，比原字体加粗的效果弱一点


        tp.setStyle(Paint.Style.FILL_AND_STROKE);
        tp.setColor(Color.BLACK);//字体颜色
//        tp.setStrokeWidth(2);//控制字体加粗的程度

        //使用:
        //fakeBoldText.setText(new Spanny().append("FakeBold",new FakeBoldSpan()));
    }
}
