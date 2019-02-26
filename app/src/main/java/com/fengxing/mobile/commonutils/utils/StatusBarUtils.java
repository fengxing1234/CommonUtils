package com.fengxing.mobile.commonutils.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Android 从 4.4 (SDK 19) 开始支持系统栏（状态栏+导航栏）半透明效果：
 * <p>
 * 通过使用  Theme.Holo.NoActionBar.TranslucentDecor 或 Theme.Holo.Light.NoActionBar.TranslucentDecor 主题，
 * 可以实现系统栏半透明的效果。但是，这样一来系统栏会覆盖在布局上（实际上是内容延伸到了系统栏原本所占的区域下面），
 * 为了使布局不被系统栏覆盖，需要设置 fitsSystemWindows 属性为 true。
 * <p>
 * <p>
 * <p>
 * 1.SDK小于19是不支持状态栏透明的，SD21及以上的实现方式也有所不同。
 * 2.SD23及以上支持状态栏颜色反转
 * 3.SD24及以上支持窗口模式，这里要进行判断，当窗口模式时，不要设置状态栏透明
 * 4.状态栏设置透明之后，输入法的adjustResize会失效。网传解决方案android:fitsSystemWindows="true"不推荐使用，
 * 因为这会导致无法在状态栏之下进行绘制。因此这里对DecorView布局变化进行监听，布局变化时动态调整子View的高度为DecorView的可见部分。
 */
public class StatusBarUtils {

    private static final String TAG = StatusBarUtils.class.getSimpleName();

    /**
     * 实现沉浸式体验
     *
     * @param darkStatusBar
     */
    public static boolean setStatusBarTransparent(final Activity activity, boolean darkStatusBar) {
        //SDK大于等于24，需要判断是否为窗口模式
        boolean isInMultiMode = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && activity.isInMultiWindowMode();

        //窗口模式或者SDK小于19，不设置状态栏透明
        if (isInMultiMode || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //SDK小于21
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            //SDK大于等于21

            int systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

            //SDK大于等于23支持翻转状态栏颜色
            if (darkStatusBar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //设置状态栏文字&图标暗色
                systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            //去除状态栏背景
            activity.getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
            //设置状态栏透明
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);//指示此窗口负责绘制系统栏背景的标志。 如果设置，系统条将以透明背景绘制，此窗口中的相应区域将填充指定的颜色指示此窗口负责绘制系统栏背景的标志。 如果设置，系统条将以透明背景绘制，此窗口中的相应区域将填充指定的颜色
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //监听DecorView的布局变化
        activity.getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.d(TAG, "onLayoutChange: left = " + left);
                Log.d(TAG, "onLayoutChange: top =" + top);
                Log.d(TAG, "onLayoutChange: right=" + right);
                Log.d(TAG, "onLayoutChange: bottom=" + bottom);

                Log.d(TAG, "onLayoutChange: oldLeft=" + oldLeft);
                Log.d(TAG, "onLayoutChange: oldTop=" + oldTop);
                Log.d(TAG, "onLayoutChange: oldRight=" + oldRight);
                Log.d(TAG, "onLayoutChange: oldBottom=" + oldBottom);

                ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
                //获取DecorView的可见区域
                Rect visibleDisplayRect = new Rect();
                decorView.getWindowVisibleDisplayFrame(visibleDisplayRect);
                /**这里省略一小段代码，后文提及*/
                //状态栏透明情况下，输入法的adjustResize不会生效，这里手动调整View的高度以适配
                if (isStatusBarTransparent()) {
                    for (int i = 0; i < decorView.getChildCount(); i++) {
                        View child = decorView.getChildAt(i);
                        if (child instanceof ViewGroup) {
                            //获取DecorView的子ViewGroup
                            ViewGroup.LayoutParams childLp = child.getLayoutParams();
                            //调整子ViewGroup的paddingBottom
                            int paddingBottom = bottom - visibleDisplayRect.bottom;
                            if (childLp instanceof ViewGroup.MarginLayoutParams) {
                                //此处减去bottomMargin，是考虑到导航栏的高度
                                paddingBottom -= ((ViewGroup.MarginLayoutParams) childLp).bottomMargin;
                            }
                            paddingBottom = Math.max(0, paddingBottom);
                            if (paddingBottom != child.getPaddingBottom()) {
                                //调整子ViewGroup的paddingBottom，以保证整个ViewGroup可见
                                child.setPadding(child.getPaddingLeft(), child.getPaddingTop(), child.getPaddingRight(), paddingBottom);
                            }
                            break;
                        }
                    }
                }
            }
        });
        return true;
    }


