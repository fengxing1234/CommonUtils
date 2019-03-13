package com.fengxing.mobile.commonutils;

import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MainBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * 点击屏幕空白处 关闭键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View currentFocus = getCurrentFocus();
            if (isShouldHideKeyBord(currentFocus, ev)) {
                hideSoftInput(currentFocus.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 判断是否隐藏键盘
     * <p>
     * 如是edit控件 点击在view之外 隐藏如键盘
     *
     * @param view
     * @param event
     * @return
     */
    protected boolean isShouldHideKeyBord(View view, MotionEvent event) {
        if (view == null) return false;
        if (view instanceof EditText) {
            int[] l = {0, 0};
            /**
             *
             * 在其窗口中计算此视图的坐标。 参数必须是两个整数的数组。 方法返回后，数组按顺序包含x和y位置。</ p>
             *
             * @param outLocation一个包含两个整数的数组，用于保存坐标
             */

            view.getLocationInWindow(l);

            int left = l[0];
            int top = l[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();

            boolean isClickView = event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom;
            return !isClickView;

        }
        return false;
    }

    /**
     * 隐藏软键盘
     *
     * @param windowToken
     */
    private void hideSoftInput(IBinder windowToken) {
        if (windowToken != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
