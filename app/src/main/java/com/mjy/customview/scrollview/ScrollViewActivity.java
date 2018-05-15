package com.mjy.customview.scrollview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.mjy.customview.R;

/**
 * ScrollView相关
 */

public class ScrollViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}

/*
* fillViewport的作用:
* 首先要明确如下几点:
* 1.ScrollView的直接子View的测量模式为UNSPECIFIED,无论你在布局中指定确切值、match_parent还是wrap_content,它的测量
*   模式恒为UNSPECIFIED。在这种模式下,子View有多大,那么父容器就有多大。此时:父容器高度 = 子最大高度。
* 2.结论1是建立在ScrollView的fillViewport属性为false的情况下,ScrollView默认fillViewport为false。
* 3.当fillViewport为true的时候,那么ScrollView会进行多一步的判断测量。经过结论1的测量之后,会判断ScrollView的直接子
*   View大小是否超过ScrollView的大小,如果没有,那么会重新测量ScrollView的子View,并把子View测量模式设置为EXACTLY,大小
*   为ScrollView的大小。如果超过,就不做处理。此时:(1)小于ScrollView高度:父容器高度 = ScrollView高度,(2)大于
*   ScrollView高度:父容器高度 = 子View最大高度。
* 4.有1和3可以得出ScrollView的直接子View高度不受layout_height属性的干扰,要么是子View最大高度,要么是ScrollView大小。
* */


