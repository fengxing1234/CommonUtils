package com.fengxing.mobile.commonutils.test.ui.viewdraghelp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**

 回调顺序

 shouldInterceptTouchEvent：

 DOWN:
 getOrderedChildIndex(findTopChildUnder)
 ->onEdgeTouched

 MOVE:
 getOrderedChildIndex(findTopChildUnder)
 ->getViewHorizontalDragRange &
 getViewVerticalDragRange(checkTouchSlop)(MOVE中可能不止一次)
 ->clampViewPositionHorizontal&
 clampViewPositionVertical
 ->onEdgeDragStarted
 ->tryCaptureView
 ->onViewCaptured
 ->onViewDragStateChanged

 processTouchEvent:

 DOWN:
 getOrderedChildIndex(findTopChildUnder)
 ->tryCaptureView
 ->onViewCaptured
 ->onViewDragStateChanged
 ->onEdgeTouched
 MOVE:
 ->STATE==DRAGGING:dragTo
 ->STATE!=DRAGGING:
 onEdgeDragStarted
 ->getOrderedChildIndex(findTopChildUnder)
 ->getViewHorizontalDragRange&
 getViewVerticalDragRange(checkTouchSlop)
 ->tryCaptureView
 ->onViewCaptured
 ->onViewDragStateChanged
 ---------------------
 作者：鸿洋_
 来源：CSDN
 原文：https://blog.csdn.net/lmj623565791/article/details/46858663
 版权声明：本文为博主原创文章，转载请附上博文链接！
 */
import android.content.Context;
import android.support.annotation.Nullable;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.fengxing.mobile.plugin.swipe_back.ViewDragHelper;


/**
 * ViewDragHelper使用练习
 */
public class TestViewDragHelperView extends LinearLayout {

    private static final String TAG = "TestViewDragHelperView";

    private ViewDragHelper mViewDragHelper;

    public TestViewDragHelperView(Context context) {
        this(context, null);
    }

