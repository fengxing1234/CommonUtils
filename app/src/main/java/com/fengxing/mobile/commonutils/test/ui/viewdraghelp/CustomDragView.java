package com.fengxing.mobile.commonutils.test.ui.viewdraghelp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * 不借助ViewDragHelp 自己完成类似效果 (拖拽效果)
 */
public class CustomDragView extends FrameLayout {

    /**
     * 涉及到触摸的话，ViewGroup 自然要在 onTouchEvent() 和 onInterceptTouchEvent() 两个方法中处理。
     * onInterceptTouchEvent() 主要是用来决定是否拦截 childView 的触摸操作，这里面为了方便演示，统一处理为 true，也就是拦截。
     * <p>
     * onTouchEvent() 在这个方法中，ViewGroup 用来处理触摸的具体流程。也就是对应上图的触摸、移动、释放手指。
     * <p>
     * 在 Android 中 MotionEvent 封装了触摸时的各种状态。所以我们主要处理的状态有以下：
     * 1. MotionEvent.ACTION_DOWN: 在这个状态时，标记手指按下屏幕。我们需要判断当前触摸的地方是否落在 childview 的显示区域，如果是则标记拖拽状态开始，我们需要记录手指的触摸位置为原始坐标。
     * 2. MotionEvent.ACTION_MOVE: 这个状态自然代表手指的移动过程，这个时候我们仍然需要记录手指触摸新的坐标，然后如果是在触摸开始的状态，则将 childview 进行位置偏移，偏移量就是新坐标与原始坐标的偏差。
     * 3. MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCLE：这两者都是表明手指离开了屏幕，这个时候如果一个 childview 正在拖拽，那么需要标记拖拽状态结束，至于 View 根据实际需要，通常是停留在新的坐标或者是回弹到原来的地方。
     * <p>
     * 知道了流程，我们就可以开始编码，我们可以新建一个 ViewGroup 命名为 DragViewGroup，为了简便起见，让它继承自 FrameLayout。之后实现它的 onInterceptTouchEvent() 和 onTouchEvent()。
     */


    private static final String TAG = CustomDragView.class.getSimpleName();


    //最小有效滑动距离
    private int mSlop;

    private View mDragView;
    private float mLastPointX;
    private float mLastPointY;
    private float mInitY;
    private float mInitX;
    private float mLastRawY;
    private float mLastRawX;
    private Scroller scroller;
    private Scroller mScroller;

    enum State {
        IDLE,
        DRAGGING
    }

    private State mCurrentState;


    public CustomDragView(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomDragView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomDragView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mScroller = new Scroller(getContext());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(ev);
        Log.d(TAG, "onInterceptTouchEvent: " + onInterceptTouchEvent);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float rawX = event.getRawX();
        float rawY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isClickOnChildView(event)) {
                    mLastPointX = event.getX();
                    mLastPointY = event.getY();
                    mCurrentState = State.DRAGGING;

                    mLastRawX = rawX;
                    mLastRawY = rawY;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int diffX = (int) (event.getX() - mLastPointX);
                int diffY = (int) (event.getY() - mLastPointY);

                int diffRawX = (int) (event.getRawX() - mLastRawX);
                int diffRawY = (int) (event.getRawY() - mLastRawY);

                if (mCurrentState == State.DRAGGING && mDragView != null && (Math.abs(diffX) > mSlop || Math.abs(diffY) > mSlop)) {
                    Log.d(TAG, "diff x :" + diffX + "  diffY = " + diffY);
                    //方式一 有抖动
                    ViewCompat.offsetLeftAndRight(mDragView, (int) diffX);
                    ViewCompat.offsetTopAndBottom(mDragView, (int) diffY);

                    //方式二 layout 相对坐标
                    //还有是稍微的抖动

//                    mDragView.layout(
//                            mDragView.getLeft() + diffX,
//                            mDragView.getTop() + diffY,
//                            mDragView.getRight() + diffX,
//                            mDragView.getBottom() + diffY);
//

                    //方式三 layout
                    //使用绝对坐标
//                    mDragView.layout(
//                            mDragView.getLeft() + diffRawX,
//                            mDragView.getTop() + diffRawY,
//                            mDragView.getRight() + diffRawX,
//                            mDragView.getBottom() + diffRawY);

                    //方式4 layoutParams 方式
//                    ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) mDragView.getLayoutParams();
//                    layoutParams.leftMargin = mDragView.getLeft() + diffX;
//                    layoutParams.topMargin = mDragView.getTop() + diffRawY;
//                    mDragView.setLayoutParams(layoutParams);

                    //方式5
                    //scrollBy(-diffX,-diffY);

                    //Scroller 失败
//                    mScroller.startScroll(getScrollX()
//                            , getScrollY()
//                            , -diffX
//                            , -diffY
//                    );
//                    invalidate();

                    mLastPointX = event.getX();
                    mLastPointY = event.getY();

                    mLastRawX = rawX;
                    mLastRawY = rawY;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mCurrentState == State.DRAGGING) {

                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(mDragView.getX(), mInitX);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mDragView.setX((Float) animation.getAnimatedValue());
                        }
                    });
                    ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(mDragView.getY(), mInitY);
                    valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mDragView.setY((Float) animation.getAnimatedValue());
                        }
                    });
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.play(valueAnimator).with(valueAnimator1);
                    animatorSet.start();
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mDragView = null;
                            mCurrentState = State.IDLE;
                        }
                    });
                    animatorSet.start();
                }
                break;
        }
        return true;
    }

    private boolean isClickOnChildView(MotionEvent event) {
        boolean result = false;
        Rect rect = new Rect();
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            rect.set((int) child.getX(), (int) child.getY(), (int) child.getX() + child.getWidth(), (int) child.getY() + child.getHeight());
            if (rect.contains((int) event.getX(), (int) event.getY())) {
                mDragView = child;
                result = true;
                mInitX = mDragView.getX();
                mInitY = mDragView.getY();
                break;
            }
        }
        return result;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(
                    mScroller.getCurrX(),
                    mScroller.getCurrY());
            // 通过重绘来不断调用computeScroll
            invalidate();
        }
    }
}
