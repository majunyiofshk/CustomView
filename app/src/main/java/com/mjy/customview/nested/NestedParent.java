package com.mjy.customview.nested;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 嵌套滚动
 */

public class NestedParent extends FrameLayout implements NestedScrollingParent{

    private static final String TAG = "NestedParent";

    public NestedParent(@NonNull Context context) {
        super(context);
    }

    public NestedParent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onStartNestedScroll");
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        Log.e(TAG, "onNestedScrollAccepted"); //顺序数组删除重复元素,
        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        Log.e(TAG, "onStopNestedScroll");
        super.onStopNestedScroll(child);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedPreScroll：" + "dx = " + dx + ", dy = " + dy);
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.e(TAG, "onNestedScroll：" + "dxConsumed = " + dxConsumed + ", dyConsumed = " + dyConsumed
        + ", dxUnconsumed = " + dxUnconsumed + ", dyUnconsumed = " + dyUnconsumed);
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public int getNestedScrollAxes() {
        return super.getNestedScrollAxes();
    }
}

/*
* 切入点:刷新控件什么时候出现?做了什么动画?
* */