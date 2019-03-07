package com.fengxing.mobile.plugin.swipe_back.ui;

public interface SwipeBackActivityBase {

    //得到SwipeBackLayout对象
    SwipeBackLayout getSwipeBackLayout();

    //设置是否可以滑动返回
    void setSwiperBackEnable();

    //自动滑动返回并关闭Activity
    void scrollToFinishActivity();
}
