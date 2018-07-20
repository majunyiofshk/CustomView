package com.mjy.customview.refresh;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;
import android.widget.TextView;

/**
 * 下拉刷新,
 */

public class RefreshLayout extends ViewGroup implements NestedScrollingParent {

    private static final String TAG = "RefreshLayout";
    private static final int INVALID_POINTER = -1;

    private View mTarget;
    private TextView mTextView;
    private boolean mIsBeingDragged;
    private boolean mNestedScrollUp;//是否开始Nest滚动
    private boolean mReturningToStart;//是否正在返回
    private int mLastMotionY;
    private float mInitialDownY;
    private float mInitialMotionY;
    private int mTouchSlop;
    private int mActivePointerId = INVALID_POINTER;
    private int mCurrentTargetOffsetTop;
    private int mOriginOffsetTop;
    private int mTotalUnconsumed;
    private int mOffsetTopToTarget = 60;
    private NestedScrollingParentHelper mNestedScrollingParentHelper;
    private OverScroller mScroller;

    public RefreshLayout(Context context) {
        super(context);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化相关参数
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

        mScroller = new OverScroller(context);

        createCustomView(context);
    }

    private void createCustomView(Context context) {
        mTextView = new TextView(context);
        mTextView.setText("下拉刷新");
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setBackgroundColor(Color.parseColor("#ff8888"));
        addView(mTextView);
    }

