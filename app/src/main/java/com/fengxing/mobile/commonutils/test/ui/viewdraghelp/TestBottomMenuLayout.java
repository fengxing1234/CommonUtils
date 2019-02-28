package com.fengxing.mobile.commonutils.test.ui.viewdraghelp;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ViewDragHelper 练习 底部拉出布局
 */
public class TestBottomMenuLayout extends LinearLayout {

    private static final String TAG = TestBottomMenuLayout.class.getSimpleName();
    private ImageView mContentView;
    private TextView mMenuView;
    private ViewDragHelper mDragHelper;

    public TestBottomMenuLayout(Context context) {
        super(context);
        init();
    }

    public TestBottomMenuLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestBottomMenuLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density1 = dm.density;
        int width3 = dm.widthPixels;
        int height3 = dm.heightPixels;

        Log.d(TAG, "density: " + density1);
        Log.d(TAG, "screenWidth: " + width3);
        Log.d(TAG, "screenHeight: " + height3);

        setOrientation(VERTICAL);
        mDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {

            /**
             * 捕捉到的是底部菜单的 View，内容区域的 View 不需要捕捉
             * @param view
             * @param i
             * @return
             */
            @Override
            public boolean tryCaptureView(@NonNull View view, int i) {
                return view == mMenuView;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                Log.d(TAG, "clampViewPositionHorizontal: ");
                return super.clampViewPositionHorizontal(child, left, dx);
            }

            /**
             * 返回竖直方向上要到达的位置
             * @param child
             * @param top
             * @param dy
             * @return
             */
            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                Log.d(TAG, "parent.getHeight() : " + getHeight());

                Log.d(TAG, "child.getHeight(): " + child.getHeight());

                Log.d(TAG, "Top : " + top);
                int max = Math.max(getHeight() - child.getHeight(), top);
                Log.d(TAG, "moveTop : " + max);
                return max;
            }

            /**
             * 这个方法返回 true 会锁住当前的边界。
             * @param edgeFlags
             * @return
             */
            @Override
            public boolean onEdgeLock(int edgeFlags) {
                Log.d(TAG, "onEdgeLock: ");
                return super.onEdgeLock(edgeFlags);
            }


            /**
             * 表示开始触摸到 ViewGroup 的边缘
             * @param edgeFlags
             * @param pointerId
             */
            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);
                Log.d(TAG, "onEdgeTouched: ");
            }

            /**
             * 表示用户开始从边缘拖拽
             * @param edgeFlags
             * @param pointerId
             */
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                Log.d(TAG, "onEdgeDragStarted: ");
                mDragHelper.captureChildView(mMenuView, pointerId);
            }

            /**
             * 用户松手了
             * @param releasedChild
             * @param xvel
             * @param yvel
             */
            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                if (yvel <= 0) {
                    mDragHelper.settleCapturedViewAt(0, getHeight() - mMenuView.getHeight());
                } else {
                    mDragHelper.settleCapturedViewAt(0, getHeight());
                }

                invalidate();
            }
        });
        //设置要监测的边缘拖拽。
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
    }

    @Override
    public void computeScroll() {
        if (mDragHelper != null && mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mContentView != null && mMenuView != null) {
            mContentView.layout(0, 0, mContentView.getMeasuredWidth(), mContentView.getMeasuredHeight());

            mMenuView.layout(0, getHeight(), mMenuView.getMeasuredWidth(), getHeight() + mMenuView.getMeasuredHeight());
        }

        Log.d(TAG, "onLayout: parent = " + getHeight());
        Log.d(TAG, "onLayout: contentView = " + mContentView.getHeight());
        Log.d(TAG, "onLayout: menuView = " + mMenuView.getHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = (ImageView) getChildAt(0);
        mMenuView = (TextView) getChildAt(1);
    }
}
