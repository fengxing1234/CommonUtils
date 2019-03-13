package com.fengxing.mobile.plugin.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fengxing.mobile.plugin.swipe_back.SwipeBackActivity;

public class BaseActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(true);
    }

    @Override
    public void onEdgeTouch(int edge) {
        super.onEdgeTouch(edge);
    }

    @Override
    public void onContentViewSwipedBack() {
        super.onContentViewSwipedBack();
    }
}
