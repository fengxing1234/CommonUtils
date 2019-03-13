package com.fengxing.mobile.plugin.swipe_back;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.fengxing.mobile.plugin.swipe_back.ui.SwipeBackLayout;


public class SwipeBackActivityHelper {

    private SwipeBackLayout mSwipeBackLayout;

    private Activity mActivity;

    public SwipeBackActivityHelper(Activity activity) {
        mActivity = activity;
    }

    public void onActivityCreate() {
        //设置Window窗口透明
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置decorView没有background
        mActivity.getWindow().getDecorView().setBackground(null);
        mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(mActivity).inflate(R.layout.swipeback_layout, null, false);
    }

    /**
     * 关联Activity
     * 插入SwipeBack布局
     * phoneWindow-->DecorView-->SwipeLayout-->ContentView
     */
    public void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
    }


    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

    public <T extends View> T findViewById(int id) {
        if (mSwipeBackLayout != null) {
            return mSwipeBackLayout.findViewById(id);
        }
        return null;
    }
}
