package com.mjy.customview.touch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mjy.customview.R;

/**
 * 手势探测器
 */

public class GestureDetectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_detector);
    }
}

/*
* 理解手势的核心:
* 1.手势事件的含义
* 2.手势所触发的条件
* 3.利用这些手势做出那些效果
* 4.手势事件的返回值对于其他手势的影响
* */

/*
* 当设置了onTouch
* */