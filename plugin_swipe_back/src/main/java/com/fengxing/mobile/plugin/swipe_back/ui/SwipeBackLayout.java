package com.fengxing.mobile.plugin.swipe_back.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.Spannable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fengxing.mobile.plugin.swipe_back.R;
import com.fengxing.mobile.plugin.swipe_back.ViewDragHelper;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;

/**
 * 侧滑退出布局
 */
public class SwipeBackLayout extends FrameLayout {

    private static final String TAG = SwipeBackLayout.class.getSimpleName();
    private List<SwipeListener> mListeners;
    private float mScrimOpacity;

    public interface SwipeListener {

        public void onScrollStateChange(int state, float scrollPercent);

        public void onEdgeTouch(int edge);

        public void onScrollOverThreshold();

        public void onContentViewSwipedBack();
    }


    public void addSwipeListener(SwipeListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        mListeners.add(listener);
    }

    public void removeSwipeListener(SwipeListener listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
    }


    public static final int EDGE_LEFT = ViewDragHelper.EDGE_LEFT;
    public static final int EDGE_RIGHT = ViewDragHelper.EDGE_RIGHT;
    public static final int EDGE_BOTTOM = ViewDragHelper.EDGE_BOTTOM;

    public static int EDGE_ALL = EDGE_BOTTOM | EDGE_RIGHT | EDGE_LEFT;

    private static final int[] EDGE_FLAGS = new int[]{EDGE_LEFT, EDGE_RIGHT, EDGE_BOTTOM, EDGE_ALL};

    //能被检测出来的最小速度
    private static final int MIN_FLING_VELOCITY = 400; //dp 每秒

    private static final float DEFAULT_SCROLL_THRESHOLD = 0.3f;

    //OVERSCROLL_DISTANCE
    //over scroll distance 超过滚动的距离
    private static final int OVER_SCROLL_DISTANCE = 10;

    private static final int FULL_ALPHA = 255;

    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;


    //正在拖拽的边界
    private int mTrackingEdge;
    //允许拖拽的边界
    private int mEdgeFlag;
    //边界阴影图片
    private Drawable mShadowLeft;
    private Drawable mShadowRight;
    private Drawable mShadowBottom;

    private Activity mActivity;
    //当前布局
    private ViewGroup mContentView;
    //滑动的百分比
    private float mScrollPercent;
    //是否开启滑动退出功能
    private boolean mEnable = true;
    //拖拽滑动帮助类
    private ViewDragHelper mDragHelper;

    private int mContentTop;
    private int mContentLeft;
    private float mScrollThreshold = DEFAULT_SCROLL_THRESHOLD;
    private Rect mTmpRect = new Rect();
    private int mScrimColor = DEFAULT_SCRIM_COLOR;
    private boolean mInLayout;


