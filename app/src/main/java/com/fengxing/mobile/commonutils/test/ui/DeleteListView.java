package com.fengxing.mobile.commonutils.test.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fengxing.mobile.commonutils.R;
import com.fengxing.mobile.commonutils.utils.ViewUtils;

public class DeleteListView extends ListView implements GestureDetector.OnGestureListener, View.OnTouchListener {

    private static final String TAG = "DeleteListView";

    public interface OnDeleteListener {

        void onDelete(int index);

    }


    private GestureDetector gestureDetector;

    private OnDeleteListener listener;

    private boolean isDeleteShown;


    private View deleteButton;

    private ViewGroup itemLayout;

    private int selectedItem;


    public DeleteListView(Context context) {
        super(context);
    }

    public DeleteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    public DeleteListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        gestureDetector = new GestureDetector(getContext(), this);
        setOnTouchListener(this);
    }

    public void setOnDeleteListener(OnDeleteListener l) {
        listener = l;
    }


    @Override
    public boolean onDown(MotionEvent e) {
//        Log.d(TAG, "onDown: " + e);
//        Log.d(TAG, "onDown: " + selectedItem);
//        selectedItem = pointToPosition((int) e.getX(), (int) e.getY());
//        Log.d(TAG, "onDown: " + selectedItem);
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG, "distanceX: " + distanceX + "  distanceY = " + distanceY);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, "velocityX : " + velocityX + "  velocityY = " + velocityY);

        Log.d(TAG, "onFling e1 : " + e1);
        Log.d(TAG, "onFling e2 : " + e2);
        Log.d(TAG, "onFling: start" + selectedItem);
        selectedItem = pointToPosition((int) e2.getX(), (int) e2.getY());
        Log.d(TAG, "onFling  end: " + selectedItem);

        if (selectedItem == -1) {
            return false;
        }
        if (!isDeleteShown && Math.abs(velocityX) > Math.abs(velocityY)) {
            deleteButton = LayoutInflater.from(getContext()).inflate(R.layout.test_button, null, false);
            deleteButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemLayout.removeView(deleteButton);
                    deleteButton = null;
                    isDeleteShown = false;
                    listener.onDelete(selectedItem);
                }
            });
            int firstVisiblePosition = getFirstVisiblePosition();
            itemLayout = (ViewGroup) getChildAt(selectedItem - getFirstVisiblePosition());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            itemLayout.addView(deleteButton, params);
            isDeleteShown = true;
        }
        return false;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int touchSlop = ViewUtils.getTouchSlop(getContext());
        float[] velocityTracker = ViewUtils.getVelocityTracker(event);
        Log.d(TAG, "onTouch: " + touchSlop);
        Log.d(TAG, "velocityTracker X : " + velocityTracker[0] + "  velocityTracker Y = " + velocityTracker[1]);
        if (isDeleteShown) {
            itemLayout.removeView(deleteButton);
            deleteButton = null;
            isDeleteShown = false;
            return false;
        } else {
            return gestureDetector.onTouchEvent(event);
        }

    }
}
