package com.fengxing.mobile.commonutils.animation.tween;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import com.fengxing.mobile.commonutils.R;

public class TestTweenAnimActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_tween_anim_activity);

        Button btn_translate_anim = findViewById(R.id.btn_translate_anim);


        //平移动画 xml文件定义
        //xmlTranslateAnim(btn_translate_anim);

        //平移动画 java代码定义
        //javaTranslateAnim(btn_translate_anim);

        //set 动画xml方式
        xmlSetAnim(btn_translate_anim);

        //set动画 java方式
        javaSetAnim(btn_translate_anim);


    }

    private void javaSetAnim(Button btn_translate_anim) {
        AnimationSet setAnimation = new AnimationSet(true);
        // 步骤1:创建组合动画对象(设置为true)


        // 步骤2:设置组合动画的属性
        // 特别说明以下情况
        // 因为在下面的旋转动画设置了无限循环(RepeatCount = INFINITE)
        // 所以动画不会结束，而是无限循环
        // 所以组合动画的下面两行设置是无效的
        setAnimation.setRepeatMode(Animation.RESTART);
        setAnimation.setRepeatCount(1);// 设置了循环一次,但无效

        // 步骤3:逐个创建子动画(方式同单个动画创建方式,此处不作过多描述)

        // 子动画1:旋转动画
        Animation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);

        // 子动画2:平移动画
        Animation translate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, -0.5f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0.5f,
                TranslateAnimation.RELATIVE_TO_SELF, 0
                , TranslateAnimation.RELATIVE_TO_SELF, 0);
        translate.setDuration(10000);

        // 子动画3:透明度动画
        Animation alpha = new AlphaAnimation(1, 0);
        alpha.setDuration(3000);
        alpha.setStartOffset(7000);

        // 子动画4:缩放动画
        Animation scale1 = new ScaleAnimation(1, 0.5f, 1, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale1.setDuration(1000);
        scale1.setStartOffset(4000);

        // 步骤4:将创建的子动画添加到组合动画里
        setAnimation.addAnimation(alpha);
        setAnimation.addAnimation(rotate);
        setAnimation.addAnimation(translate);
        setAnimation.addAnimation(scale1);

        btn_translate_anim.startAnimation(setAnimation);
    }

    private void xmlSetAnim(Button btn_translate_anim) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.test_anim_tween_set);
        btn_translate_anim.startAnimation(animation);
    }

    private void javaTranslateAnim(Button btn_translate_anim) {
        TranslateAnimation animation = new TranslateAnimation(0, 1000, 0, 1000);
        animation.setDuration(5000);
        btn_translate_anim.startAnimation(animation);
    }

    private void xmlTranslateAnim(Button btn_translate_anim) {
        Animation translateAnim = AnimationUtils.loadAnimation(this, R.anim.test_anim_tween_translate);
        btn_translate_anim.startAnimation(translateAnim);
    }

    /**
     *
     * 缩放xml属性
     *
     *
     // 以下参数是缩放动画特有的属性
     android:fromXScale="0.0"
     // 动画在水平方向X的起始缩放倍数
     // 0.0表示收缩到没有；1.0表示正常无伸缩
     // 值小于1.0表示收缩；值大于1.0表示放大

     android:toXScale="2"  //动画在水平方向X的结束缩放倍数

     android:fromYScale="0.0" //动画开始前在竖直方向Y的起始缩放倍数
     android:toYScale="2" //动画在竖直方向Y的结束缩放倍数

     android:pivotX="50%" // 缩放轴点的x坐标
     android:pivotY="50%" // 缩放轴点的y坐标
     // 轴点 = 视图缩放的中心点

     // pivotX pivotY,可取值为数字，百分比，或者百分比p
     // 设置为数字时（如50），轴点为View的左上角的原点在x方向和y方向加上50px的点。在Java代码里面设置这个参数的对应参数是Animation.ABSOLUTE。
     // 设置为百分比时（如50%），轴点为View的左上角的原点在x方向加上自身宽度50%和y方向自身高度50%的点。在Java代码里面设置这个参数的对应参数是Animation.RELATIVE_TO_SELF。
     // 设置为百分比p时（如50%p），轴点为View的左上角的原点在x方向加上父控件宽度50%和y方向父控件高度50%的点。在Java代码里面设置这个参数的对应参数是Animation.RELATIVE_TO_PARENT
     */


    /**
     *
     *
     * 旋转动画xml属性

     // 以下参数是旋转动画特有的属性
     android:duration="1000"
     android:fromDegrees="0" // 动画开始时 视图的旋转角度(正数 = 顺时针，负数 = 逆时针)
     android:toDegrees="270" // 动画结束时 视图的旋转角度(正数 = 顺时针，负数 = 逆时针)
     android:pivotX="50%" // 旋转轴点的x坐标
     android:pivotY="0" // 旋转轴点的y坐标
     // 轴点 = 视图缩放的中心点

     // pivotX pivotY,可取值为数字，百分比，或者百分比p
     // 设置为数字时（如50），轴点为View的左上角的原点在x方向和y方向加上50px的点。在Java代码里面设置这个参数的对应参数是Animation.ABSOLUTE。
     // 设置为百分比时（如50%），轴点为View的左上角的原点在x方向加上自身宽度50%和y方向自身高度50%的点。在Java代码里面设置这个参数的对应参数是Animation.RELATIVE_TO_SELF。
     // 设置为百分比p时（如50%p），轴点为View的左上角的原点在x方向加上父控件宽度50%和y方向父控件高度50%的点。在Java代码里面设置这个参数的对应参数是Animation.RELATIVE_TO_PARENT

     */


    /**
     *
     * 透明动画xml属性
     *  // 以下参数是透明度动画特有的属性
     android:fromAlpha="1.0" // 动画开始时视图的透明度(取值范围: -1 ~ 1)
     android:toAlpha="0.0"// 动画结束时视图的透明度(取值范围: -1 ~ 1)

     */
}
