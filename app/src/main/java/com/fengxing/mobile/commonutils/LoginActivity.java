package com.fengxing.mobile.commonutils;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengxing.mobile.commonutils.ui.ImageVerifyCodeView;
import com.fengxing.mobile.plugin.common.utils.StatusBarTools;

public class LoginActivity extends AppCompatActivity {

    private EditText et_id;
    private EditText et_pass_word;
    private EditText et_verify_code;
    private ImageVerifyCodeView mImageVerifyCodeView;
    private LinearLayout main_root;
    private TextView tv_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        int color = getResources().getColor(R.color.white);
        StatusBarTools.setColorNoTranslucent(this, color);
        StatusBarTools.autoChangeStatusBarTextColor(getWindow(), color);
        main_root = (LinearLayout) findViewById(R.id.main_root);
        et_id = (EditText) findViewById(R.id.et_id);
        et_pass_word = (EditText) findViewById(R.id.et_pass_word);
        et_verify_code = (EditText) findViewById(R.id.et_verify_code);
        mImageVerifyCodeView = (ImageVerifyCodeView) findViewById(R.id.verify_code_view);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
        mImageVerifyCodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageVerifyCodeView.startRefreshCode();
            }
        });
        controlKeyboardLayout(main_root, tv_login);
    }

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
                    if (scrollHeight == 0) {
                        return;
                    }
                    root.scrollTo(0, scrollHeight);
                } else {//键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }


    private void getKeyboardHeight(final View root, final View scrollView) {
        //注册布局变化监听
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int screenHeight = root.getRootView().getHeight();
                //判断窗口可见区域大小
                Rect r = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
                int heightDifference = screenHeight - (r.bottom - r.top);

                int rootInvisibleHeight = screenHeight - r.bottom;

                boolean isKeyboardShowing = heightDifference > screenHeight / 3;
                if (isKeyboardShowing) {
                    int[] location = new int[2];
                    scrollView.getLocationInWindow(location);
                    //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                    int scrollHeight = (location[1] + scrollView.getHeight()) - rootInvisibleHeight;
                    root.scrollTo(0, scrollHeight);
                } else {
                    root.scrollTo(0, 0);
                }
                //移除布局变化监听
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }
}
