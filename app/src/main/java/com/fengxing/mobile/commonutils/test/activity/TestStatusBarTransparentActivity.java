package com.fengxing.mobile.commonutils.test.activity;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fengxing.mobile.commonutils.R;
import com.fengxing.mobile.plugin.common.utils.BuildVersionTool;
import com.fengxing.mobile.plugin.common.utils.StatusBarTools;


public class TestStatusBarTransparentActivity extends AppCompatActivity {

    private boolean isEnableTransparent = false;

    private boolean isDarkMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_status_bar_activity);
        findViewById(R.id.iv_src).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDarkMode = StatusBarTools.changeStatusBarTextColor(getWindow(), !isDarkMode);
            }
        });
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }


//        int statusBarHeight1 = StatusBarTools.getStatusBarHeight(this);
//        int statusBarHeight2 = StatusBarTools.getStatusBarHeight2(this);
//
//        StatusBarTools.setStatusBarColor(getWindow(), getResources().getColor(R.color.aqua));

        int color = getResources().getColor(R.color.red);


//        Window window = getWindow();
//        if (BuildVersionTool.dd21LOLLIPOP5_0()) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(color);
//        } else {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            ViewGroup systemContent = findViewById(android.R.id.content);
//            View view = new View(this);
//            int statusBarHeight = StatusBarTools.getStatusBarHeight(this);
//            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
//            view.setBackgroundColor(color);
//            systemContent.getChildAt(0).setFitsSystemWindows(true);
//            systemContent.addView(view, 0, lp);
//        }
//
//        StatusBarTools.autoChangeStatusBarTextColor(getWindow(), color);


        //StatusBarTools.transparencyBar(this);
        //StatusBarTools.statusBarTransparent(getWindow());
        //StatusBarTools.setStatusBarColor(getWindow(),color);

        //StatusBarTools.setColor(this,getResources().getColor(R.color.blueviolet));
        StatusBarTools.transparentStatusBar(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isEnableTransparent && hasFocus && BuildVersionTool.dd19KITKAT4_4()) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    private void statusTransparent() {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(options);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 全屏
     */
    private void fullScreen() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}
