package com.mjy.customview.refresh;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mjy.customview.R;

/**
 * 搭建一个下拉刷新的View,支持众多列表View。
 */

public class RefreshActivity extends AppCompatActivity {

    private static final String TAG = "RefreshActivity";
    int a;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        initView();
        int[] nums = new int[]{1, 1, 1, 2, 2, 5, 8, 9, 9, 9};
        int len = removeDuplicates(nums);
        Log.e(TAG, "len = " + len);
        for (int i = 0; i < len; i++) {
            Log.e(TAG, "nums[" + i + "] = " + nums[i]);
        }
    }

    private void initView() {
        RecyclerView rvContent = (RecyclerView) findViewById(R.id.rv_content);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        rvContent.setLayoutManager(layout);
        rvContent.setAdapter(new RefreshAdapter());
        rvContent.addItemDecoration(new DividerItemDecoration(this, layout.getOrientation()));
//        findViewById(R.id.srl_refresh).scrollTo(0, -300);
    }

    public int removeDuplicates(int[] nums) {
        //顺序的数组,去除重复的元素,输出新数组的长度
        //需要一个变量记录不相等的数量
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int len = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] != nums[len]) {//从第一个元素开始比较,第一个元素的值不变
                nums[++len] = nums[i];
            }
        }
        return len + 1;
    }

}

/*
* 1.刷新控件的摆放方式?
* 2.手势的处理?
*
* a.scroll这种方式排除,当父容器开始滑动的时候,子控件都会滑动。
* b.padding值为负的这种方式可取,移动的时候改变padding值。
* c.本质就是控件的摆放与移动。
* d.摆放关系也只有两种,要么从上至下,要么覆盖。改变目标View的位置,可以使用scroller来改变
* e.参考NestedScrollView,触摸时的代码书写技巧与转换思路。
* */

/*
* 触摸控件的处理技巧
* 1.需要重写的方法:a.onInterceptTouchEvent;b.onTouchEvent;c.requestDisallowInterceptTouchEvent
* 2.必要的一些属性:速度追踪、最小滑动距离、最大速度、最小速度、当前控件的状态(是否处于拖动状态)
* 3.越界的处理,处理的方式。
* */

/*
* 嵌套滚动机制
* 预定:实现NestedScrollChild接口的View叫做child,实现NestedScrollParent接口的View叫做parent
* 1.child在ACTION_DOWN事件调用startNestedScroll()方法,该方法会回调parent的onStartNestedScroll()和
*   onNestedScrollAccepted()方法,onStartNestedScroll()方法的返回值决定parent在那些事件可以滑动。
* 2.
* */
// child1 ---> parent1 ---> parent2 ---> 顶层