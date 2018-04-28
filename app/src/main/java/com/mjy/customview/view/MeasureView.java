package com.mjy.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * View测试示例
 */

public class MeasureView extends View {
    public MeasureView(Context context) {
        this(context, null);
    }

    public MeasureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int mode = MeasureSpec.getMode(heightMeasureSpec);
//        int size = MeasureSpec.getSize(heightMeasureSpec);
//        Log.e("child--->", "mode = " + mode + ", size = " + size);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.e("child--->", "测量高度 = " + getMeasuredHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        Log.e("child--->", "right " + getBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
