package com.fengxing.mobile.commonutils.test;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.fengxing.mobile.commonutils.R;
import com.fengxing.mobile.commonutils.utils.ViewUtils;

public class TestRotationRocket extends View {

    private static final String TAG = "TestRotationRocket";

    public static final int DEF_IMG_SAMPLE_SIZE = 30;
    public static final int DEF_DURATION = 500;
    private int mDuration; //火箭动画的时间
    private int mImgSampleSize;//火箭缩放比例
    private Bitmap mBitmap;
    private int mBitmapHeight;
    private int mBitmapWidth;
    private Paint mPaint;
    private Scroller mScroller;

    public TestRotationRocket(Context context) {
        this(context, null);
    }

    public TestRotationRocket(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestRotationRocket(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TestRotationRocket);


        int indexCount = typedArray.getIndexCount();

        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.TestRotationRocket_duration:
                    mImgSampleSize = typedArray.getInteger(R.styleable.TestRotationRocket_imgSampleSize, DEF_IMG_SAMPLE_SIZE);
                    break;
                case R.styleable.TestRotationRocket_imgSampleSize:
                    mDuration = typedArray.getInteger(R.styleable.TestRotationRocket_duration, DEF_DURATION);
                    break;
            }
        }

//        mImgSampleSize = typedArray.getInteger(R.styleable.TestRotationRocket_imgSampleSize, DEF_IMG_SAMPLE_SIZE);
//        mDuration = typedArray.getInteger(R.styleable.TestRotationRocket_duration, DEF_DURATION);


        setBackgroundColor(getResources().getColor(R.color.sandybrown));
        mBitmap = decodeSampledBitmap(getResources(), R.drawable.rocket, mImgSampleSize);
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        typedArray.recycle();

        int touchSlop = ViewUtils.getTouchSlop(getContext());
        Log.d(TAG, "touchSlop: " + touchSlop);

        mScroller = new Scroller(getContext());
    }

    /**
     * 根据属性计算火箭图片的宽高
     *
     * @param resources
     * @param rocket
     * @param imgSampleSize
     * @return
     */
    private Bitmap decodeSampledBitmap(Resources resources, int rocket, int imgSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = imgSampleSize;
        return BitmapFactory.decodeResource(resources, rocket, options);
    }


    /**
     * 重写onMeasure方法，根据xml属性来决定控件的宽高。
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width, height;

        if (widthMode == MeasureSpec.EXACTLY) { //使用者在布局文件里指定了具体的宽度
            width = widthSize;
        } else {//没有指定，或者设置成了wrap_content
            //这时候要计算具体的数值,宽度取图片的宽度
            width = mBitmapWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = mBitmapHeight;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);//在画布上画出图片
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startAnimation();
        }
        return false;
    }

    public void startAnimation() {     //开始动画
        ObjectAnimator.ofFloat(this, "rotationX", 0.0f, 360.0f).setDuration(mDuration).start();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
