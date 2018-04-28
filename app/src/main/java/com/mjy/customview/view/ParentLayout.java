package com.mjy.customview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by mjy on 2018/4/24.
 */

public class ParentLayout extends FrameLayout {
    public ParentLayout(@NonNull Context context) {
        this(context, null);
    }

    public ParentLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                intercept = true;
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.e("ParentLayout", "down事件");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("ParentLayout", "move事件");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("ParentLayout", "up事件");
                break;
        }
        return false;
    }
}

/*
  1.子View的down事件返回true,那么后续事件都会传递给子View(即使子View在在后续的事件中都返回false)
  2.子View的down事件返回false,那么后续事件都不会传递给子View,也不会调用父容器的onInterceptTouchEvent的方法,这时会
    调用父容器的onTouchEvent,如果父容器的事件传递规则桶子View的onTouchEvent处理方式。
  3.父容器如果拦截了事件,那么后续事件都不会传递给子View(自这次拦截事件的所有后续事件),将会调用父容器的onTouchEvent,
    父容器的事件传递规则桶子View的onTouchEvent处理方式。
*/

