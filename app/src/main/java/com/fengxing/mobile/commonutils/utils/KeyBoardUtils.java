package com.fengxing.mobile.commonutils.utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

public class KeyBoardUtils {

    private static int contentViewInvisibleHeight;
    private static OnSoftInputChangedListener onSoftInputChangedListener;
    private static ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    public interface OnSoftInputChangedListener {
        void onSoftInputChanged(int height);
    }


    /**
     * 监听根布局高度是否发生变换
     *
     * @param activity
     * @param listener
     */
    public static void registerSoftInputChangedListener(final Activity activity, final OnSoftInputChangedListener listener) {
        int flags = activity.getWindow().getAttributes().flags;
        if ((flags & WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        View contentView = activity.findViewById(android.R.id.content);
        onSoftInputChangedListener = listener;
        contentViewInvisibleHeight = getContentViewInvisibleHeight(activity);
        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (onSoftInputChangedListener != null) {
                    int height = getContentViewInvisibleHeight(activity);
                    if (contentViewInvisibleHeight != height) {
                        onSoftInputChangedListener.onSoftInputChanged(height);
                        contentViewInvisibleHeight = height;
                    }
                }
            }
        };
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    /**
     * 取消监听
     *
     * @param activity
     */
    public static void unregisterSoftInputChangedListener(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        view.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        onGlobalLayoutListener = null;
        onSoftInputChangedListener = null;
    }

    /**
     * 获取布局被遮挡部分的高度
     *
     * @param activity
     * @return
     */
    public static int getContentViewInvisibleHeight(Activity activity) {
        View contentView = activity.findViewById(android.R.id.content);
        Rect rect = new Rect();
        contentView.getWindowVisibleDisplayFrame(rect);
        return contentView.getHeight() - rect.bottom;
    }

    /**
     * 获取rootview的高度
     *
     * @param activity
     * @return
     */
    public static int getRootViewHeight(Activity activity) {
        View contentView = activity.findViewById(android.R.id.content);// 全屏模式下：直接返回r.bottom，r.top其实是状态栏的高度
        Rect rect = new Rect();
        contentView.getWindowVisibleDisplayFrame(rect);
        return rect.bottom - rect.top;
    }


    /**
     * 判断是否显示软键盘
     *
     * @param activity
     * @return
     */
    public static boolean isSoftInputVisible(Activity activity) {
        return isSoftInputVisible(activity, 200);
    }

    /**
     * 判断是否显示软键盘
     *
     * @param activity
     * @param minSoftInputHeight
     * @return
     */
    public static boolean isSoftInputVisible(Activity activity, int minSoftInputHeight) {
        return getContentViewInvisibleHeight(activity) > minSoftInputHeight;
    }

    /**
     * 滑动
     *
     * @param activity
     * @param view
     */
    public static void scrollView(final Activity activity, final View view) {
        final ViewWrapper viewWrapper = new ViewWrapper(view);
        registerSoftInputChangedListener(activity, new OnSoftInputChangedListener() {

            private int scroll;

            @Override
            public void onSoftInputChanged(int height) {
                if (isSoftInputVisible(activity)) {
                    scroll = getContentViewInvisibleHeight(activity);
                    if (scroll > 0) {
                        ObjectAnimator topMarginAnimator = ObjectAnimator.ofInt(viewWrapper, "topMargin", 0, -scroll);
                        topMarginAnimator.setDuration(200).start();
                    }
                } else {
                    if (scroll > 0) {
                        ObjectAnimator topMarginAnimator = ObjectAnimator.ofInt(viewWrapper, "topMargin", -scroll, 0);
                        topMarginAnimator.setDuration(200).start();
                    }

                }
            }
        });
    }

    public static class ViewWrapper {

        private View mTarget;
        private final FrameLayout.LayoutParams layoutParams;

        public ViewWrapper(View target) {
            mTarget = target;
            layoutParams = (FrameLayout.LayoutParams) mTarget.getLayoutParams();
        }

        public void setTopMargin(int topMargin) {
            setTopMargin(0, topMargin, 0, 0);
        }

        public void setTopMargin(int left, int top, int right, int bottom) {
            layoutParams.setMargins(left, top, right, bottom);
            mTarget.setLayoutParams(layoutParams);
            mTarget.requestLayout();
        }

        public int getTopMargin() {
            return layoutParams.topMargin;
        }
    }


    /**
     * 关闭键盘
     *
     * @param view
     */
    public static void hideSoftInput(View view) {
        if (view != null) {
            Context context = view.getContext();
            IBinder windowToken = view.getWindowToken();
            InputMethodManager im = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 关闭键盘
     *
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        if (activity != null) {
            IBinder windowToken = activity.getWindow().getDecorView().getWindowToken();
            InputMethodManager im = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 打开软键盘
     *
     * @param context
     */
    public static void openSoftInput(Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    /**
     * 打开关闭相互切换
     *
     * @param activity
     */
    public static void switchKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 判断键盘 是显示 还是隐藏
     * <p>
     * true 显示
     * false 隐藏
     *
     * @param context
     * @return
     */
    public static boolean getKeyBoardState(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();//isOpen若返回true，则表示输入法打开
    }


    /**
     * 隐藏键盘
     *
     * @return
     */
    boolean hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = activity.getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                boolean result = false;
                try {
                    result = inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
        }

        return false;
    }

    /**
     * 解决EditText登录界面遮挡布局问题
     *
     * @param root
     * @param scrollView
     */
    private void controlKeyboardLayout(final View root, final View scrollView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取根布局显示区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                if (rootInvisibleHeight > 300) {// 键盘显示
                    int[] location = new int[2];
                    scrollView.getLocationInWindow(location);
                    int scrollHeight = (location[1] + scrollView.getHeight()) - rootInvisibleHeight;
                    root.scrollTo(0, scrollHeight);
                } else {//键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }

}
