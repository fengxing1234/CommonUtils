package com.fengxing.mobile.commonutils.test;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class TesMoveImageView extends ImageView {

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
                mLastX = getX();
                mLastY = getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = getX();
                float y = getY();
                int offsetX = (int) (x - mLastX);
                int offsetY = (int) (y - mLastY);
                layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}
