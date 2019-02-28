package com.fengxing.mobile.commonutils.test.ui.animation.tween;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * 自定义补间动画（三维旋转动画）
 */
public class TestCustomAnimation extends Animation {


    private Camera mCamera = new Camera();

    private final int mDuration;
    private final float mCenterY;
    private final float mCenterX;

    public TestCustomAnimation(float x, float y, int duration) {
        mCenterX = x;
        mCenterY = y;
        mDuration = duration;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        //设置持续事件
        setDuration(mDuration);

        //设置动画结束后效果保留
        setFillAfter(true);

        // 设置匀速变换
        setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        mCamera.save();
        // 根据interpolatedTime时间来控制x、y、z轴上的偏移
        //mCamera.translate(100f - 100f * interpolatedTime, 150f * interpolatedTime - 150, 80.0f - 80.0f * interpolatedTime);
        // 设置根据interpolatedTime时间在x轴上旋转不同角度
        //mCamera.rotateX(360 * interpolatedTime);
        // 设置根据interpolatedTime时间在Y轴上旋转不同角度
        mCamera.rotateY(360 * interpolatedTime);

        Matrix matrix = t.getMatrix();
        mCamera.getMatrix(matrix);
        matrix.preTranslate(-mCenterX, -mCenterY);
        matrix.postTranslate(mCenterX, mCenterY);
        mCamera.restore();
    }
}
