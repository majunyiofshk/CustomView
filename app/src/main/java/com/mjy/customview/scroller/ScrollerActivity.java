package com.mjy.customview.scroller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
        initView();
    }

    private void initView() {
        final View parent = findViewById(R.id.fl_container);
        final Button btn = (Button) findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printArea(btn);
                parent.scrollBy(-100, 0);
                printArea(btn);
            }
        });
    }

    private static void printArea(View view) {
        final int left = view.getLeft();
        final int top = view.getTop();
        final int right = view.getRight();
        final int bottom = view.getBottom();
        Log.e(TAG, "left = " + left + ", top = " + top +
                ", right = " + right + ", bottom = " + bottom);
    }


}
