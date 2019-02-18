package com.fengxing.mobile.commonutils.animation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.fengxing.mobile.commonutils.data.TestValueAnimatorOfObjectPointData;

public class TestValueAnimatorView extends View {


    private static final String TAG = "TestValueAnimatorView";
    private Paint mPaint;
    private TestValueAnimatorOfObjectPointData mCurrentPoint;
    private float RADIUS = 70f;
    private int mBottom;
    private int mRight;

    public TestValueAnimatorView(Context context) {
        this(context, null);
    }

    public TestValueAnimatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestValueAnimatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.mRight = right;
        this.mBottom = bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mCurrentPoint == null) {
            mCurrentPoint = new TestValueAnimatorOfObjectPointData(RADIUS, RADIUS);
            canvas.drawCircle(mCurrentPoint.startX, mCurrentPoint.startY, RADIUS, mPaint);

            TestValueAnimatorOfObjectPointData endPoint = new TestValueAnimatorOfObjectPointData(mRight - RADIUS, mBottom - RADIUS);
            ValueAnimator valueAnimator = ValueAnimator.ofObject(new TestValueAnimatorOfObject(), mCurrentPoint, endPoint);
            valueAnimator.setDuration(5000);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentPoint = (TestValueAnimatorOfObjectPointData) animation.getAnimatedValue();
                    Log.d(TAG, "onAnimationUpdate: " + animation.getAnimatedFraction());
                    invalidate();
                }
            });
            valueAnimator.start();
        } else {
            float startY = mCurrentPoint.startY;
            float startX = mCurrentPoint.startX;
            canvas.drawCircle(startX, startY, RADIUS, mPaint);
        }
    }
}
