package com.fengxing.mobile.commonutils.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.fengxing.mobile.commonutils.R;

public class ImageVerifyCodeView extends RelativeLayout {

    private ProgressBar progressBar;
    private ImageView ivCode;

    public ImageVerifyCodeView(Context context) {
        this(context, null);
    }

    public ImageVerifyCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageVerifyCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.image_verify_code_layout, this, true);
        ivCode = (ImageView) findViewById(R.id.iv_code);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    public void startRefreshCode() {
        progressBar.setVisibility(VISIBLE);
    }


    public void stopRefreshCode() {
        progressBar.setVisibility(GONE);
    }

    public ImageView getVerifyCodePic() {
        return ivCode;
    }
}
