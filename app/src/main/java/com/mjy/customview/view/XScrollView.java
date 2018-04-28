package com.mjy.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * 控件可以滑动的原理与处理
 */

public class XScrollView extends FrameLayout {
    private int mLastPoint;
    private OverScroller mScroller;
    private int mTouchSlop;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;
    private boolean mIsBeingDragged;

    public XScrollView(Context context) {
        this(context, null);
    }

    public XScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initXScrollView();
    }

    private void initXScrollView() {
        mScroller = new OverScroller(getContext());
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width);
        final int usedTotal = getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin +
                heightUsed;
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(0, MeasureSpec.getSize(parentHeightMeasureSpec) - usedTotal),
                MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (getChildCount() == 0) {
                    return false;
                }
                if (mIsBeingDragged = !mScroller.isFinished()) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastPoint = (int) ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                final int y = (int) ev.getY();
                int deltaY = mLastPoint - y;
                final int range = getScrollRange();
                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                    mIsBeingDragged = true;
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
                    mLastPoint = y;
                    overScrollBy(0, deltaY, 0, getScrollY(), 0, range, 0, 0, true);
                }
                break;

            case MotionEvent.ACTION_UP:
                //处理fling事件
                break;
        }
        return true;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.e("Parent --->", "draw");
    }

    private int getScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0,
                    child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
        }
        return scrollRange;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.scrollTo(scrollX, scrollY);
    }
}

/*
* View是怎么滚动的,从ScrollView开滚动控件的处理?
* 1.滚动控件的测量策略
* 2.滚动控件的摆放测量
* 3.滚动控件的触摸事件处理
* */

/*
* ScrollView触摸事件处理:
*
* ScrollView中的OverScroller:
*  1.OverScroller是用来将一段距离在一定的时间范围内拆分为若干个距离,也就是说在一定的时间内只滑动一部分的距离。给人的一种感觉
*    就是在滑动。
*  2.创建OverScroller的时候mFinished为true,调用startScroll时mFinished为false。在duration时间过后或者手动调用
*    abortAnimation(),mFinished为true。
*
* 1.MotionEvent.ACTION_DOWN:
*  (1)如果没有子View就不滑动,不滑动的处理就是返回值为false
*  (2)如果正在滑动,请求父容器不要拦截。(不理解)
*  (3)如果正在滑动,证明上一次的滑动还在进行,也就是fling效果。当手指再次触碰屏幕时,需要停止fling。停止fling的处理就是调用
*     Scroller.abortAnimation()。
*  (4)记录这一次的手触摸的位置,以便move事件使用。
*
* 2.MotionEvent.ACTION_MOVE:
*  (1)如果不是滑动状态,就判断滑动距离是否小于默认值,如果是就,就忽略此次操作。
*  (2)如果是滑动状态,就开始计算手指滑动的距离,调用overScrollBy()的方法开始滑动,
* 3.
* */