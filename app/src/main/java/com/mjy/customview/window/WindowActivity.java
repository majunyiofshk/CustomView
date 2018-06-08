package com.mjy.customview.window;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mjy.customview.R;
import com.mjy.customview.scroller.ScrollerActivity;

/**
 * window相关知识
 */

public class WindowActivity extends AppCompatActivity implements View.OnClickListener {

    private WindowManager mWm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window);

        initView();

        mWm = getWindowManager();
    }

    private void initView() {
        Button btnAddView = (Button) findViewById(R.id.btn_add_view);
        Button btnRemoveView = (Button) findViewById(R.id.btn_remove_view);
        btnAddView.setOnClickListener(this);
        btnRemoveView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_view:
                addView();
                break;
            case R.id.btn_remove_view:
                removeView();
                break;
        }
    }

    private void addView() {
        TextView textView = new TextView(this);
        textView.setText("添加新的View");
        textView.setTextColor(Color.RED);
        textView.setTextSize(50);

        mWm.addView(textView, createLayoutParams());
    }

    private void removeView() {
        Intent intent = new Intent(this, ScrollerActivity.class);
        startActivity(intent);
    }

    private WindowManager.LayoutParams createLayoutParams(){
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        return lp;
    }
}

/*
* 1.编码能力
* 2.架构的形成
* 3.网络
* 4.binder的代码怎么写
* */