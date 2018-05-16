package com.mjy.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.OverScroller;

import com.mjy.customview.R;

/**
 * 仿薄荷卷尺
 */

public class RulerView extends View {
    public static final String TAG = "RulerView";

    private int mWidth;
    private int mHeight;
    private int mMinGraduation = 25; //最小刻度
    private int mMaxGraduation = 200; //最大刻度

    private OverScroller mScroller;
    private int mOverDistance; //超出的距离
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;

    private int mLastPoint;
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId = INVALID_POINTER;

    private boolean mIsBeingDragged;

    private float mSmallGraduationHeight;
    private float mBigGraduationHeight;
    private float mMiddleGraduationHeight;
    private int mSpace = 29;
    private int mTotalGraduation;

    private Paint mSmallGraduationPaint;
    private Paint mBigGraduationPaint;
    private Paint mMiddleGraduationPaint;
    private Paint mTextPaint;
    private Paint.FontMetrics mFontMetrics;

    private float mTextPaddingBottom = 39;

    public RulerView(Context context) {
        super(context);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initRulerView(context);
    }

    private void initRulerView(Context context) {
        mScroller = new OverScroller(context);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();

        mOverDistance = 2 * 10 * mSpace;

        mSmallGraduationPaint = new Paint();
        mSmallGraduationPaint.setStrokeWidth(2);
        mSmallGraduationPaint.setColor(context.getResources().getColor(R.color.rulerGraduation));

        mBigGraduationPaint = new Paint();
        mBigGraduationPaint.setStrokeWidth(4);
        mBigGraduationPaint.setColor(context.getResources().getColor(R.color.rulerGraduation));

        mMiddleGraduationPaint = new Paint();
        mMiddleGraduationPaint.setStrokeWidth(4);
        mMiddleGraduationPaint.setColor(context.getResources().getColor(R.color.rulerMiddleGraduation));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(40);
        mTextPaint.setColor(context.getResources().getColor(R.color.rulerGraduationText));
        mFontMetrics = mTextPaint.getFontMetrics();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        final float part = h / 5f;
        //计算小刻度高度
        mSmallGraduationHeight = part;
        //计算大刻度高度
        mBigGraduationHeight = 2 * part;
        //计算中间刻度高度
        mMiddleGraduationHeight = 3 * part;
        //一共多少个刻度
        mTotalGraduation = w / mSpace;

        mWidth = w;
        mHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();

        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mIsBeingDragged = !mScroller.isFinished()) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }

                if (mIsBeingDragged) {
                    mScroller.abortAnimation();
                }

                mLastPoint = (int) event.getX();
                mActivePointerId = event.getPointerId(0);
                break;

            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = event.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    break;
                }

                final int x = (int) event.getX(activePointerIndex);
                int deltaX = mLastPoint - x;
                final int range = getScrollRange();
                if (!mIsBeingDragged && Math.abs(deltaX) > mTouchSlop) {
                    mIsBeingDragged = true;
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }

                if (mIsBeingDragged) {
                    mVelocityTracker.addMovement(event);
                    mLastPoint = x;
                    overScrollBy(deltaX, 0, getScrollX(), 0, range, 0,
                            mOverDistance, 0, true);
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged){
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                    int initialVelocity = (int) velocityTracker.getXVelocity(mActivePointerId);
                    if (Math.abs(initialVelocity) > mMinimumFlingVelocity){
                        handleFling(-initialVelocity);
                    }else if (mScroller.springBack(getScrollX(), getScrollY(),
                            0, getScrollRange(), 0, 0)){
                        postInvalidateOnAnimation();
                    }

                    mActivePointerId = INVALID_POINTER;
                    endDrag();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged){
                    if (mScroller.springBack(getScrollX(), getScrollY(),
                            0, getScrollRange(), 0, 0)){
                        postInvalidateOnAnimation();
                    }
                }

                mActivePointerId = INVALID_POINTER;
                endDrag();

                break;
        }
        return true;
    }

    private void initVelocityTrackerIfNotExists(){
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

    private void endDrag(){
        mIsBeingDragged = false;

        recycleVelocityTracker();
    }

    private void handleFling(int velocityX){
        final int x = getScrollX();
        final boolean canFling = (x >= -mOverDistance || velocityX > 0) &&
                (x <= getScrollRange() + mOverDistance || velocityX < 0);
        if (canFling) {
            mScroller.fling(getScrollX(), getScrollY(), velocityX, 0, 0,
                    getScrollRange(), 0, 0, mOverDistance, 0);
            postInvalidateOnAnimation();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            final int oldX = getScrollX();
            final int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            final int range = getScrollRange();
            Log.e(TAG, "oldX = " + oldX + ", x = " + x);
            //超过滚动范围会出现 oldX = x 或者 oldY = y 的情况,需要特殊处理
            final boolean isOver = (x >= -mOverDistance && x < 0)
                    || (x > range && x <= range + mOverDistance);
            if (oldX != x || oldY != y) {
                overScrollBy(x - oldX, y - oldY, oldX, oldY,
                        range, 0, mOverDistance, 0, false);
            }else if (isOver){
                //跳过此次滚动,进行下一次的计算
                invalidate();
            }
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.scrollTo(scrollX, scrollY);
    }

    @Override
    protected int computeHorizontalScrollRange() {
        return getScrollRange();
    }

    private int getScrollRange() {
        //卷尺的总长加上控件的宽度
        return 10 * mSpace * (mMaxGraduation - mMinGraduation);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int x = getScrollX();
        final int saveCount = canvas.getSaveCount();
        canvas.save();
        canvas.translate(x , 0);

        final int total = mTotalGraduation + 1; //绘制的时候多绘制一个刻度,类似于ListView
        final int space = mSpace;
        final float smallGraduationHeight = mSmallGraduationHeight;
        final float bigGraduationHeight = mBigGraduationHeight;
        final int index = computeStartIndex();
        float startX = computeStartDistance();
        for (int i = index; i < total + index; i++) {
            if (i % 10 == 0) {
                //画大刻度
                canvas.drawLine(startX, 0, startX, bigGraduationHeight, mBigGraduationPaint);

                //画文字
                int textValue = i / 10 + mMinGraduation;
                if (textValue >= mMinGraduation && textValue <= mMaxGraduation){
                    String text = String.valueOf(textValue);
                    float textWidth = mTextPaint.measureText(text);
                    final float textX = startX - textWidth / 2.0f;
                    final float textY = mHeight - mTextPaddingBottom + mFontMetrics.bottom;
                    canvas.drawText(text, textX, textY, mTextPaint);
                }
            } else {
                //画小刻度
                canvas.drawLine(startX, 0, startX, smallGraduationHeight, mSmallGraduationPaint);
            }
            startX += space;
        }

        //画中间刻度
        final float middleX = mWidth / 2.0f;
        final float middleGraduationHeight = mMiddleGraduationHeight;
        canvas.drawLine(middleX, 0, middleX, middleGraduationHeight, mMiddleGraduationPaint);

        canvas.restoreToCount(saveCount);
    }

    /**
     * 用于计算绘制大刻度的索引值，需要考虑开始和末尾的大刻度在控件中间
     *
     * @return 索引值
     */
    private int computeStartIndex() {
        final int scrollX = getScrollX();
        final int space = mSpace;
        final int middle = mWidth / 2;
        final int index = (scrollX - middle) / space;
        return index;
    }

    /**
     * 用于计算绘制刻度的起始距离
     *
     * @return 起始距离
     */
    private int computeStartDistance() {
        final int scrollX = getScrollX();
        final int space = mSpace;
        final int middle = mWidth / 2 ;
        return (middle - scrollX) % space;
    }
}

/*
* 绘制的部分:
* 1.背景--->灰色
* 2.主体--->刻度,文字
* 3.
* */
