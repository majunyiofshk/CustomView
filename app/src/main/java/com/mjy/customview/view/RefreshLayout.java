package com.mjy.customview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 下拉刷新的View
 */

public class RefreshLayout extends FrameLayout {
    public RefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /*
    * 大小应该是多少
    * 1.应该和它包裹的控件大小一致
    * 2.是否沿用父类的测量大小
    * */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /*
    * 摆放的规则
    * */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}

/*
* 1.测量大小
* 2.触摸事件的处理 ---->难点
* 3.开启下拉刷新
* 4.下拉后出现的动画效果
* 5.关闭下拉刷新
* 6.是否实现嵌套滚动
* */
