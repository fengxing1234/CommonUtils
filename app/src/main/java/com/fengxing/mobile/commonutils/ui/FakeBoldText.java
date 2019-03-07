package com.fengxing.mobile.commonutils.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.fengxing.mobile.commonutils.utils.FakeBoldSpan;

/**
 * 字体加粗  不是特别粗
 */
public class FakeBoldText extends TextView {

    public FakeBoldText(Context context) {
        super(context);
        init();
    }

    public FakeBoldText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FakeBoldText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        SpannableStringBuilder spannable = new SpannableStringBuilder(getText());
        spannable.setSpan(new FakeBoldSpan(), 0, length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(spannable);
    }
}
