package com.mjy.customview.view;

import android.support.v4.view.NestedScrollingChild;

/**
 * NestedScrollingChild相关方法解析
 */

public class NestedScrollingChildView implements NestedScrollingChild {
    /**
     * 设置当前View是否可以支持嵌套滚动
     */
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {

    }

    /**
     * 表示当前View是否可以嵌套滚动
     */
    @Override
    public boolean isNestedScrollingEnabled() {
        return false;
    }

    /**
     * 起始方法, 主要作用是找到接收滑动距离信息的外控件
     *
     * @param axes 滚动的方向
     * @return 在该方向是否可以嵌套滚动
     */
    @Override
    public boolean startNestedScroll(int axes) {
        return false;
    }

    /**
     *  结束方法, 主要作用就是清空嵌套滑动的相关状态
     */
    @Override
    public void stopNestedScroll() {

    }

    /**
     * 表示当前View是否有嵌套滚动的父容器
     */
    @Override
    public boolean hasNestedScrollingParent() {
        return false;
    }

    /**
     *  在内控件处理完滑动后把剩下的滑动距离信息分发给外控件
     *
     * @param dxConsumed 当前View所消耗的水平滚动距离px
     * @param dyConsumed 当前View所消耗的竖直滚动距离px
     * @param dxUnconsumed 当前View没有消耗的水平滚动距离px
     * @param dyUnconsumed 当前View没有消耗的竖直滚动距离px
     * @param offsetInWindow 如果该事件被分发将会返回true
     * @return
     */
    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return false;
    }

    /**
     * 在内控件处理滑动前把滑动信息分发给外控件
     *
     * @param dx 水平滚动的距离px
     * @param dy 竖直滚动的距离px
     * @param consumed 如果不为null,consumed[0]代表水平滚动需要消耗的距离px,consumed[1]代表竖直方向需要消耗的距离px
     * @param offsetInWindow
     * @return 如果父容器消耗了部分或者全部滚动距离,那么将会返回true
     */
    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return false;
    }

    /**
     *
     *
     * @param velocityX
     * @param velocityY
     * @param consumed
     * @return
     */
    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    /**
     * 在当前View消耗fling事件之前,向父容器进行分发
     *
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return false;
    }
}

/*
* 嵌套滚动的使用流程:
* 嵌套滚动是由子控件发起的,一般是在子控件的onTouchEvent方法中进行相应方法的回调
* 1.第一步:子控件在down事件中调用startNestedScroll并传入滚动方向,startNestedScroll的实现为:(1).判断是否有可嵌套滚动
* 的外控件(减少后续的递归调用),(2).判断子控件是否可以嵌套滚动(setNestedScrollingEnabled需要提前设置),(3).调用外控件的
* onStartNestedScroll,直至找到可以嵌套滚动的外控件(onStartNestedScroll的返回值影响着后续事件的派发)
*
* 2.第二步:子控件在move事件中计算已经滑动的距离并且调用dispatchNestedPreScroll方法,dispatchNestedPreScroll方法的
* 实现为:(1).判断子控件是否可以嵌套滚动并且是否有可以嵌套滚动的外控件,(2).如果两个条件都满足,初始化consumed数组中的元素为
* 0,并且调用外控件的onNestedPreScroll,在该方法中需要自己去实现消耗多少滚动距离并且把消耗的距离复制给数组,最后会判断外控件
* 是否消耗了滚动距离,如果消耗了就返回true,否则返回false(3).如果其中一个不满足,那么就会返回false。注意dispatchNestedPreScroll
* 的返回值,如果为true,代表着外控件消耗了一部分滚动距离,那么子控件需要把这部分消耗的距离给减去。
*
* 3.第三步:外控件滑动结束后,子控件继续滑动,子控件滑动结束后,调用子控件的dispatchNestedScroll,把子控件已经消耗的滚动距离
* 和剩余未消耗的滚动距离当做参数传递,接着调用外控件的onNestedScroll方法(可以在此方法中处理滑动),在up事件或者合适的时间中
* 调用stopNestedScroll方法。
* */

/*
* 滑动事件中刷新控件的疑惑:
* 1.move事件的回调频率
* 2.在move事件中刷新View的频率和move事件回调事件的平衡
* 3.滑动速度过快的处理方式
* 4.fling事件的处理
* 5.滑动的距离与Scroller的处理的结合
* */

/*
* 1.只有移动距离大于某个距离的时候才算滑动:configuration.getScaledTouchSlop()
* 2.拥有一个全局变量去表示当前View是否处于滑动状态
* 3.
* */
