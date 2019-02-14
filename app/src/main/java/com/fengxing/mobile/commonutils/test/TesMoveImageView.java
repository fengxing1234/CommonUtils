package com.fengxing.mobile.commonutils.test;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;

import static android.content.ContentValues.TAG;

@SuppressLint("AppCompatCustomView")
public class TesMoveImageView extends ImageView {

    public static final String TAG = "TesMoveImageView";

    private float mLastX;
    private float mLastY;

    public TesMoveImageView(Context context) {
        this(context, null);
    }

    public TesMoveImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TesMoveImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                Log.d(TAG, "onTouchEvent:  Down");
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) event.getX();
                int y = (int) event.getY();
                int offsetX = (int) (x - mLastX);
                int offsetY = (int) (y - mLastY);

                Log.d(TAG, "onTouchEvent X : " + x);
                Log.d(TAG, "onTouchEvent Y : " + y);

                Log.d(TAG, "onTouchEvent mLastX : " + mLastX);
                Log.d(TAG, "onTouchEvent mLastY : " + mLastY);

                Log.d(TAG, "onTouchEvent offsetX : " + offsetX);
                Log.d(TAG, "onTouchEvent offsetY : " + offsetY);

                //点击更换背景 回复原来位置并更改背景颜色
                //layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);

                //点击更换背景 直接更改背景颜色 没有改变位置
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                layoutParams.leftMargin = getLeft() + offsetX;
                layoutParams.topMargin = getTop() + offsetY;
                setLayoutParams(layoutParams);

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
