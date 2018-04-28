package com.mjy.customview.view;

import android.support.v4.view.NestedScrollingParent;
import android.view.View;

/**
 * NestedScrollingParent相关方法解析
 */

public class NestScrollingParentView implements NestedScrollingParent {
    /**
     * 对应startNestedScroll, 内控件通过调用外控件的这个方法来确定外控件是否接收滑动信息
     *
     * @param child  外控件的直接子View
     * @param target 发起嵌套滚动的内控件
     * @param nestedScrollAxes
     * @return 返回true表示当前控件接受本次嵌套滚动(也就是onNestedScrollAccepted会被调用)
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return false;
    }

    /**
     * 当外控件确定接收滑动信息后该方法被回调, 可以让外控件针对嵌套滑动做一些前期工作
     *
     * @param child
     * @param target
     * @param nestedScrollAxes
     */
    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {

    }

    /**
     *  对应stopNestedScroll, 用来做一些收尾工作
     *
     * @param target
     */
    @Override
    public void onStopNestedScroll(View target) {

    }

    /**
     * 关键方法, 接收内控件处理完滑动后的滑动距离信息, 在这里外控件可以选择是否处理剩余的滑动距离
     *
     * @param target 发起嵌套滚动的内控件
     * @param dxConsumed 当前View消耗的水平距离px
     * @param dyConsumed 当前View消耗的竖直距离px
     * @param dxUnconsumed 当前View没有消耗的距离px
     * @param dyUnconsumed 当前View没有消耗的距离px
     */
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    /**
     * 关键方法, 接收内控件处理滑动前的滑动距离信息, 在这里外控件可以优先响应滑动操作, 消耗部分或者全部滑动距离
     *
     * @param target
     * @param dx 水平滚动的距离px
     * @param dy 竖直滚动的距离px
     * @param consumed 如果不为null,consumed[0]代表水平滚动需要消耗的距离px,consumed[1]代表竖直方向需要消耗的距离px
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    /**
     * 返回当前嵌套滚动的父容器应该在水平还是竖直方向滚动
     *
     * @return
     */
    @Override
    public int getNestedScrollAxes() {
        return 0;
    }
}
