package com.mjy.customview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mjy.customview.view.RulerView;

/**
 * 薄荷卷尺
 */

public class RulerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruler);
        initView();
    }

    private void initView() {
        Button btnRuler = (Button) findViewById(R.id.btnRuler);
        final RulerView rulerView = (RulerView) findViewById(R.id.rulerView);
        btnRuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rulerView.scrollBy(50, 0);
            }
        });
    }
}
