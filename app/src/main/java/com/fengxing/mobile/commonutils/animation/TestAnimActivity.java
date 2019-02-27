package com.fengxing.mobile.commonutils.animation;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fengxing.mobile.commonutils.R;

public class TestAnimActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_anim_activity);
        View testAlpha = findViewById(R.id.btn_test_alpha);
        //objectAnim(testAlpha);

        TestValueAnimatorView valueAnimatorView = findViewById(R.id.view_value_anim);
        ObjectAnimator colorAnim = ObjectAnimator.ofObject(valueAnimatorView, "color",new TestObjectColorEvaluator(),"#0000ff","#ff0000");
        colorAnim.setDuration(8000);
        colorAnim.start();
    }

    private void objectAnim(View testAlpha) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(testAlpha, "alpha", 1.0f, 0.5f, 0.4f, 0.1f, 0.5f, 0.8f);
        alpha.setDuration(5000);
        alpha.start();
    }
}
