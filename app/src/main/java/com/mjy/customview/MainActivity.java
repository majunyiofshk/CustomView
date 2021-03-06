package com.mjy.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mjy.customview.touch.TouchAdapter;
import com.mjy.customview.touch.TouchData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TouchAdapter.OnItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView rvTouch = (RecyclerView) findViewById(R.id.rv_touch);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        rvTouch.setLayoutManager(layout);
        TouchAdapter adapter = new TouchAdapter(createListData());
        adapter.setOnItemClickListener(this);
        rvTouch.setAdapter(adapter);
        DividerItemDecoration decoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        rvTouch.addItemDecoration(decoration);
    }

    private List<TouchData> createListData() {
        List<TouchData> list = new ArrayList<>();
        String[] itemNames = getResources().getStringArray(R.array.itemNames);
        String[] itemActions = getResources().getStringArray(R.array.itemActions);
        final int size = itemNames.length;
        for (int i = 0; i < size; i++) {
            TouchData touchData = new TouchData();
            touchData.name = itemNames[i];
            touchData.action = itemActions[i];
            list.add(touchData);
        }
        return list;
    }

    @Override
    public void onItemClick(View v, String action) {
        try {
            Class<?> clazz = Class.forName(action);
            Intent intent = new Intent(this, clazz);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

/*
* ViewRootImpl测量
* 1.在WindowManager调用addView方法时候开始创建ViewRootImpl对象,然后调用ViewRootImpl的setView方法
* 2.ViewRootImpl在performTraversals中或者measureHierarchy方法中将会调用getRootMeasureSpec计算出DecorView所
* 需要的宽高值。它的计算规则是--->
* (1)WindowManager.LayoutParams为MATCH_PARENT:测量大小为屏幕大小,测量模式为EXACTLY。
* (2)WindowManager.LayoutParams为WRAP_CONTENT:测量大小为屏幕大小,测量模式为AT_MOST。
* (3)WindowManager.LayoutParams为确切值:测量大小为确切值,测量模式为EXACTLY。
* */

/*
* FrameLayout测量
* 1.对于ViewGroup的容器View来说,都是先测量孩子,在测量自己。
* 2.在测量模式为EXACTLY,它的大小是测量值。在测量模式为AT_MOST,它的大小与孩子的最大测量值有关。如果孩子最大测量值大于它的
*   测量值,那么它的大小就是它的测量值。如果孩子最大测量值小于它的测量值,那么它的大小就是孩子的最大测量值。
* */

/*
* 疑惑:
* 1.Display中的宽高和Configuration中的宽高有何不同?
* 2.导航栏对于屏幕适配有和影响?
* 3.沉浸式中对于状态栏和导航栏的选择对DecorView的测量大小有和影响?
* 3.Window中的windowFixedWidthMajor、windowFixedWidthMinor、windowFixedHeightMajor、windowFixedHeightMinor
*   对DecorView的测量有什么影响?
* 4.getOutsets对DecorView的测量有什么影响?
* */


/*
* Activity的启动流程:(Android7.0)
* 1 Activity
*   1.1 Activity.startActivity
*   1.2 Activity.startActivityForResult
* 2 Instrumentation
*   2.1 Instrumentation.execStartActivity
* 3 ActivityManagerProxy
*   3.1 ActivityManagerProxy.startActivity ---> AMP经过binder IPC进入到AMN()
* 4 ActivityManagerNative
*   4.1 ActivityManagerNative.onTransact ---> AMN为抽象类,实现类为ActivityManagerService
* 5 ActivityManagerService
*   5.1 ActivityManagerService.startActivity --->(多了一个userId)
*   5.2 ActivityManagerService.startActivityAsUser
* 6 ActivityStarter & ActivityStackSupervisor(管理者)
*   6.1 ActivityStarter.startActivityMayWait
*       6.1.1 ActivityStackSupervisor.resolveIntent
*             6.1.1.1 PackageManagerService.resolveIntent
*             6.1.1.2 PackageManagerService.queryIntentActivitiesInternal
*       6.1.2 ActivityStackSupervisor.resolveActivity
*   6.2 ActivityStarter.startActivityLocked
*       6.2.1 ActivityManagerService.checkAppSwitchAllowedLocked
*       6.2.1 ActivityStarter.doPendingActivityLaunchesLocked
*   6.3 ActivityStarter.startActivityUnchecked
*       6.3.1 Launch Mode ---> 处理Activity四种启动模式
*       6.3.2 ActivityStack.startActivityLocked
*       6.3.3 ActivityStackSupervisor.resumeFocusedStackTopActivityLocked
*             6.3.3.1 ActivityStack.resumeTopActivityUncheckedLocked
*             6.3.3.2 ActivityStack.resumeTopActivityInnerLocked
*             6.3.3.3 ActivityStackSupervisor.pauseBackStacks
*             6.3.3.4 ActivityStack.startPausingLocked
*             6.3.3.5 ActivityStack.completePauseLocked
*       6.3.4 ActivityStackSupervisor.startSpecificActivityLocked
*             6.3.4.1 ActivityManagerService.startProcessLocked (如果进程不存在则创建进程)
*             6.3.4.2 ActivityManagerService.realStartActivityLocked
*       6.3.5 ApplicationThreadProxy.scheduleLaunchActivity
*       6.3.6 ApplicationThreadNative.onTransact
*       6.3.7 ApplicationThread.scheduleLaunchActivity
*       6.3.8 H.handleMessage
*       6.3.9 ActivityThread.handleLaunchActivity
*       6.3.10 ActivityThread.performLaunchActivity
*  总结核心过程
*      流程6.1 借助PackageManager来查询系统所有符合条件的Activity,包含多个符合条件的activity展示。
*      流程6.2 创建ActivityRecord对象,并检查App是否切换
*      流程6.3.1 找到或者创建任务栈
*      流程6.3.2 - 6.3.3 任务栈相关操作
*      流程6.3.4.1 目标进程是否存在,如果存在直接进入,如果不存在创建进程
*      流程6.3.5 - 6.3.6 通过binder调用进入目标程序
*      流程6.3.7 - 6.3.10 进入目标程序,开始发送消息。
* */

/*
* View刷新流程
* 1.View有关刷新的方法
* 2.刷新方法最终执行到了哪里
* 3.每个刷新的方法具体做了什么 ---> 执行过程
* 4.Choreographer的作用
* */

/*
* 调用invalidate可能出现这样的几种情况:
* 1.界面首次绘制还没有完成
* 2.界面首次绘制完成
* 3.上一次由invalidate方法的调用导致的重绘还没有完成
* 4.上一次由invalidate方法的调用导致的重绘已经完成
* */

/*
* mPrivateFlags标记位
* 1.PFLAG_DRAWN
*   (1)用来标记View是否执行过draw方法,执行过draw方法的View会被打上该标记。
*   (2)可以添加该标记使invalidate方法可以通过
* 2.PFLAG_HAS_BOUNDS
*   (1)用来标记是否执行过setFrame方法,执行过setFrame并且View的位置(左上右下)发生改变时的View会被打上该标记。
*   (2)值得注意的是View并没有相关代码去清除该标记。
* 3.PFLAG_INVALIDATED
*   (1)该标记表明当前View所占区域已经失效,需要重新绘制。
*   (2)对于已经打上该标记的View执行invalidate不会重新绘制。
*   (3)对于执行invalidate和requestLayout方法的View,都会被打上该标记。
*   (4)执行过draw方法
* 4.PFLAG_DRAWING_CACHE_VALID
*  (1)该标记表明当前View正在缓存的一些视图是有效的
*  (2)绘制完成后的View会被打上该标记
* 6.PFLAG_OPAQUE_BACKGROUND
*  (1)标记View的背景是否是实心的,背景实心的条件:a.背景不为空,b.背景是不透明的
*  (2)改标记时判断View是否实心的条件之一
* 7.PFLAG_OPAQUE_SCROLLBARS
*  (1)标记View的滚动条是否是实心的,滚动条实心的条件:a.没有滚动条,或者b.有滚动条但是滚动条是OVERLAY类型
*  (2)该标记是判断View是否是实心的条件之一
* 8.PFLAG_SKIP_DRAW
*  (1)标记View是否跳过绘制,对于ViewGroup来说,绘制的时候只会调用dispatchDraw
*  (2)该标记是会由WILL_NOT_DRAW以及背景和前景共同决定的。具体的逻辑为:a.如果View没有打上WILL_NOT_DRAW的标记,那么
*     View就不会被打上PFLAG_SKIP_DRAW标记,b.如果View有WILL_NOT_DRAW的标记,接下来会判断是否有背景,如果没有背景,
*     那么View就不会被打上PFLAG_SKIP_DRAW的标记。
*
*     10000001 00000000 00001000 00110000
*               1100000 00000000 00000000
* */