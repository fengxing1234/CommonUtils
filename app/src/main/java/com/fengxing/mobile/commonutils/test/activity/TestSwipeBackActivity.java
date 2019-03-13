package com.fengxing.mobile.commonutils.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fengxing.mobile.commonutils.R;
import com.fengxing.mobile.plugin.common.base.BaseActivity;


public class TestSwipeBackActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_swipe_back_layout);
        findViewById(R.id.view_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestSwipeBackActivity.this, TestSwipeBackTwoActivity.class));
            }
        });

        findViewById(R.id.view_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToFinishActivity();
            }
        });
    }

    @Override
    public void onContentViewSwipedBack() {
        super.onContentViewSwipedBack();
    }
}
