package com.fengxing.mobile.commonutils.test;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fengxing.mobile.commonutils.LoginActivity;
import com.fengxing.mobile.commonutils.MainActivity;
import com.fengxing.mobile.commonutils.R;
import com.fengxing.mobile.commonutils.ui.ImageVerifyCodeView;
import com.fengxing.mobile.plugin.common.utils.StatusBarTools;

public class Login2Activity extends AppCompatActivity {

    private EditText et_id;
    private EditText et_pass_word;
    private EditText et_verify_code;
    private ImageVerifyCodeView mImageVerifyCodeView;
    private LinearLayout main_root;
    private TextView tv_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_scroll);
        int color = getResources().getColor(R.color.white);
        StatusBarTools.setColorNoTranslucent(this, color);
        StatusBarTools.autoChangeStatusBarTextColor(getWindow(), color);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
        main_root = (LinearLayout) findViewById(R.id.main_root);
        et_id = (EditText) findViewById(R.id.et_id);
        et_pass_word = (EditText) findViewById(R.id.et_pass_word);
        et_verify_code = (EditText) findViewById(R.id.et_verify_code);
        mImageVerifyCodeView = (ImageVerifyCodeView) findViewById(R.id.verify_code_view);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login2Activity.this, MainActivity.class));
            }
        });
        mImageVerifyCodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageVerifyCodeView.startRefreshCode();
            }
        });
        controlKeyboardLayout(scrollView, tv_login);
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
}
