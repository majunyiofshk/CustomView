package com.mjy.customview.drag;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * ViewDragHelper的使用
 */

public class DragView extends ViewGroup {

    public static final String TAG = "DragView";
    private ViewDragHelper mDragHelper;
    private int mCenterSize;
    private static final int CENTER_SIZE = 100;

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        final float density = context.getResources().getDisplayMetrics().density;
        mCenterSize = (int) (CENTER_SIZE * density);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragCallback());
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
        ViewDragHelper dragHelper = ViewDragHelper.create(this, 1.0f, new DragCallback());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isCenter(event)){
            mDragHelper.processTouchEvent(event);
        }
        return true;
    }

    private boolean isCenter(MotionEvent event){
//        int y = (int) event.getY();
//        //边界
//        int top = (getHeight() - mCenterSize) / 2;
//        int bottom = (getHeight() + mCenterSize) / 2;
//        Log.e(TAG, "y = " + y + ", top = " + top + ", bottom = " + bottom);
//        return y > top && y < bottom;
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量孩子
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        //测量自己,在测量子View,每个子View大小为父容器的大小。
        final int parentMeasureWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int parentMeasureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(parentMeasureWidth, parentMeasureHeight);
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        final int parentMeasureWidth = MeasureSpec.getSize(parentWidthMeasureSpec);
        final int parentMeasureHeight = MeasureSpec.getSize(parentHeightMeasureSpec);
        final int childWidthMeasureSpec =
                MeasureSpec.makeMeasureSpec(parentMeasureWidth, MeasureSpec.EXACTLY);
        final int childHeightMeasureSpec =
                MeasureSpec.makeMeasureSpec(parentMeasureHeight, MeasureSpec.EXACTLY);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int childCount = getChildCount();
        int childLeft = getLeft();
        int childTop = getTop();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final int width = child.getMeasuredWidth();
            final int height = child.getMeasuredHeight();
            child.layout(childLeft, childTop, childLeft + width, childTop + height);
            childTop += height;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)){
            invalidate();
        }
    }

    /**
     *
     */
    private class DragCallback extends ViewDragHelper.Callback {

        /**
         * 考虑到一种特殊情况,当几个View重叠的时候,这时只想对下面的View做处理,那么就需要改变对应的索引值。
         *
         * @param index 子View索引
         * @return 子View的索引
         */
        @Override
        public int getOrderedChildIndex(int index) {
            return super.getOrderedChildIndex(index);
        }

        /**
         * 告诉ViewDragHelper是否捕获该View
         *
         * @param child     此时手指触摸的View
         * @param pointerId 手指id
         * @return 表示是否捕获该View
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
//            return indexOfChild(child) == 1;
            return true;
        }

        /**
         * 当拖动状态发生改变的时候被回调
         *
         * @param state 状态
         */
        @Override
        public void onViewDragStateChanged(int state) {
        }

        /**
         * 当View的位置发生改变的时候被回调
         *
         * @param changedView 被拖拽的View
         * @param left        改变后View的left坐标
         * @param top         改变后View的top坐标
         * @param dx          X轴方向移动的距离
         * @param dy          Y轴方向移动的距离
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        }

        /**
         * 当View被捕获的时候被回调
         *
         * @param capturedChild   捕获的View
         * @param activePointerId 手指id
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
        }

        /**
         * 当View被释放的时候回调
         *
         * @param releasedChild 释放的View
         * @param xvel          X轴方向上的速度
         * @param yvel          Y轴方向上的速度
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final int height = releasedChild.getHeight();
            final int top = releasedChild.getTop();
            float movePercent = (height + top) / ((float) height);
            Log.e(TAG, "movePercent = " + movePercent + ", yvel = " + yvel);
            int finalTop = yvel > 0 ? height : 0;
            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), finalTop);
            invalidate();
        }

        /**
         * 当边界被触摸的时候回调,需要注意一点的是该方法的被调用并不代表onEdgeDragStarted方法也能被调用
         * ,考虑到这样一种情况,当某个子View也处于此时拖动的边界中,那么系统回去选择处理该子View,而不是处理
         * 边界。
         *
         * @param edgeFlags 上、下、左、右四个边界
         * @param pointerId 手指id
         */
        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            Log.e(TAG, "onEdgeTouched ---> edgeFlags = " + edgeFlags + ", pointerId = " + pointerId);
        }

        /**
         * 控制某一边界触摸是否有用,换言之就是影响onEdgeDragStarted函数是否被回调。
         * 当手指在某一边界的时候,系统会判断手指滑动的方向和onEdgeLock方法的返回值来决定是否锁住该边界。
         *
         * @param edgeFlags 上、下、左、右四个边界
         * @return 确定某个边界是否可用
         */
        @Override
        public boolean onEdgeLock(int edgeFlags) {
            Log.e(TAG, "onEdgeLock ---> edgeFlags = " + edgeFlags);
            return true;
        }

        /**
         * 当边界开始拖动的时候被回调,在方法中你可以决定捕捉那个View
         *
         * @param edgeFlags 上、下、左、右四个边界
         * @param pointerId 手指id
         */
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            //只捕获遮罩层
            mDragHelper.captureChildView(getChildAt(1), pointerId);
        }

        /**
         * 该方法有两个作用:
         * 1.判断捕获的View在竖直方向上是否可以滚动
         * 2.用于计算Scroller的duration
         *
         * 对于作用1,想象一种情况,一开始手指触摸屏幕的位置不在任何子View上,然后手指在屏幕上滑动的时候进入了
         * 某个子View的位置,这时该方法就会被回调。
         *
         * 对于作用2,只能调用了ViewDragHelper的settleCapturedViewAt或者smoothSlideViewTo方法的时候才会调用
         * 此方法。
         *
         * @param child 捕获的View
         * @return View水平方向可以滚动的距离
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return 0;
        }

        /**
         * 同getViewHorizontalDragRange
         */
        @Override
        public int getViewVerticalDragRange(View child) {
            Log.e(TAG, "getViewVerticalDragRange ---> child = " + child);
            return super.getViewVerticalDragRange(child);
        }

        /**
         * 获取水平方向位移后的位置(在这里可以做边界限定)
         *
         * @param child 捕获的View
         * @param left  移动后的位置
         * @param dx    移动的距离
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            final int leftBound = getPaddingLeft();
//            final int rightBound = getWidth() - child.getWidth() - getPaddingRight();
//            return Math.min(Math.max(left, leftBound), rightBound);
            return 0;
        }

        /**
         * 同clampViewPositionHorizontal
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //改变捕捉的子View
            mDragHelper.captureChildView(getChildAt(1), 0);

            final int topBound = 0;
            final int bottomBound = getHeight();
            return Math.min(Math.max(top, topBound), bottomBound);
        }
    }
}

/*
* 待解决:触摸中心的位置才能滑动(重写ViewDragHelp还是...)
*
* */
