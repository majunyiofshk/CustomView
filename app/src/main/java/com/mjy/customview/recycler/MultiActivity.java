package com.mjy.customview.recycler;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.mjy.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 展示RecyclerView,该RecyclerView中没一个条目都是一个RecyclerView
 */

public class MultiActivity extends AppCompatActivity {

    private static final int[] colors = new int[]{Color.GREEN, Color.BLUE, Color.GRAY, Color.RED};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);
        RecyclerView rvMulti = (RecyclerView) findViewById(R.id.rv_multi);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        rvMulti.setLayoutManager(layout);
        MultiAdapter adapter = new MultiAdapter(createData());
        rvMulti.setAdapter(adapter);
        DividerItemDecoration decoration = new DividerItemDecoration(this, LinearLayout.VERTICAL);
        rvMulti.addItemDecoration(decoration);
    }

    private static List<MultiBean> createData() {
        List<MultiBean> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            MultiBean multiBean = new MultiBean();
            String title = "标题" + (i + 1);
            multiBean.id = i;
            multiBean.title = title;
            multiBean.color = colors[i];
            List<String> subList = new ArrayList<>();
            for (int j = 0; j < 12; j++) {
                subList.add(title + " - 条目" + (j + 1));
            }
            multiBean.content = subList;
            list.add(multiBean);
        }
        return list;
    }
}
