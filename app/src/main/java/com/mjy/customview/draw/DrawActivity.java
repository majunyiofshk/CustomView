package com.mjy.customview.draw;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mjy.customview.R;

import java.lang.reflect.Field;

/**
 *
 */

public class DrawActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawViewGroup mParent;
    private DrawView mChild;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        initView();
    }

    private void initView() {
        Button btnParent = (Button) findViewById(R.id.btn_parent);
        btnParent.setOnClickListener(this);
        Button btnChild = (Button) findViewById(R.id.btn_child);
        btnChild.setOnClickListener(this);

        mParent = (DrawViewGroup) findViewById(R.id.dvg_parent);
        mChild = (DrawView) findViewById(R.id.dv_child);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_parent:
                mParent.invalidate();
                break;
            case R.id.btn_child:
                int flag = -2130704336;
                int mask = 0x00600000;
                int value = flag & mask;

                try {
                    Class<?> clazz = mChild.getClass().getSuperclass();
                    Field field = clazz.getDeclaredField("mPrivateFlags");
                    field.setAccessible(true);
                    int privateFlag = (int) field.get(mParent);
                    Log.e("DrawActivity", "privateFlag = " + privateFlag);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("DrawActivity", "反射调用失败");
                }

                mChild.invalidate();

                break;
        }
    }
}