//    private class PrivateListener implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener, View.OnLayoutChangeListener {
//
//
//        private ViewGroup mDecorView;
//
//        public PrivateListener(Activity activity) {
//            mDecorView = (ViewGroup) activity.getWindow().getDecorView();
//        }
//
//        @Override
//        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//            //获取DecorView的可见区域
//            Rect visibleDisplayRect = new Rect();
//            mDecorView.getWindowVisibleDisplayFrame(visibleDisplayRect);
//            //调整mWindowBackGroundView的Y轴偏移量，用于覆盖不可见区域出现的黑色（不可见区域常见为当输入法及导航栏变化时的背景）
//            mWindowBackGroundView = getWindowBackGroundView(mDecorView);
//            if (mWindowBackGroundView != null) {
//                mWindowBackGroundView.setTranslationY(visibleDisplayRect.bottom);
//            }
//            //状态栏透明情况下，输入法的adjustResize不会生效，这里手动调整View的高度以适配
//            if (isStatusBarTransparent()) {
//                for (int i = 0; i < mDecorView.getChildCount(); i++) {
//                    View child = mDecorView.getChildAt(i);
//                    if (child instanceof ViewGroup) {
//                        //获取DecorView的子ViewGroup
//                        ViewGroup.LayoutParams childLp = child.getLayoutParams();
//                        //调整子ViewGroup的paddingBottom
//                        int paddingBottom = bottom - visibleDisplayRect.bottom;
//                        if (childLp instanceof ViewGroup.MarginLayoutParams) {
//                            //此处减去bottomMargin，是考虑到导航栏的高度
//                            paddingBottom -= ((ViewGroup.MarginLayoutParams) childLp).bottomMargin;
//                        }
//                        if (paddingBottom < 0) {
//                            paddingBottom = 0;
//                        }
//                        if (paddingBottom != child.getPaddingBottom()) {
//                            //调整子ViewGroup的paddingBottom，以保证整个ViewGroup可见
//                            child.setPadding(child.getPaddingLeft(), child.getPaddingTop(), child.getPaddingRight(), paddingBottom);
//                        }
//                        break;
//                    }
//                }
//            }
//        }
//
//        @Override
//        public void onAnimationUpdate(ValueAnimator animation) {
//            float translation = (Float) animation.getAnimatedValue();
//            swipeBackEvent((int) translation);
//        }
//
//        @Override
//        public void onAnimationEnd(Animator animation) {
//            if (!isAnimationCancel) {
//                //最终移动距离位置超过半宽，结束当前Activity
//                if (mShadowView.getWidth() + 2 * mShadowView.getTranslationX() >= 0) {
//                    mShadowView.setVisibility(View.GONE);
//                    mSwipeBackActivity.finish();
//                    mSwipeBackActivity.overridePendingTransition(-1, -1);//取消返回动画
//                } else {
//                    mShadowView.setTranslationX(-mShadowView.getWidth());
//                    mSwipeBackView.setTranslationX(0);
//                    convertFromTranslucent(mSwipeBackActivity);
//                }
//            }
//        }
//
//        @Override
//        public void onAnimationStart(Animator animation) {
//            isAnimationCancel = false;
//        }
//
//        @Override
//        public void onAnimationCancel(Animator animation) {
//            isAnimationCancel = true;
//        }
//
//        @Override
//        public void onAnimationRepeat(Animator animation) {
//        }
//
//    }

    /**
     * 状态栏是否透明
     */
    public static boolean isStatusBarTransparent() {
        return false;
    }
}
