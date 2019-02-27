package com.fengxing.mobile.commonutils.utils;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.Scroller;

/**
 * 相当于父控件
 * getTop()
 * getLeft()
 * getRight()
 * getBottom()
 * <p>
 * getX() 相对于父坐标
 * getRawX() 相对于屏幕坐标
 * <p>
 * https://juejin.im/entry/576a264f80dda4005fb17639
 * getScrollX()
 * getScrollX()、getScrollY()得到的是偏移量，是相对自己初始位置的滑动偏移距离，
 * 只有当有scroll事件发生时，这两个方法才能有值，否则getScrollX()、getScrollY()都是初始时的值0，而不管你这个滑动控件在哪里。
 * 所谓自己初始位置是指，控件在刚开始显示时、没有滑动前的位置
 * <p>
 * https://blog.csdn.net/bigconvience/article/details/26697645
 * scrollTo(x,y)让View相对于 初始 的位置滚动某段距离 多次调用值替换
 * scrollBy(x,y)让View相对于 当前 的位置滚动某段距离 多次调用值叠加
 */
public class ViewUtils {

    /**
     * TouchSlop是一个常量，保存的是系统所能识别出的最小滑动距离。
     * 这个值在不同的设备上有可能是不同的，我们通常使用它来进行一些滑动过滤，
     * 比如说判断当前滑动的距离如果少于这个值，那么就可以判断不是滑动，不进行滑动后的处理，增强用户体验。
     *
     * @param context
     * @return
     */
    public static int getTouchSlop(Context context) {
        return ViewConfiguration.get(context).getScaledTouchSlop();
    }


    /**
     * VelocityTracker，速度追踪，通过这个类我们可以获取手指在滑动时的速度，其中包括水平和垂直方向的速度。
     * <p>
     * 需要注意的是，在获取之前，必须先要调用computeCurrentVelocity(int units)方法，其中units参数代表的是毫秒时间段。
     * 比如说上方写的1000就代表1秒内手指滑动的像素数，如果1秒内滑动了100像素数，那么值就为100，当然这个值也有可能是负数，
     * 如果是在水平方向从右往左滑动，值则会负数，垂直时，从下往上滑动，也为负数。总结下来可以用一个公式来表示：
     * <p>
     * 速度 = （终点位置 - 起点位置） / 时间段
     * <p>
     * 当我们不需要使用VelocityTracker的时候，可以通过如下方法重置并回收内存
     *
     * @param event
     * @return 数组 第一个是x速度 第二个是y速度
     */
    public static float[] getVelocityTracker(MotionEvent event) {
        VelocityTracker velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(event);

        velocityTracker.computeCurrentVelocity(1000);

        float xVelocity = velocityTracker.getXVelocity();
        float yVelocity = velocityTracker.getYVelocity();

        velocityTracker.clear();
        velocityTracker.recycle();
        return new float[]{xVelocity, yVelocity};
    }


    /**
     * Scroller是一个弹性滑动类，用于实现View的弹性滑动。
     * Scroller的好处就在于滑动时可以让View平滑的滑动，而不是像scrollTo/scrollBy一样生硬的一下子滑动完成。
     *
     * @param context
     * @param x
     * @param y
     * @param dx
     * @param dy
     * @param duration
     */
    public static void scroller(Context context, int x, int y, int dx, int dy, int duration) {
        //创建Scroller对象
        Scroller scroller = new Scroller(context);
        //调用startScroll()方法滑动到指定位置。

        //第一个参数是滚动开始时X的坐标，第二个参数是滚动开始时Y的坐标，
        //第三个参数是横向滚动的距离，正值表示向左滚动，第四个参数是纵向滚动的距离，正值表示向上滚动 第五个参数 时长
        scroller.startScroll(x, y, dx, dy, duration);
        //3,重写computeScroll()方法，并在其内部完成平滑移动逻辑。
        /**
         *
         if (mScroller.computeScrollOffset()) {
         scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
         invalidate();
         }
         */
    }
}
