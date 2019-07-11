package com.aorise.ymguess;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/6/24.
 * 可移动View的Touch事件
 */
public class OnMoveViewTouchListener implements View.OnTouchListener {
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean isIntercept = false;
    /**
     * 按下时的位置控件相对屏幕左上角的位置X
     */
    private int startDownX;
    /**
     * 按下时的位置控件距离屏幕左上角的位置Y
     */
    private int startDownY;
    /**
     * 控件相对屏幕左上角移动的位置X
     */
    private int lastMoveX;
    /**
     * 控件相对屏幕左上角移动的位置Y
     */
    private int lastMoveY;

    private boolean isParentCoordinatorLayout;

    public OnMoveViewTouchListener(Context context, int height) {
        this(context, height, false);
    }

    public OnMoveViewTouchListener(Context context, int height, boolean isParentCoordinator) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels - (getStatusBarHeight(context) + height);
        isParentCoordinatorLayout = isParentCoordinator;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int eventaction = event.getAction(); // 得到事件动作

        if (eventaction == MotionEvent.ACTION_DOWN) {
            startDownX = lastMoveX = (int) event.getRawX();
            startDownY = lastMoveY = (int) event.getRawY();

        } else if (eventaction == MotionEvent.ACTION_MOVE) {
            int dx = (int) event.getRawX() - lastMoveX;
            int dy = (int) event.getRawY() - lastMoveY;

            int left = v.getLeft() + dx;
            int top = v.getTop() + dy;
            int right = v.getRight() + dx;
            int bottom = v.getBottom() + dy;
            if (left < 0) {
                left = 0;
                right = left + v.getWidth();
            }
            if (right > mScreenWidth) {
                right = mScreenWidth;
                left = right - v.getWidth();
            }
            if (top < 0) {
                top = 0;
                bottom = top + v.getHeight();
            }
            if (bottom > mScreenHeight) {
                bottom = mScreenHeight;
                top = bottom - v.getHeight();
                Log.i("123", "mScreenHeight " + mScreenHeight);
                Log.i("123", "bottom " + bottom);
                Log.i("123", "top " + top);
                Log.i("123", "v.getHeight() " + v.getHeight());
            }
            Log.i("123", "mScreenHeight " + mScreenHeight);
            Log.i("123", "bottom " + bottom);
            Log.i("123", "top " + top);
            Log.i("123", "v.getHeight() " + v.getHeight());
            v.layout(left, top, right, bottom);
            lastMoveX = (int) event.getRawX();
            lastMoveY = (int) event.getRawY();

        } else if (eventaction == MotionEvent.ACTION_UP) {
            int lastMoveDx = Math.abs((int) event.getRawX() - startDownX);
            int lastMoveDy = Math.abs((int) event.getRawY() - startDownY);
            isIntercept = Math.sqrt(lastMoveDx * lastMoveDx + lastMoveDy * lastMoveDy) > 5;
            // 每次移动都要设置其layout，不然由于父布局可能嵌套listview，当父布局发生改变冲毁（如下拉刷新时）则移动的view会回到原来的位置
            if (isParentCoordinatorLayout) {
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(v.getWidth(), v.getHeight());
                params.leftMargin = v.getLeft();
                params.topMargin = v.getTop();
                params.setMargins(v.getLeft(), v.getTop(), 0, 0);
                v.setLayoutParams(params);
            } else {
                RelativeLayout.LayoutParams lpFeedback = new RelativeLayout.LayoutParams(v.getWidth(), v.getHeight());
                lpFeedback.leftMargin = v.getLeft();
                lpFeedback.topMargin = v.getTop();
                lpFeedback.setMargins(v.getLeft(), v.getTop(), 0, 0);
                v.setLayoutParams(lpFeedback);
            }
        }
        return isIntercept;
    }
}
