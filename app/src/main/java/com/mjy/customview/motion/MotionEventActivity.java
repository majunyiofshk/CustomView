package com.mjy.customview.motion;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.mjy.customview.R;

/**
 * 事件类型
 */

public class MotionEventActivity extends AppCompatActivity {
    public static final String TAG = "MotionEventActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int actionMasked = event.getActionMasked();
        final int action = event.getAction();
        Log.e(TAG, "actionMasked = " + actionMasked + ", action = " + action);
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                printMotionEvent(event, "ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                printMotionEvent(event, "ACTION_MOVE");
                break;

            case MotionEvent.ACTION_UP:
                printMotionEvent(event, "ACTION_UP");
                break;

            case MotionEvent.ACTION_CANCEL:
                printMotionEvent(event, "ACTION_CANCEL");
                break;

            case MotionEvent.ACTION_OUTSIDE:
                printMotionEvent(event, "ACTION_OUTSIDE");
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                printMotionEvent(event, "ACTION_POINTER_DOWN");
                break;

            case MotionEvent.ACTION_POINTER_UP:
                printMotionEvent(event, "ACTION_POINTER_UP");
                break;

        }
        return super.onTouchEvent(event);
    }

    public static void printMotionEvent(MotionEvent event, String name){
        int index = event.getActionIndex();
        int pointerId = event.getPointerId(index);
        int pointerIndex = event.findPointerIndex(pointerId);
//        Log.e(TAG, name + " ---> " + "index = " + index + ", pointerId = "
//                + pointerId + ", pointerIndex = " + pointerIndex);
    }
}

/*
* 三根手指按下和抬起的index和pointerId
*
*        按下
* 手指  index  pointerId
*  1     0       0
*  2     1       1
*  3     2       2
*
*  按手指1,2,3依次抬起
* 手指  index  pointerId
*  1     0       0
*  2     0       1
*  3     0       2
*
*  按手指1,3,2依次抬起
* 手指  index  pointerId
*  1     0       0
*  3     1       2
*  2     0       1
*
*  按手指2,1,3依次抬起
* 手指  index  pointerId
*  2     1       1
*  1     0       0
*  3     0       2
*
*
*  按手指2,3,1依次抬起
* 手指  index  pointerId
*  2     1       1
*  3     1       2
*  1     0       0
*
*  按手指3,1,2依次抬起
* 手指  index  pointerId
*  3     2       2
*  1     0       0
*  2     0       1
*
*  按手指3,2,1依次抬起
* 手指  index  pointerId
*  3     2       2
*  2     1       1
*  1     0       0
* */
