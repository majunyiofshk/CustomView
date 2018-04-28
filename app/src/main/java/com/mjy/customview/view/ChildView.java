package com.mjy.customview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mjy on 2018/4/24.
 */

public class ChildView extends View {
    public ChildView(Context context) {
        this(context, null);
    }

    public ChildView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        boolean consumed = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                consumed = true;
                Log.e("ChildView", "down事件");
                break;
            case MotionEvent.ACTION_MOVE:
                consumed = true;
                Log.e("ChildView", "move事件");
                break;
            case MotionEvent.ACTION_UP:
                consumed = true;
                Log.e("ChildView", "up事件");
                break;
        }
        return false;
    }
}