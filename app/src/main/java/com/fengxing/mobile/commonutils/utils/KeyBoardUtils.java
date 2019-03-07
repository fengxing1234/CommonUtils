package com.fengxing.mobile.commonutils.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

public class KeyBoardUtils {

    /**
     * 关闭键盘
     *
     * @param view
     */
    public static void hideSoftInput(View view) {
        if (view != null) {
            Context context = view.getContext();
            IBinder windowToken = view.getWindowToken();
            InputMethodManager im = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 关闭键盘
     *
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        if (activity != null) {
            IBinder windowToken = activity.getWindow().getDecorView().getWindowToken();
            InputMethodManager im = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 打开软键盘
     *
     * @param context
     */
    public static void openSoftInput(Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    /**
     * 打开关闭相互切换
     *
     * @param activity
     */
    public static void switchKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 判断键盘 是显示 还是隐藏
     * <p>
     * true 显示
     * false 隐藏
     *
     * @param context
     * @return
     */
    public static boolean getKeyBoardState(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();//isOpen若返回true，则表示输入法打开
    }


    /**
     * 隐藏键盘
     *
     * @return
     */
    boolean hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = activity.getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                boolean result = false;
                try {
                    result = inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
        }

        return false;
    }

    /**
     * 解决EditText登录界面遮挡布局问题
     *
     * @param root
     * @param scrollView
     */
    private void controlKeyboardLayout(final View root, final View scrollView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取根布局显示区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                if (rootInvisibleHeight > 300) {// 键盘显示
                    int[] location = new int[2];
                    scrollView.getLocationInWindow(location);
                    int scrollHeight = (location[1] + scrollView.getHeight()) - rootInvisibleHeight;
                    root.scrollTo(0, scrollHeight);
                } else {//键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }

}
