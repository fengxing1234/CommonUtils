package com.fengxing.mobile.commonutils.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
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

}
