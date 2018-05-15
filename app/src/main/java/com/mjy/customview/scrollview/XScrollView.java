package com.mjy.customview.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.OverScroller;

/**
 * 控件可以滑动的原理与处理
 */

public class XScrollView extends FrameLayout {
    private static final String TAG = "XScrollView";

    private static final int INVALID_POINTER = -1;
    private int mActivePointerId = INVALID_POINTER;

    private int mLastPoint;

    private VelocityTracker mVelocityTracker;

    private boolean mIsBeingDragged = false;

    private OverScroller mScroller;

    private int mTouchSlop;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;
    private int mOverDistance = 500;

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
        initVelocityTrackerIfNotExists();

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
                mActivePointerId = ev.getPointerId(0);
                break;

            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    break;
                }

                final int y = (int) ev.getY(activePointerIndex);
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
                    mVelocityTracker.addMovement(ev);
                    mLastPoint = y;
                    overScrollBy(0, deltaY, 0, getScrollY(), 0, range,
                            0, mOverDistance, true);
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                    int initialVelocity = (int) velocityTracker.getYVelocity(mActivePointerId);
                    if (Math.abs(initialVelocity) > mMinimumFlingVelocity) {
                        handleFling(-initialVelocity);
                        Log.e(TAG, "是否fling");
                    } else if (mScroller.springBack(getScrollX(), getScrollY(), 0, 0,
                            0, getScrollRange())) {
                        Log.e(TAG, "是否回弹");
                        postInvalidateOnAnimation();
                    }
                    mActivePointerId = INVALID_POINTER;
                    endDrag();
                }
                break;
        }
        return true;
    }

    private void endDrag() {
        mIsBeingDragged = false;

        recycleVelocityTracker();
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
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
    protected boolean overScrollBy(int deltaX, int deltaY,
                                   int scrollX, int scrollY,
                                   int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY,
                                   boolean isTouchEvent) {
        //对fling没有产生阻尼效果
        if ((scrollY >= -mOverDistance && scrollY < 0) ||
                (scrollY > scrollRangeY && scrollY <= scrollRangeY + mOverDistance)){
            deltaY = (int) (deltaY / 2.0f + 0.5f);
            Log.e(TAG, "deltaY = " + deltaY);
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (!mScroller.isFinished()) {
            final int oldX = getScrollX();
            final int oldY = getScrollY();
            setScrollX(scrollX);
            setScrollY(scrollY);
            onScrollChanged(scrollX, scrollY, oldX, oldY);
            invalidate();
        } else {
            super.scrollTo(scrollX, scrollY);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            final int oldX = getScrollX();
            final int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            final int range = getScrollRange();

            /*
            * fling情况下和回弹时才能执行滚动
            * */
            if (oldX != x || oldY != y || (y >= -mOverDistance && y < 0)
                    || (y > range && y <= range + mOverDistance)) {
//                Log.e(TAG, "处理fling, oldY = " + oldY + ", y = " + y);
                overScrollBy(x - oldX, y - oldY, oldX, oldY,
                        0, range, 0, mOverDistance, false);
            }
        }
    }

    @Override
    protected int computeVerticalScrollRange() { //在于ScrollBar高度的计算,超过返回后ScrollBar的高度会变小作为一个提示
        final int count = getChildCount();
        final int contentHeight = getHeight() - getPaddingBottom() - getPaddingTop();
        if (count == 0) {
            return contentHeight;
        }

        int scrollRange = getChildAt(0).getBottom();
        final int scrollY = getScrollY();
        final int overScrollBottom = Math.max(0, scrollRange - contentHeight);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else if (scrollY > overScrollBottom) {
            scrollRange += scrollY - overScrollBottom;
        }
        return scrollRange;
    }

    @Override
    protected int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    private void handleFling(int velocityY) {
        final int y = getScrollY();
        final boolean canFling = (y >= -mOverDistance || velocityY > 0) &&
                (y <= getScrollRange() + mOverDistance || velocityY < 0);
        if (canFling) {
            fling(velocityY);
        }
    }

    private void fling(int velocityY) {
        if (getChildCount() > 0) {
            mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, 0,
                    0, getScrollRange(), 0, mOverDistance);
            postInvalidateOnAnimation();
        }
    }
}

/*
* View是怎么滚动的,从ScrollView开滚动控件的处理?
* 1.滚动控件的测量策略
* 2.滚动控件的摆放测量
* 3.滚动控件的触摸事件处理
* */

/*
* 疑惑
* 1.OverScroller中fling参数的理解。
* 2.OverScroller中springBack的作用以及参数的理解
* 3.EdgeEffect的作用以及理解。
* */

/*
* 1.理解fling中的可以超过滚动的范围
*   当使用了这个方法,那么值得计算会超过滚动的范围,超过滚动范围后的值又会回到滚动范围的临界值。
* */