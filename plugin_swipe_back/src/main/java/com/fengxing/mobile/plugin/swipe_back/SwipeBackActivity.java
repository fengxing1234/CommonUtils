package com.fengxing.mobile.plugin.swipe_back;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fengxing.mobile.plugin.swipe_back.ui.SwipeBackActivityBase;
import com.fengxing.mobile.plugin.swipe_back.ui.SwipeBackLayout;
import com.fengxing.mobile.plugin.swipe_back.utils.TranslucentUtils;


public class SwipeBackActivity extends AppCompatActivity implements SwipeBackActivityBase, SwipeBackLayout.SwipeListener {

    private static final String TAG = SwipeBackActivity.class.getSimpleName();
    private SwipeBackActivityHelper mSwipeBackHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSwipeBackHelper = new SwipeBackActivityHelper(this);
        mSwipeBackHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mSwipeBackHelper.onPostCreate();
    }

    @Override
    public <T extends View> T findViewById(int id) {
        T view = super.findViewById(id);
        if (view == null && mSwipeBackHelper != null) {
            return mSwipeBackHelper.findViewById(id);
        }
        return view;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setSwipeBackEnable(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        TranslucentUtils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    /**
     * 当拖拽状态发生改变时
     *
     * @param state
     * @param scrollPercent
     */
    @Override
    public void onScrollStateChange(int state, float scrollPercent) {
        Log.d(TAG, "onScrollStateChange: state = " + state + "  scrollPercent = " + scrollPercent);
    }

    /**
     * 当边缘触摸时 触发
     *
     * @param edge
     */
    @Override
    public void onEdgeTouch(int edge) {
        Log.d(TAG, "onEdgeTouch: edge = " + edge);
    }

    /**
     * 滑动时 达到30% 触发  自定义空间中控制数值
     */
    @Override
    public void onScrollOverThreshold() {
        Log.d(TAG, "onScrollOverThreshold: " + this);
    }

    @Override
    public void onContentViewSwipedBack() {
        if (!isFinishing()) {
            finish();
            overridePendingTransition(0, 0);
        }

    }
}
