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

import com.fengxing.mobile.plugin.swipe_back.ViewDragHelper;


/**
 * ViewDragHelper使用练习
 * <p>
 * 将 child 安置到坐标 (finalLeft,finalTop) 的位置。
 * settleCapturedViewAt(int finalLeft, int finalTop)
 * <p>
 * mDragHelper.isEdgeTouched(mEdgeFlag, i)
 * 检查具有指定ID的指针最初是否触摸了指定的任何边缘。 如果当前没有当前活动的手势或者当前没有指定ID的指针，则此方法将返回false。
 * <p>
 * mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL, i)
 * 检查当前手势中跟踪的指定指针是否超过了所需的斜率阈值。
 * 这个方法主要就是检查手指移动的距离有没有超过触发处理移动事件的最短距离（mTouchSlop）了，
 * 注意dx和dy指的是当前触摸点到ACTION_DOWN触摸到的点的距离。
 * 这里先检查Callback的getViewHorizontalDragRange(child)和getViewVerticalDragRange(child)是否大于0，
 * 如果想让某个View在某个方向上滑动，就要在那个方向对应的方法里返回大于0的数。
 * 否则在processTouchEvent()的ACTION_MOVE部分就不会调用tryCaptureViewForDrag()来捕获当前触摸到的View了，拖动也就没办法进行了。
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
        /**
         * 是否应该拦截 children 的触摸事件，
         * 只有拦截了 ViewDragHelper 才能进行后续的动作
         */
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 处理 ViewGroup 中传递过来的触摸事件序列
         *在 ViewGroup 中的 onTouchEvent() 方法中处理
         */
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
         * 该方法在child（需要捕捉的View）位置改变时执行，参数left（top）跟之前介绍方法中含义相同，
         * 为child最新的left（top）位置，而dx（dy）是child相较于上一次移动时水平（垂直）方向上改变的距离。
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