    public SwipeBackLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.SwipeBackLayoutStyle);
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        init(context, attrs, defStyleAttr);

    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mDragHelper = ViewDragHelper.create(this, new ViewDrawCallBack());
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeBackLayout, defStyleAttr, R.style.SwipeBackLayout);
        int edgeSize = typedArray.getDimensionPixelSize(R.styleable.SwipeBackLayout_edge_size, -1);

        if (edgeSize > 0)
            setEdgeSize(edgeSize);

        int flag = typedArray.getInt(R.styleable.SwipeBackLayout_edge_flag, 0);
        int mode = EDGE_FLAGS[flag];
        setEdgeTrackingEnable(mode);
        int shadowLeft = typedArray.getResourceId(R.styleable.SwipeBackLayout_shadow_left, R.drawable.shadow_left);
        int shadowRight = typedArray.getResourceId(R.styleable.SwipeBackLayout_shadow_right, R.drawable.shadow_right);
        int shadowBottom = typedArray.getResourceId(R.styleable.SwipeBackLayout_shadow_bottom, R.drawable.shadow_bottom);
        setShadow(shadowLeft, EDGE_LEFT);
        setShadow(shadowRight, EDGE_RIGHT);
        setShadow(shadowBottom, EDGE_BOTTOM);
        typedArray.recycle();

        float density = getResources().getDisplayMetrics().density;
        float minVel = MIN_FLING_VELOCITY * density;
        mDragHelper.setMinVelocity(minVel);
        mDragHelper.setMaxVelocity(minVel * 2.0f);
    }

    private void setShadow(@DrawableRes int resId, int edge) {
        setShadow(getResources().getDrawable(resId), edge);
    }

    // TODO: 2019/3/12 为什么要用 invalidate ?

    /**
     * 设置边界阴影图片
     *
     * @param drawable
     * @param edge
     */
    private void setShadow(Drawable drawable, int edge) {
        if ((edge & EDGE_LEFT) != 0) {
            mShadowLeft = drawable;
        } else if ((edge & EDGE_RIGHT) != 0) {
            mShadowRight = drawable;
        } else if ((edge & EDGE_BOTTOM) != 0) {
            mShadowBottom = drawable;
        }
        invalidate();
    }

    /**
     * 为父视图的选定边启用边缘跟踪
     * 如果边界被触摸了 回调 onEdgeTouched 和 onEdgeDragStarted 方法
     *
     * @param mode
     */
    public void setEdgeTrackingEnable(int mode) {
        mEdgeFlag = mode;
        mDragHelper.setEdgeTrackingEnabled(mode);
    }

    /**
     * 设置边缘的大小。 这是沿着此视图边缘的像素范围，如果启用了边缘跟踪，它将主动检测边缘触摸或拖动。
     *
     * @param edgeSize
     */
    private void setEdgeSize(int edgeSize) {
        mDragHelper.setEdgeSize(edgeSize);
    }

    /**
     * 侧滑返回是否可用
     *
     * @param enable
     */
    public void setSwipeBackEnable(boolean enable) {
        mEnable = enable;
    }

    /**
     * 关联Activity
     *
     * @param
     */
    public void attachToActivity(Activity activity) {
        mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowBackground});
        int background = a.getResourceId(0, 0);
        a.recycle();
        ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
        ViewGroup contentView = (ViewGroup) decorView.getChildAt(0);
        contentView.setBackgroundResource(background);
        decorView.removeView(contentView);
        addView(contentView);
        setContentView(contentView);
        if (activity instanceof SwipeListener) {
            addSwipeListener((SwipeListener) activity);
        }

        decorView.addView(this);
    }

    private void setContentView(ViewGroup view) {
        mContentView = view;
    }

    /**
     * 滑动关闭Activity
     * 自动关闭的activity 计算处滑动的距离
     */
    public void scrollToFinishActivity() {
        int width = mContentView.getWidth();
        int height = mContentView.getHeight();
        int left = 0, top = 0;
        if ((mEdgeFlag & EDGE_LEFT) != 0) {//当前是左边缘关闭页面 contentView 从0--width关闭
            left = width + mShadowLeft.getIntrinsicWidth() + OVER_SCROLL_DISTANCE;
            //自定关闭activity 也需要设定 因为在下面需要 已经滑动的百分比
            mTrackingEdge = EDGE_LEFT;
        } else if ((mEdgeFlag & EDGE_RIGHT) != 0) {//右边缘 从width 滑到 0 退出页面
            left = -width - mShadowRight.getIntrinsicWidth() - OVER_SCROLL_DISTANCE;
            mTrackingEdge = EDGE_RIGHT;
        } else if ((mEdgeFlag & EDGE_BOTTOM) != 0) {//从底部滑到上面
            top = -height - mShadowBottom.getIntrinsicHeight() - OVER_SCROLL_DISTANCE;
            mTrackingEdge = EDGE_BOTTOM;
        }
        mDragHelper.smoothSlideViewTo(mContentView, left, top);
        invalidate();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mInLayout = false;
        if (mContentView != null) {
            mContentView.layout(
                    mContentLeft,
                    mContentTop,
                    mContentLeft + mContentView.getMeasuredWidth(),
                    mContentTop + mContentView.getMeasuredHeight());
        }
        mInLayout = true;
    }

    @Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }

    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean drawContent = child == mContentView;
        boolean ret = super.drawChild(canvas, child, drawingTime);
        if (mScrimOpacity > 0 && drawContent && mDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            drawShadow(canvas, child);
            drawScrim(canvas, child);
        }
        return ret;
    }

    private void drawScrim(Canvas canvas, View child) {
        final int baseAlpha = (mScrimColor & 0xff000000) >>> 24;
        final int alpha = (int) (baseAlpha * mScrimOpacity);
        final int color = alpha << 24 | (mScrimColor & 0xffffff);

        if ((mTrackingEdge & EDGE_LEFT) != 0) {
            canvas.clipRect(0, 0, child.getLeft(), getHeight());
        } else if ((mTrackingEdge & EDGE_RIGHT) != 0) {
            canvas.clipRect(child.getRight(), 0, getRight(), getHeight());
        } else if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
            canvas.clipRect(child.getLeft(), child.getBottom(), getRight(), getHeight());
        }
        canvas.drawColor(color);
    }

    private void drawShadow(Canvas canvas, View child) {
        Rect childRect = mTmpRect;
        child.getHitRect(childRect);
        if ((mEdgeFlag & EDGE_LEFT) != 0) {
            mShadowLeft.setBounds(childRect.left - mShadowLeft.getIntrinsicWidth(), childRect.top, childRect.left, childRect.bottom);
            mShadowLeft.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
            mShadowLeft.draw(canvas);
        }

        if ((mEdgeFlag & EDGE_RIGHT) != 0) {
            mShadowRight.setBounds(childRect.right, childRect.top,
                    childRect.right + mShadowRight.getIntrinsicWidth(), childRect.bottom);
            mShadowRight.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
            mShadowRight.draw(canvas);
        }

        if ((mEdgeFlag & EDGE_BOTTOM) != 0) {
            mShadowBottom.setBounds(childRect.left, childRect.bottom, childRect.right,
                    childRect.bottom + mShadowBottom.getIntrinsicHeight());
            mShadowBottom.setAlpha((int) (mScrimOpacity * FULL_ALPHA));
            mShadowBottom.draw(canvas);
        }
    }

    @Override
    public void computeScroll() {
        mScrimOpacity = 1 - mScrollPercent;
        if (mEnable && mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);//区别 代动画？？？
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mEnable) {
            return false;
        }
        try {
            //由ViewDrawHelp 判断是否可以 拦截触摸事件 来自己处理
            return mDragHelper.shouldInterceptTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnable) {
            return false;
        }
        //把触摸时间交给ViewDrawHelp处理
        mDragHelper.processTouchEvent(event);
        return true;
    }

    private class ViewDrawCallBack extends ViewDragHelper.Callback {
        //主要作用 就是让 监听onScrollOverThreshold  只执行一次 就是每次滑动到百分之三十的时候 到都执行一次onScrollOverThreshold 30-100就不在重复执行
        private boolean mIsScrollOverValid;

        //判断view是否可以拖拽
        //返回ture可以拖拽
        //返回false不可以拖拽
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //校验指定边缘是否可以触摸，并设置当前正在触摸的边缘,然后回调
            boolean edgeTouched = mDragHelper.isEdgeTouched(mEdgeFlag, pointerId);//指定view是否触碰到了边界
            if (edgeTouched) {
                if (mDragHelper.isEdgeTouched(EDGE_LEFT, pointerId)) {
                    mTrackingEdge = EDGE_LEFT;
                } else if (mDragHelper.isEdgeTouched(EDGE_RIGHT, pointerId)) {
                    mTrackingEdge = EDGE_RIGHT;
                } else if (mDragHelper.isEdgeTouched(EDGE_BOTTOM, pointerId)) {
                    mTrackingEdge = EDGE_BOTTOM;
                }

                if (mListeners != null && !mListeners.isEmpty()) {
                    for (SwipeListener listener : mListeners) {
                        listener.onEdgeTouch(mTrackingEdge);
                    }
                    mIsScrollOverValid = true;
                }
            }

            boolean directionCheck = false;
            if (mEdgeFlag == EDGE_LEFT || mEdgeFlag == EDGE_RIGHT) {
                ////检查手指移动的距离有没有超过触发处理移动事件的最短距离mTouchSlop
                directionCheck = !mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL, pointerId);
            } else if (mEdgeFlag == EDGE_BOTTOM) {
                directionCheck = !mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_HORIZONTAL, pointerId);
            } else if (mEdgeFlag == EDGE_ALL) {
                directionCheck = true;
            }
            return edgeTouched && directionCheck;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int ret = 0;
            if ((mTrackingEdge & EDGE_LEFT) != 0) {
                ret = Math.min(child.getWidth(), Math.max(0, left));
            } else if ((mTrackingEdge & EDGE_RIGHT) != 0) {
                ret = Math.min(0, Math.max(left, -child.getWidth()));
            }
            Log.d(TAG, "clampViewPositionHorizontal: left = " + left + " ret :" + ret);
            return ret;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int ret = 0;
            if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
                ret = Math.min(0, Math.max(top, -child.getHeight()));
            }
            Log.d(TAG, "clampViewPositionVertical:  ret = " + ret);
            return ret;
        }

        /**
         * 返回给定子View在相应方向上可以被拖动的最远距离，默认为0，一般是可被挪动View时指定为指定View的大小等
         *
         * @param child Child view to check
         * @return
         */
        @Override
        public int getViewVerticalDragRange(View child) {
            int flag = mEdgeFlag & EDGE_BOTTOM;
            Log.d(TAG, "getViewVerticalDragRange: " + flag);
            return flag;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            int i = mEdgeFlag & (EDGE_LEFT | EDGE_RIGHT);
            Log.d(TAG, "getViewHorizontalDragRange: " + i);
            return i;
        }

        /**
         * 当位置有变化时回调Callback的onViewPositionChanged方法实时通知
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
            if ((mTrackingEdge & EDGE_LEFT) != 0) {//当前拖拽的是左边缘
                //不加float mScrollPercent 的结果为0 因为需要的是 0.x 百分比形式
                mScrollPercent = Math.abs((float) left / (mContentView.getWidth() + mShadowLeft.getIntrinsicWidth()));
            } else if ((mTrackingEdge & EDGE_RIGHT) != 0) {
                mScrollPercent = Math.abs((float) left / (mContentView.getWidth() + mShadowRight.getIntrinsicWidth()));
            } else if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
                mScrollPercent = Math.abs(top / mContentView.getHeight() + mShadowBottom.getIntrinsicHeight());
            }
            Log.d(TAG, "mScrollPercent: " + mScrollPercent);
            mContentLeft = left;
            mContentTop = top;

            invalidate();

            if (mScrollPercent > mScrollThreshold && !mIsScrollOverValid) {
                mIsScrollOverValid = true;
            }

            //状态，百分比 回调
            if (mListeners != null && !mListeners.isEmpty()) {
                for (SwipeListener listener : mListeners) {
                    listener.onScrollStateChange(mDragHelper.getViewDragState(), mScrollPercent);
                }
            }

            //监听布局滑动到百分之三十
            if (mListeners != null//不为空
                    && !mListeners.isEmpty()//有数据
                    && mDragHelper.getViewDragState() == ViewDragHelper.STATE_DRAGGING//正在拖拽
                    && mScrollPercent > mScrollThreshold//百分比>0.3f
                    && mIsScrollOverValid//可用
                    ) {
                mIsScrollOverValid = false;
                for (SwipeListener listener : mListeners) {
                    listener.onScrollOverThreshold();
                }
            }

            //滑动完成对调
            if (mScrollPercent >= 1) {
                if (mListeners != null && !mListeners.isEmpty()) {
                    for (SwipeListener listener : mListeners) {
                        listener.onContentViewSwipedBack();
                    }
                }
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int width = releasedChild.getWidth();
            int height = releasedChild.getHeight();
            int left = 0, top = 0;
            if ((mTrackingEdge & EDGE_LEFT) != 0) {
                left = ((xvel > 0 || xvel == 0) && mScrollPercent > mScrollThreshold) ?
                        width + mShadowLeft.getIntrinsicWidth() + OVER_SCROLL_DISTANCE : 0;

            } else if ((mTrackingEdge & EDGE_RIGHT) != 0) {
                left = ((xvel < 0 || xvel == 0) && mScrollPercent > mScrollThreshold) ?
                        -(width + mShadowLeft.getIntrinsicWidth() + OVER_SCROLL_DISTANCE) : 0;

            } else if ((mTrackingEdge & EDGE_BOTTOM) != 0) {
                top = yvel < 0 || yvel == 0 && mScrollPercent > mScrollThreshold ? -(height
                        + mShadowBottom.getIntrinsicHeight() + OVER_SCROLL_DISTANCE) : 0;
            }
            mDragHelper.settleCapturedViewAt(left, top);
            invalidate();
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (mListeners != null && !mListeners.isEmpty()) {
                for (SwipeListener listener : mListeners) {
                    listener.onScrollStateChange(state, mScrollPercent);
                }
            }
        }
    }

}
