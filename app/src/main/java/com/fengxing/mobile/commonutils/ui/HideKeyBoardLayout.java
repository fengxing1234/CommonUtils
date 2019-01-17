package com.fengxing.mobile.commonutils.ui;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


/**
 * 是否显示了软键盘
 */
public class HideKeyBoardLayout extends LinearLayout {


    public HideKeyBoardLayout(Context context) {
        this(context, null);
    }

    public HideKeyBoardLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HideKeyBoardLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (isShouldShowKeyBord(this)) {
            Log.e("CommonLinearLayout","show");
        }else {
            Log.e("CommonLinearLayout","hide");
        }
    }

    private boolean isShouldShowKeyBord(View rootView) {
        final int softKeyboardHeight = 100;
        Rect rect = new Rect();
        /**

         检索此视图所连接的窗口所在的整体可见显示大小。这考虑了窗口上方的屏幕装饰，对于窗口本身位于其中或窗口位于其下的两种情况 然后，覆盖的插图用于窗口将其内容定位在其中。
         实际上，这会告诉您可以放置内容并且对用户可见的可用区域。

         <p>此函数需要IPC返回窗口管理器以检索所请求的信息，因此不应在性能关键代码（如绘图）中使用。
         @param outRect填入可见的显示框。 如果视图未附加到窗口，则这只是原始显示大小。
         */
        //获取窗口可视区域大小
        rootView.getWindowVisibleDisplayFrame(rect);
        DisplayMetrics displayMetrics = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - rect.bottom;
        return heightDiff > softKeyboardHeight * displayMetrics.density;
    }
}
