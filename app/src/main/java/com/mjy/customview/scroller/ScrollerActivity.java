package com.mjy.customview.scroller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.OverScroller;

import com.mjy.customview.R;

/**
 * Scroller使用
 */

public class ScrollerActivity extends AppCompatActivity {
    public static final String TAG = "ScrollerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);
        initScroller();
        initFlingScroller();
    }

    private void initScroller() {
        OverScroller scroller = new OverScroller(this);
        scroller.startScroll(100, -100, 100, 100);
        final int currX = scroller.getCurrX();
        final int currY = scroller.getCurrY();
        final int finalX = scroller.getFinalX();
        final int finalY = scroller.getFinalY();
        Log.e(TAG, "startScroll ---> currX = " + currX + ", currY = " + currY +
                ", finalX = " + finalX + ", finalY = " + finalY);
    }

    private void initFlingScroller(){
        OverScroller scroller = new OverScroller(this);
        scroller.fling(100, 0, 9000, 0, 0, 1000, 0, 0, 0, 200);
        final int currX = scroller.getCurrX();
        final int currY = scroller.getCurrY();
        final int finalX = scroller.getFinalX();
        final int finalY = scroller.getFinalY();
        Log.e(TAG, "fling ---> currX = " + currX + ", currY = " + currY +
                ", finalX = " + finalX + ", finalY = " + finalY);
    }
}
