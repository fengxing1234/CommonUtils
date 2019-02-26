package com.im.picc.plugin.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;

import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.im.picc.plugin.common.R;


/**
 * View.System_ui_flag 参数
 * <p>
 * SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION，表示会让应用的主体内容占用系统导航栏的空间
 * <p>
 * SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN   ,   SYSTEM_UI_FLAG_LAYOUT_STABLE，
 * 注意两个Flag必须要结合在一起使用，表示会让应用的主体内容占用系统状态栏的空间
 */
public class StatusBarTools {

    private static final String TAG_FAKE_STATUS_BAR_VIEW = "tag_fake_status_bar_view";
    private static final String TAG_MARGIN_ADDED = "tag_margin_added";

    /**
     * 全屏模式 隐藏状态栏
     */
    public static void fullScreenMode(Activity activity) {
        fullScreenMode(activity, null);
    }

    /**
     * 全屏模式 隐藏状态栏和ActionBar
     *
     * @param activity
     * @param actionBar
     */
    public static void fullScreenMode(Activity activity, ActionBar actionBar) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    /**
     * 隐藏导航栏
     *
     * @param window
     */
    public static void hideNavigation(Window window) {
        View decorView = window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(option);
    }

    /**
     * android5.0 可以使用
     * android4.4 可以使用
     * <p>
     * 透明状态栏 只有在Android5。0以上有效
     * 注意两个Flag必须要结合在一起使用，表示会让应用的主体内容占用系统状态栏的空间，
     * 最后再调用Window的setStatusBarColor()方法将状态栏设置成透明色就可以了。
     */
    public static void statusBarTransparent(Window window) {
        if (BuildVersionTool.dd21LOLLIPOP5_0()) {
            View decorView = window.getDecorView();
            int flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(flag);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (BuildVersionTool.dd19KITKAT4_4()) {//4.4-5.0
            WindowManager.LayoutParams params = window.getAttributes();
            params.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | params.flags;
        }
    }


    /**
     * 修改状态栏为全透明  android 版本 4.4可用
     *
     * @param activity
     */
    @TargetApi(19)
    public static void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 状态栏和底部导航栏 透明 5.0以上可用
     * <p>
     *
     * @param window
     */
    public static void transparentStatusBarAndNavigation(Window window) {
        if (BuildVersionTool.dd21LOLLIPOP5_0()) {
            View decorView = window.getDecorView();
            int flag =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(flag);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }

    }


    /**
     * 沉浸式模式
     * Android 4.4 才支持
     * <p>
     * 实现 onWindowFocusChanged 方法
     */
    public static void immersiveMode(Window window) {
        immersiveMode(true, window);
    }

    /**
     * @param hasFocus 是否获取到焦点
     * @param window
     */
    public static void immersiveMode(boolean hasFocus, Window window) {
        if (hasFocus && BuildVersionTool.dd19KITKAT4_4()) {
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }


    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight2(Context context) {
        /**
         * 获取状态栏高度——方法2
         * */
        int statusBarHeight2 = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusBarHeight2 = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight2;
    }


    /**
     * 设置状态栏背景颜色 4.4以上可以使用
     *
     * @param window
     * @param color
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setStatusBarColor(Window window, @ColorInt int color) {
        if (BuildVersionTool.dd21LOLLIPOP5_0()) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup systemContent = window.findViewById(android.R.id.content);
            View view = new View(window.getContext());
            int statusBarHeight = StatusBarTools.getStatusBarHeight(window.getContext());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
            view.setBackgroundColor(color);
            systemContent.getChildAt(0).setFitsSystemWindows(true);
            systemContent.addView(view, 0, lp);
        }
    }

    /**
     * //4.4-5.0还没有API可以直接修改状态栏颜色，所以必须先将状态栏设置为透明，
     * 然后在布局中添加一个背景为期望色值的View来作为状态栏的填充。
     *
     * @param window
     * @param color
     */
    private static void setStatusBarColor2(Window window, @ColorInt int color) {

        //设置Window为全透明
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //跟布局
        ViewGroup contentView = window.findViewById(Window.ID_ANDROID_CONTENT);
        //获取父布局
        View contentChild = contentView.getChildAt(0);

        //获取状态栏高度
        int statusBarHeight = StatusBarTools.getStatusBarHeight(window.getContext());

        //如果已经存在假状态栏则移除，防止重复添加
        removeFakeStatusBarViewIfExist(window);

        //添加一个View来作为状态栏的填充
        addFakeStatusBarView(window, color, statusBarHeight);

        //设置子控件到状态栏的间距
        addMarginTopToContentChild(contentChild, statusBarHeight);

        //不预留系统栏位置
        contentChild.setFitsSystemWindows(false);

        //如果在Activity中使用了ActionBar则需要再将布局与状态栏的高度跳高一个ActionBar的高度，否则内容会被ActionBar遮挡
//            int action_bar_id = window.getContext().getResources().getIdentifier("action_bar", "id", window.getContext().getPackageName());
//            View view = activity.findViewById(action_bar_id);
//            if (view != null) {
//                TypedValue typedValue = new TypedValue();
//                if (activity.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
//                    int actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, activity.getResources().getDisplayMetrics());
//                    setContentTopPadding(activity, actionBarHeight);
//                }
//            }
    }

    static void setContentTopPadding(Activity activity, int padding) {
        ViewGroup mContentView = (ViewGroup) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mContentView.setPadding(0, padding, 0, 0);
    }

    private static void addMarginTopToContentChild(View contentChild, int statusBarHeight) {
        if (contentChild == null) {
            return;
        }
        if (!TAG_MARGIN_ADDED.equals(contentChild.getTag())) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) contentChild.getLayoutParams();
            lp.topMargin += statusBarHeight;
            contentChild.setLayoutParams(lp);
            contentChild.setTag(TAG_MARGIN_ADDED);
        }
    }

    private static void addFakeStatusBarView(Window window, int color, int statusBarHeight) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View statusBarView = new View(window.getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        layoutParams.gravity = Gravity.TOP;
        statusBarView.setBackgroundColor(color);
        statusBarView.setLayoutParams(layoutParams);
        statusBarView.setTag(TAG_FAKE_STATUS_BAR_VIEW);
        decorView.addView(statusBarView);
    }

    private static void removeFakeStatusBarViewIfExist(Window window) {
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View fakeView = decorView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
        if (fakeView != null) {
            decorView.removeView(fakeView);
        }
    }


    /**
     * 根据颜色的亮暗 自动转化状态栏字体颜色
     *
     * @param window
     * @param color
     */
    public static void autoChangeStatusBarTextColor(Window window, int color) {
        if (!ColorTools.isColorDark(color)) {
            statusBarTextColorBlack(window);
        } else {
            statusBarTextColorWhite(window);
        }
    }


    /**
     * 改变状态栏文字的颜色
     * 设置白底黑字
     *
     * @param isDarkMode
     */
    public static boolean changeStatusBarTextColor(Window window, boolean isDarkMode) {
        if (BuildVersionTool.dd23M6_0()) {
            if (isDarkMode) {//黑色字体
                //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                statusBarTextColorBlack(window);
            } else {//白色字体
                //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                statusBarTextColorWhite(window);
            }
        }
        return isDarkMode;
    }

    /**
     * 设置状态栏字体为白色 6.0
     *
     * @param window
     */
    public static void statusBarTextColorWhite(Window window) {
        if (BuildVersionTool.dd23M6_0()) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    /**
     * 设置状态栏字体为黑色 6.0
     *
     * @param window
     */
    public static void statusBarTextColorBlack(Window window) {
        if (BuildVersionTool.dd23M6_0()) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
