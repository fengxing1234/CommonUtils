package com.fengxing.mobile.commonutils;

import android.accounts.AbstractAccountAuthenticator;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fengxing.mobile.commonutils.animation.TestAnimActivity;
import com.fengxing.mobile.commonutils.animation.tween.TestTweenAnimActivity;
import com.fengxing.mobile.commonutils.test.TestDeleteListActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvResult;
    private static final int MODE_MASK = 0x3 << 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_key_board_hide).setOnClickListener(this);
        findViewById(R.id.btn_right_movie).setOnClickListener(this);
        findViewById(R.id.btn_new_page).setOnClickListener(this);
        findViewById(R.id.btn_test_anim).setOnClickListener(this);
        findViewById(R.id.btn_test_tween_anim).setOnClickListener(this);
        findViewById(R.id.btn_test_permission).setOnClickListener(this);



        tvResult = (TextView) findViewById(R.id.tv_result);

        ConstraintLayout rootView = (ConstraintLayout) findViewById(R.id.main_container);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_key_board_hide:
                startActivity(new Intent(this, KeyBoardHideActivity.class));
                break;
            case R.id.btn_right_movie:

                String result = String.valueOf("");
                String s = Integer.toBinaryString(0b101 | 0b01);
                String s1 = Integer.toBinaryString(011);
                String s2 = Integer.toBinaryString(2 << 30);

                int i1 = 0x0011 & ~MODE_MASK;
                Log.d("fengxing", "onClick: " + ~MODE_MASK);
                Log.d("fengxing", "toBinaryString: " + Integer.toBinaryString(~MODE_MASK));
                Log.d("fengxing", "0x11: " + Integer.toBinaryString(0x11));

                String s3 = Integer.toBinaryString(i1);
                tvResult.setText(s);
                break;
            case R.id.btn_new_page:
                startActivity(new Intent(this, TestDeleteListActivity.class));
                break;
            case R.id.btn_test_anim:
                startActivity(new Intent(this, TestAnimActivity.class));
                break;

            case R.id.btn_test_tween_anim:
                startActivity(new Intent(this, TestTweenAnimActivity.class));
                break;

            case R.id.btn_test_permission:
                startActivity(new Intent(this, TextPermissionActivity.class));
                break;
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

}
