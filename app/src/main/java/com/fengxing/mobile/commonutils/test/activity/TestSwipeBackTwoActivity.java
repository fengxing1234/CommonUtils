package com.fengxing.mobile.commonutils.test.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fengxing.mobile.commonutils.R;
import com.fengxing.mobile.plugin.swipe_back.SwipeBackActivity;

public class TestSwipeBackTwoActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_swipe_back_layout);
    }
}
