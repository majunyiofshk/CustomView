package com.mjy.customview.gesture;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * 处理手势的View
 */

public class GestureView extends AppCompatTextView implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private GestureDetector mGestureDetector;

    public GestureView(Context context) {
        this(context, null);
    }

    public GestureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initGestureDetector(context);
    }

    private void initGestureDetector(Context context) {
        mGestureDetector = new GestureDetector(context, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    /* ----------------------- OnGestureListener接口中的方法 ----------------------------*/

    @Override
    public boolean onDown(MotionEvent e) {
        Log.e("GestureView", "接口：OnGestureListener, 行为：onDown, 事件："
                + Convert.convert(e.getAction()));
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.e("GestureView", "接口：OnGestureListener, 行为：onShowPress, 事件："
                + Convert.convert(e.getAction()));
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.e("GestureView", "接口：OnGestureListener, 行为：onSingleTapUp, 事件："
                + Convert.convert(e.getAction()));
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.e("GestureView", "接口：OnGestureListener, 行为：onScroll, 事件e1："
                + Convert.convert(e1.getAction()) + ", 事件e2: " + Convert.convert(e2.getAction()));
        Log.e("GestureView", "distanceX = " + distanceX + ", distanceY" + distanceY);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.e("GestureView", "接口：OnGestureListener, 行为：onLongPress, 事件："
                + Convert.convert(e.getAction()));
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.e("GestureView", "接口：OnGestureListener, 行为：onFling, 事件："
                + Convert.convert(e1.getAction()));
        return true;
    }

    /* ----------------------- OnDoubleTapListener接口中的方法 ----------------------------*/

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.e("GestureView", "接口：OnDoubleTapListener, 行为：onSingleTapConfirmed, 事件："
                + Convert.convert(e.getAction()));
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.e("GestureView", "接口：OnDoubleTapListener, 行为：onDoubleTap, 事件："
                + Convert.convert(e.getAction()));
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.e("GestureView", "接口：OnDoubleTapListener, 行为：onDoubleTapEvent, 事件："
                + Convert.convert(e.getAction()));
        return true;
    }

    public static class Convert {
        public static String convert(int action) {
            String actionString = "";
            switch (action) {
                case 0:
                    actionString = "ACTION_DOWN";
                    break;
                case 1:
                    actionString = "ACTION_UP";
                    break;
                case 2:
                    actionString = "ACTION_MOVE";
                    break;
                case 3:
                    actionString = "ACTION_CANCEL";
                    break;
                case 4:
                    actionString = "ACTION_OUTSIDE";
                    break;
                case 5:
                    actionString = "ACTION_POINTER_DOWN";
                    break;
                case 6:
                    actionString = "ACTION_POINTER_UP";
                    break;
            }
            return actionString;
        }
    }
}

/*
* 回调执行的先后顺序以及产生的事件类型
*
* 操作1:快速点击屏幕一次
* 回调:onDown, 事件:ACTION_DOWN
* 回调:onSingleTapUp, 事件:ACTION_UP
* 回调:onSingleTapConfirmed, 事件:ACTION_DOWN
*
* 操作2:较操作1稍微慢一点
* 回调:onDown, 事件:ACTION_DOWN
* 回调:onShowPress, 事件:ACTION_DOWN
* 回调:onSingleTapUp, 事件:ACTION_UP
* 回调:onSingleTapConfirmed, 事件:ACTION_DOWN
*
* 操作3:按压
* 回调:onDown, 事件:ACTION_DOWN
* 回调:onShowPress, 事件:ACTION_DOWN
* 回调:onLongPress, 事件:ACTION_DOWN
*
* 操作4:快速点击两次
* 回调:onDown, 事件:ACTION_DOWN
* 回调:onSingleTapUp, 事件:ACTION_UP
* 回调:onDoubleTap, 事件:ACTION_DOWN
* 回调:onDoubleTapEvent, 事件:ACTION_DOWN
* 回调:onDown, 事件:ACTION_DOWN
* 回调:onDoubleTapEvent, 事件:ACTION_UP
*
* 由上诉几个操作可以得出几个结论:
* 1.onSingleTapConfirmed方法回调可以证明某次操作是单击行为。
* 2.onLongPress方法回调可以证明某次某次操作是按压行为。
* 3.onDoubleTap方法回调可以证明是双击行为
*
* 回调返回值得影响:
* 1.onDown回调返回false,对于手指所有的操作系统都会认为是按压操作,所以,想要手势能够正常的工作,那么onDow的返回值必须为true。
* */