    private void ensureTarget() {
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(mTextView)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    public boolean canChildScrollUp() {
//        if (mChildScrollUpCallback != null) {
//            return mChildScrollUpCallback.canChildScrollUp(this, mTarget);
//        }
        return ViewCompat.canScrollVertically(mTarget, -1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }
        mTarget.measure(MeasureSpec.makeMeasureSpec(
                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(),
                MeasureSpec.EXACTLY));
        mTextView.measure(MeasureSpec.makeMeasureSpec(360, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(120, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }

        int customWidth = mTextView.getMeasuredWidth();
        int customHeight = mTextView.getMeasuredHeight();
        mTextView.layout((width - customWidth) / 2, -customHeight - mOffsetTopToTarget,
                (width + customWidth) / 2, -mOffsetTopToTarget);

        final View child = mTarget;
        final int childLeft = getPaddingLeft();
        final int childTop = getPaddingTop();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
    }

    /*------------------------NestedScrollingParent--------------------------*/

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onStartNestedScroll");
        return isEnabled() && !mReturningToStart
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        //父类准备向下,改变该状态
        mTotalUnconsumed = Math.abs(getScrollY());
        Log.e(TAG, "onNestedScrollAccepted ---> child = " + child + ", target = " + target + ", axes = " + axes);
        mNestedScrollUp = true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = dy - mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
            moveTarget(mTotalUnconsumed);
        }

    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        //这里的滑动先后顺序为:如果向下,孩子先滑动,一直到孩子不能滑动的时候在去滑动父亲;如果向上,父亲先滑动,等到父亲不能滑动
        //的时候再去滑动孩子。
        final int dy = dyUnconsumed;
        if (dy < 0 && !canChildScrollUp()) {
            mTotalUnconsumed += Math.abs(dyUnconsumed);
            moveTarget(mTotalUnconsumed);
        }
    }

    @Override
    public void onStopNestedScroll(View child) {
        Log.e(TAG, "onStopNestedScroll");
        mNestedScrollingParentHelper.onStopNestedScroll(child);
        mNestedScrollUp = false;
        if (mTotalUnconsumed > 0) {
            finishTarget(-mTotalUnconsumed);
            mTotalUnconsumed = Math.abs(getScrollY());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*
         * 注意几种状态, 正在下拉,正在刷新,刷新后回弹的过程,这个过程不拦截,滑动事件被拦截的原因。
         * 1.down事件--->滑动,未滑动
         * 2.move事件--->滑动,未滑动
         * 3.up事件--->清理工作
         * 4.针对上诉几种情况需要做哪些处理?  同时要考虑到它作为一个子View应该如何处理
         * */
        ensureTarget();

        final int action = MotionEventCompat.getActionMasked(ev);
        int pointIndex;

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }

//        if (action == MotionEvent.ACTION_DOWN){
//            Log.e(TAG, "canChildScrollUp = " + canChildScrollUp() + ", mNestedScrollUp = " +
//                    mNestedScrollUp + ", mReturningToStart = " + mReturningToStart);
//        }

        if (!isEnabled() || canChildScrollUp() || mNestedScrollUp || mReturningToStart) {
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;

                pointIndex = ev.findPointerIndex(mActivePointerId);
                if (pointIndex < 0) {
                    return false;
                }
                mInitialDownY = ev.getY(pointIndex);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }

                pointIndex = ev.findPointerIndex(mActivePointerId);
                if (pointIndex < 0) {
                    return false;
                }

                final float y = ev.getY(pointIndex);
                startDragging(y);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        return mIsBeingDragged;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ensureTarget();

        final int action = MotionEventCompat.getActionMasked(event);
        int pointIndex;

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }

        if (!isEnabled() || canChildScrollUp() || mNestedScrollUp || mReturningToStart) {
            return false;
        }


        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = event.getPointerId(0);
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE: {
                pointIndex = event.findPointerIndex(mActivePointerId);
                if (pointIndex < 0) {
                    return false;
                }

                final float y = event.getY(pointIndex);
                startDragging(y);

                if (mIsBeingDragged) {
                    final float overScrollTop = y - mInitialMotionY; //这里可以乘以一个系数
                    if (overScrollTop > 0) {
                        moveTarget((int) overScrollTop);
                    } else {
                        return false;
                    }
                }
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN:
                pointIndex = MotionEventCompat.getActionIndex(event);
                if (pointIndex < 0) {
                    return false;
                }
                mActivePointerId = event.getPointerId(pointIndex);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;

            case MotionEvent.ACTION_UP: {
                pointIndex = event.findPointerIndex(mActivePointerId);
                if (pointIndex < 0) {
                    return false;
                }

                if (mIsBeingDragged) {
                    final float y = event.getY(pointIndex);
                    final float overScrollTop = y - mInitialMotionY;
                    mIsBeingDragged = false;
                    finishTarget((int) overScrollTop);
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }

            case MotionEvent.ACTION_CANCEL:
                return false;
        }
        return true;
    }

    private void startDragging(float y) {
        final float yDiff = y - mInitialDownY;
        if (yDiff > mTouchSlop && !mIsBeingDragged) {
            mIsBeingDragged = true;
            mInitialMotionY = mInitialDownY + mTouchSlop;
            //开始拖动的准备工作
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private void moveTarget(int moveY) {
        //计算offset,最初的高度减去当前的总移动值
//        ViewCompat.offsetTopAndBottom(mTarget, moveY - mCurrentTargetOffsetTop);
//        mCurrentTargetOffsetTop = mTarget.getTop();
        final int scrollY = getScrollY();
        final int deltaY = moveY - scrollY;
        overScrollBy(0, deltaY, 0, getScrollY(), 0, 240, 0, 0, true);
    }

    private void finishTarget(int moveY) {
//        final int finishDistance = Math.abs(mCurrentTargetOffsetTop - mOriginOffsetTop);
//        ViewCompat.offsetTopAndBottom(mTarget, -finishDistance);
//        mCurrentTargetOffsetTop = mTarget.getTop();
        if (mScroller.springBack(getScrollX(), getScrollY(), 0, 0, 0, 240)) {
            mReturningToStart = true;
            postInvalidateOnAnimation();//有一个bug,滑动的时候再次触摸会出现什么情况
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.scrollTo(scrollX, -scrollY);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset() && mReturningToStart) {
            final int oldX = Math.abs(getScrollX());
            final int oldY = Math.abs(getScrollY());
            final int x = Math.abs(mScroller.getCurrX());
            final int y = Math.abs(mScroller.getCurrY());
            //回弹
            if (oldX != x || oldY != y) {
                overScrollBy(x - oldX, y - oldY, oldX, oldY, 0, 240, 0, 0, false);
            } else {
                postInvalidateOnAnimation();
            }
        }
    }
}

/*
* NestedScrolling ---> 子View在滑动的时候
* 触摸阶段
* 滑动的时候移动Target
* */
