package com.mjy.customview.recycler;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mjy.customview.R;

import java.util.List;

/**
 * RecyclerView相关知识
 */

public class RecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        initView();
    }

    private void initView() {
        RecyclerView rvThink = (RecyclerView) findViewById(R.id.rv_think);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvThink.setLayoutManager(manager);
        RecyclerAdapter adapter = new RecyclerAdapter();
        rvThink.setAdapter(adapter);
//        LinearSnapHelper snapHelper = new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(rvThink);
        PackageManager pm = getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo info : packages) {
            String packageName = info.packageName;
            Log.e("RecyclerViewActivity", packageName);
        }
    }
}

/*
* 1.RecyclerView源码--->测量,摆放,触摸,复用,动画
* 2.RecyclerView分割线
* 3.RecyclerView帮助类--->ItemTouchHelper、SnapHelper、AdapterHelper、ChildHelper
* 4.RecyclerView工具类--->DiffUtil等
* */

/*
* 测量过程
* 1.在RecyclerView的宽高都为EXACTLY时,会先测量自己并且跳过测量子View。在onLayout中进行测量子View,然后执行摆放。
* 2.其余情况,在onMeasure中先测量孩子,在测量本身。先前已经执行过的过程会被跳过。
* */

/*
* RecyclerView中重要的成员:
*
* 1.mState:保存着当前View的一些重要状态
*  (1)mIsMeasuring:表示当前RecyclerView是否处于测量阶段。
*  (2)mLayoutStep:表示当前RecyclerView处于layout的哪一个阶段
*     STEP_START:第一步 ---> dispatchLayoutStep1
*     STEP_LAYOUT:第二步 ---> dispatchLayoutStep2
*     STEP_ANIMATIONS:第三步 ---> dispatchLayoutStep3
* */
