package com.fengxing.mobile.commonutils.test;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fengxing.mobile.commonutils.R;

import java.net.URL;

public class TestSpannableActivity extends AppCompatActivity {

    private static final String TAG = "TestSpannableActivity";
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_spannable_activity);
        textView = (TextView) findViewById(R.id.tv_spannable);

        CharSequence text = textView.getText();
        String content = text.toString();


        SpannableStringBuilder style = new SpannableStringBuilder(text);
        style.clearSpans();// should clear old spans

        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) text;
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            for (final URLSpan url : urls) {
                if (!TextUtils.isEmpty(url.getURL()) && (url.getURL().startsWith("http://")) || url.getURL().startsWith("https://") || url.getURL().startsWith("com.picc.mcp"))
                    style.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            Toast.makeText(TestSpannableActivity.this, "点击了", Toast.LENGTH_LONG).show();
                        }
                    }, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                //设置字体（依次包括字体名称，字体样式，字体大小，字体颜色，链接颜色）
                ColorStateList csl = getResources().getColorStateList(R.color.red);
                style.setSpan(new TextAppearanceSpan(null, Typeface.NORMAL, 50, csl, csl), sp.getSpanStart(url), sp.getSpanEnd(url), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (content.startsWith("您有新的") && content.endsWith("，请注意查收！") && content.contains("车牌号") && content.contains("报案号")) {
                style.setSpan(new URLSpan("com.picc.mcp.Main2Activity"), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                style.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Toast.makeText(TestSpannableActivity.this, "新任务下发", Toast.LENGTH_LONG).show();
                    }
                }, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ColorStateList csl = getResources().getColorStateList(R.color.red);
                style.setSpan(new TextAppearanceSpan(null, Typeface.NORMAL, 50, csl, csl), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            textView.setText(style);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
        if (content.startsWith("您有新的") && content.endsWith("，请注意查收！") && content.contains("车牌号") && content.contains("报案号")) {
            style.setSpan(new URLSpan("com.picc.mcp.Main2Activity"), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Toast.makeText(TestSpannableActivity.this, "新任务下发", Toast.LENGTH_LONG).show();
                }
            }, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ColorStateList csl = getResources().getColorStateList(R.color.red);
            style.setSpan(new TextAppearanceSpan(null, Typeface.NORMAL, 50, csl, csl), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(style);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @NonNull
    private CharSequence getCharSequence() {
        CharSequence text = textView.getText();
        SpannableString spannableStringBuilder = new SpannableString(text);
        spannableStringBuilder.setSpan(new URLSpan("com.picc.mcp.Main2Activity"), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(TestSpannableActivity.this, "点击了", Toast.LENGTH_LONG).show();
            }
        }, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableStringBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());


        if (textView.getText() instanceof Spannable) {
            URLSpan[] spans = ((Spannable) textView.getText()).getSpans(0, textView.getText().length(), URLSpan.class);
            Log.d(TAG, "onCreate: " + spans.length);
        }
        return text;
    }
}