    public TestViewDragHelperView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestViewDragHelperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new TestCallBack());
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_CANCEL && ev.getAction() == MotionEvent.ACTION_UP) {
            mViewDragHelper.cancel();
            return false;
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    private class TestCallBack extends ViewDragHelper.Callback {

        private int mCurrentLeft;
        private int mCurrentTop;


        //是否能否拖拽

        /**
         * 如何返回ture则表示可以捕获该view，你可以根据传入的第一个view参数决定哪些可以捕获
         *
         * @param child     Child the user is attempting to capture
         * @param pointerId ID of the pointer attempting the capture
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            int childLeft = child.getLeft();
            int childTop = child.getTop();
            return true;
        }

        ////////////////////////////////水平 竖直方向 移动

        /**
         * 可以在该方法中对child移动的边界进行控制，left , top 分别为即将移动到的位置，
         * 比如横向的情况下，我希望只在ViewGroup的内部移动，即：
         * 最小>=paddingleft，最大<=ViewGroup.getWidth()-paddingright-child.getWidth
         *
         * @param child Child view being dragged
         * @param left  Attempted motion along the X axis
         * @param dx    Proposed change in position for left
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.d(TAG, "clampViewPositionHorizontal: " + child);
            Log.d(TAG, "left: " + left);
            // 最小 x 坐标值不能小于 leftBound
            int leftBound = getPaddingLeft();
            Log.d(TAG, "leftBound: " + leftBound);
            // 最大 x 坐标值不能大于 rightBound
            int rightBound = getWidth() - child.getWidth() - getPaddingRight();
            Log.d(TAG, "rightBound: " + rightBound);
            Log.d(TAG, "child.getWidth(): " + child.getWidth());
            int newLeft = Math.min(Math.max(left, leftBound), rightBound);
            Log.d(TAG, "newLeft: " + newLeft);
            mCurrentLeft = newLeft;
            return newLeft;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            // 最小 y 坐标值不能小于 topBound
            int paddingTop = getPaddingTop();
            // 最大 y 坐标值不能大于 bottomBound
            int diffHeight = getHeight() - child.getHeight() - getPaddingBottom();

            int newTop = Math.min(Math.max(top, paddingTop), diffHeight);
            mCurrentTop = newTop;
            return newTop;
        }

        ////////////////////////////////操作边缘


        /**
         * 当触摸到边界时回调。
         *
         * @param edgeFlags A combination of edge flags describing the edge(s)
         *                  currently touched
         * @param pointerId ID of the pointer touching the described edge(s)
         */
        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
            Log.d(TAG, "edgeFlags: " + edgeFlags);
            Log.d(TAG, "pointerId: " + pointerId);
        }

        /**
         * 在边界拖动时回调
         *
         * @param edgeFlags A combination of edge flags describing the edge(s)
         *                  dragged
         * @param pointerId ID of the pointer touching the described edge(s)
         */
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
            View child = getChildAt(0);
            //边界移动 让哪一个view跟着移动
            mViewDragHelper.captureChildView(child, pointerId);
        }

        /**
         * true的时候会锁住当前的边界，false则unLock。
         *
         * @param edgeFlags A combination of edge flags describing the edge(s)
         *                  locked
         * @return
         */
        @Override
        public boolean onEdgeLock(int edgeFlags) {
            return super.onEdgeLock(edgeFlags);
        }


        /////////////////////////////////////View 释放

        /**
         * 手指释放的时候回调
         *
         * @param releasedChild The captured child view now being released
         * @param xvel          X velocity of the pointer as it left the screen in pixels
         *                      per second.
         * @param yvel          Y velocity of the pointer as it left the screen in pixels
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.d(TAG, "onViewReleased, xvel=" + xvel + "; yvel=" + yvel);

            int leftBound = getPaddingLeft();

            int rightBound = getWidth() - releasedChild.getWidth() - getPaddingRight();

            if (releasedChild.getWidth() / 2 + mCurrentLeft < getWidth() / 2) {//移动到左边
                //移动到指定位置
                mViewDragHelper.settleCapturedViewAt(leftBound, mCurrentTop);
            } else {//移动到右边
                mViewDragHelper.settleCapturedViewAt(rightBound, mCurrentTop);
            }
            /**
             * 大家可以看到紧随其后的代码是invalidate();
             * 因为其内部使用的是mScroller.startScroll，所以别忘了需要invalidate()以及结合computeScroll方法一起。
             */
            invalidate();
        }

        /**
         * 如果你用Button测试，或者给TextView添加了clickable = true ，都记得重写下面这两个方法
         * <p>
         * <p>
         * 主要是因为，如果子View不消耗事件，那么整个手势（DOWN-MOVE*-UP）都是直接进入onTouchEvent
         * ，在onTouchEvent的DOWN的时候就确定了captureView。如果消耗事件，那么就会先走onInterceptTouchEvent方法
         * 判断是否可以捕获，而在判断的过程中会去判断另外两个回调的方法：
         * getViewHorizontalDragRange和getViewVerticalDragRange，
         * 只有这两个方法返回大于0的值才能正常的捕获。
         *
         * @param child Child view to check
         * @return
         */

        @Override
        public int getViewHorizontalDragRange(View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }

        /**
         * 当ViewDragHelper状态发生变化时回调（IDLE,DRAGGING,SETTING[自动滚动时]）
         *
         * @param state The new drag state
         */
        @Override
        public void onViewDragStateChanged(int state) {
            Log.d(TAG, "onViewDragStateChanged: " + state);
            super.onViewDragStateChanged(state);
        }

        /**
         * 当captureview的位置发生改变时回调
         *
         * @param changedView View whose position changed
         * @param left        New X coordinate of the left edge of the view
         * @param top         New Y coordinate of the top edge of the view
         * @param dx          Change in X position from the last call
         * @param dy          Change in Y position from the last call
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            Log.d(TAG, "onViewPositionChanged: ");
        }

        /**
         * 当captureview被捕获时回调
         *
         * @param capturedChild   Child view that was captured
         * @param activePointerId Pointer id tracking the child capture
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            Log.d(TAG, "onViewCaptured: " + capturedChild);
        }

        /**
         * 改变同一个坐标（x,y）去寻找captureView位置的方法。（具体在：findTopChildUnder方法中）
         *
         * @param index the ordered position to query for
         * @return
         */
        @Override
        public int getOrderedChildIndex(int index) {
            Log.d(TAG, "getOrderedChildIndex: " + index);
            return super.getOrderedChildIndex(index);
        }
    }
}
