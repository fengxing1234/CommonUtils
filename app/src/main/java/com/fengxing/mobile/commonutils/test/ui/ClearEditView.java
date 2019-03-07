package com.fengxing.mobile.commonutils.test.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.fengxing.mobile.commonutils.R;

@SuppressLint("AppCompatCustomView")
public class ClearEditView extends EditText implements View.OnTouchListener, View.OnFocusChangeListener {

    private Drawable mClear;

    public ClearEditView(Context context) {
        super(context);
    }

    public ClearEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mClear = getResources().getDrawable(R.drawable.delete);
        mClear.setBounds(0, 0, mClear.getIntrinsicWidth(), mClear.getIntrinsicHeight());
        doClearDrawable();
        setOnTouchListener(this);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doClearDrawable();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void doClearDrawable() {
        if ("".equals(getText().toString().trim()) || !isFocused()) {
            setClearDrawableNull();
            return;
        }
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mClear, getCompoundDrawables()[3]);
    }

    private void setClearDrawableNull() {
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], null, getCompoundDrawables()[3]);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] == null) {
            return false;
        }
        if (event.getAction() != MotionEvent.ACTION_UP || event.getX() <= getWidth() - getPaddingRight() - mClear.getIntrinsicWidth()) {
            return false;
        }
        getText().clear();
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        doClearDrawable();
    }
}
