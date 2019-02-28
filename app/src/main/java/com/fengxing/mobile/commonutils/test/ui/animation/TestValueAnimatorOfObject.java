package com.fengxing.mobile.commonutils.test.ui.animation;

import android.animation.TypeEvaluator;
import android.util.Log;

import com.fengxing.mobile.commonutils.test.data.TestValueAnimatorOfObjectPointData;

public class TestValueAnimatorOfObject implements TypeEvaluator {


    private static final String TAG = "TestValueAnimator";

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        // 将动画初始值startValue 和 动画结束值endValue 强制类型转换成Point对象
        TestValueAnimatorOfObjectPointData startPoint = (TestValueAnimatorOfObjectPointData) startValue;
        TestValueAnimatorOfObjectPointData endPoint = (TestValueAnimatorOfObjectPointData) endValue;

        // 根据fraction来计算当前动画的x和y的值
        Log.d(TAG, "evaluate:  fraction = " + fraction);
        //（结束值 - 开始值 ）* 时间差 就是移动到的值
        // 10             20  fraction = 0.1 当前的时间应该移动一个单位
        //10+（20-10）*0.1 = 11
        float x = startPoint.startX + fraction * (endPoint.startX - startPoint.startX);
        float y = startPoint.startY + fraction * (endPoint.startY - startPoint.startY);
        TestValueAnimatorOfObjectPointData data = new TestValueAnimatorOfObjectPointData(x, y);
        return data;
    }
}
