package com.fengxing.mobile.commonutils.test;

import android.content.Context;
import android.support.annotation.Nullable;

import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.fengxing.mobile.plugin.swipe_back.ViewDragHelper;


public class TestViewDragHelperView extends LinearLayout {

    private ViewDragHelper mViewDragHelper;

    public TestViewDragHelperView(Context context) {
        this(context, null);
    }

    public TestViewDragHelperView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestViewDragHelperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewDragHelper = ViewDragHelper.create(this, new TestCallBack());
    }

    private static class TestCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }
    }
}
