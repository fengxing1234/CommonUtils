package com.fengxing.mobile.commonutils.test.ui.scroller;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 模拟Viewpager滑动效果
 */
public class TestScrollerLayout extends ViewGroup {

    private static final String TAG = "TestScrollerLayout";


    //用于完成滚动操作的实例
    private Scroller mScroller;

    //判定为拖动的最小移动像素数
    private int mTouchSlop;
    private int mRightBorder;
    private int mLeftBorder;
    private float mXDown;
    private float mXLastDown;
    private float mXLastMove;
    private float mXMove;


    public TestScrollerLayout(Context context) {
        this(context, null);
    }

    public TestScrollerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestScrollerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mScroller = new Scroller(context);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 为ScrollerLayout中的每一个子控件测量大小
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout: " + changed);
        Log.d(TAG, "left : " + l + "  right : " + r + "  Bottom :" + b + "  Top " + t);
        if (changed) {

            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                // 为ScrollerLayout中的每一个子控件在水平方向上进行布局
                int measuredWidth = child.getMeasuredWidth();
                int measuredHeight = child.getMeasuredHeight();

                Log.d(TAG, "measuredWidth: " + measuredWidth);
                Log.d(TAG, "measuredHeight: " + measuredHeight);

                child.layout(i * measuredWidth, 0, (i + 1) * measuredWidth, measuredHeight + i * 50);
            }
            // 初始化左右边界值
            mLeftBorder = getChildAt(0).getLeft();
            mRightBorder = getChildAt(childCount - 1).getRight();

            Log.d(TAG, "mLeftBorder: " + mLeftBorder);

            Log.d(TAG, "mRightBorder: " + mRightBorder);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                mXLastMove = mXDown;
                Log.d(TAG, "onInterceptTouchEvent: Down" + mXDown);
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                mXLastMove = mXMove;
                Log.d(TAG, "onInterceptTouchEvent: Move" + mXMove);
                if (diff > mTouchSlop) {
                    //当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mXMove = event.getRawX();
                int scrolledX = (int) (mXLastMove - mXMove);
                int scrollX = getScrollX();

                Log.d(TAG, "onTouchEvent: mXLastMove" + mXLastMove);

                Log.d(TAG, "onTouchEvent: mXMove" + mXMove);

                Log.d(TAG, "onTouchEvent  scrolledX : " + scrolledX);

                Log.d(TAG, "onTouchEvent  scrollX : " + scrollX);
                int width = getWidth();
                Log.d(TAG, "onTouchEvent: getWidth() = " + width);


                if (scrollX + scrolledX < mLeftBorder) {
                    scrollTo(mLeftBorder, 0);
                    return true;
                } else if (scrollX + width + scrolledX > mRightBorder) {
                    Log.d(TAG, "onTouchEvent RightBorder: " + scrollX + width + scrolledX);
                    scrollTo(mRightBorder - width, 0);
                    return true;
                }

                scrollBy(scrolledX, 0);
                mXLastMove = mXMove;
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: UP");
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面


                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                int dx = targetIndex * getWidth() - getScrollX();
                // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
//                if (targetIndex > 2) {
//                    targetIndex = 2;
//                }
//                scrollTo(targetIndex * getWidth(), 0);
                Log.d(TAG, "onTouchEvent: index" + targetIndex);
                Log.d(TAG, "onTouchEvent: dx" + dx);
                mScroller.startScroll(getScrollX(), 0, dx, 0);
                invalidate();
                break;
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: Down");
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            Log.d(TAG, "computeScroll: " + mScroller.getCurrX());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
