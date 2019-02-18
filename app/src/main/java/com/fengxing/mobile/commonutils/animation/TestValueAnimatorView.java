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
    private String color;

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

    public void setColor(String color) {
        this.color = color;
        mPaint.setColor(Color.parseColor(color));
        // 将画笔的颜色设置成方法参数传入的颜色
        invalidate();
        // 调用了invalidate()方法,即画笔颜色每次改变都会刷新视图，然后调用onDraw()方法重新绘制圆
        // 而因为每次调用onDraw()方法时画笔的颜色都会改变,所以圆的颜色也会改变
    }

    public String getColor() {
        return color;
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
        canvas.drawCircle(500, 500, RADIUS, mPaint);


//        if (mCurrentPoint == null) {
//            mCurrentPoint = new TestValueAnimatorOfObjectPointData(RADIUS, RADIUS);
//            canvas.drawCircle(mCurrentPoint.startX, mCurrentPoint.startY, RADIUS, mPaint);
//
//            TestValueAnimatorOfObjectPointData endPoint = new TestValueAnimatorOfObjectPointData(mRight - RADIUS, mBottom - RADIUS);
//            ValueAnimator valueAnimator = ValueAnimator.ofObject(new TestValueAnimatorOfObject(), mCurrentPoint, endPoint);
//            valueAnimator.setDuration(5000);
//            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    mCurrentPoint = (TestValueAnimatorOfObjectPointData) animation.getAnimatedValue();
//                    Log.d(TAG, "onAnimationUpdate: " + animation.getAnimatedFraction());
//                    invalidate();
//                }
//            });
//            valueAnimator.start();
//        } else {
//            float startY = mCurrentPoint.startY;
//            float startX = mCurrentPoint.startX;
//            canvas.drawCircle(startX, startY, RADIUS, mPaint);
//        }
    }
}
