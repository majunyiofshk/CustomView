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
    private OverScroller mScroller;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;

    private int mLastPoint;
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId = INVALID_POINTER;

    private boolean mIsBeingDragged;

    private int mWidth;
    private int mHeight;

    private float mSmallGraduationHeight;
    private float mBigGraduationHeight;
    private float mMiddleGraduationHeight;
    private int mSpace = 30;
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
                            0, 0, true);
                }
                break;

//            case MotionEvent.ACTION_UP:
//                if (mIsBeingDragged){
//                    final VelocityTracker velocityTracker = mVelocityTracker;
//                    velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
//                    int initialVelocity = (int) velocityTracker.getXVelocity(mActivePointerId);
//                    if (Math.abs(initialVelocity) > mMinimumFlingVelocity){
//                        Log.e(TAG, "是否fling?");
//                        handleFling(-initialVelocity);
//                    }
//                }
//                break;
        }
        return true;
    }

    private void initVelocityTrackerIfNotExists(){
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void handleFling(int velocityX){
        final int x = getScrollX();
        final boolean canFling = (x >= 0 || velocityX > 0) &&
                (x <= getScrollRange() || velocityX < 0);
        if (canFling) {
            mScroller.fling(getScrollX(), getScrollY(), velocityX, 0, 0, getScrollRange(), 0, 0);
            postInvalidateOnAnimation();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            Log.e(TAG, "computeScroll --->是否执行?");
            final int oldX = getScrollX();
            final int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            final int range = getScrollRange();

            if (oldX != x || oldY != y ) {
                overScrollBy(x - oldX, y - oldY, oldX, oldY,
                        range, 0, 0, 0, false);
            }
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (!mScroller.isFinished()) {
            //fling情况
            final int oldX = getScrollX();
            final int oldY = getScrollY();
            onScrollChanged(scrollX, scrollY, oldX, oldY);
            super.scrollTo(scrollX, scrollY);
//            invalidate();
        } else {
            Log.e(TAG, "onOverScrolled ---> 是否执行?");
            super.scrollTo(scrollX, scrollY);
        }
    }

    private int getScrollRange() {
        //卷尺的总长加上控件的宽度
        return mSpace * 10 * 200 + mWidth; //200
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "onDraw ---> 是否执行");
//        final int x = getScrollX();
//        final int saveCount = canvas.getSaveCount();
//        canvas.save();
//        canvas.translate(getScrollX(), 0);

        //画刻度:大刻度、小刻度、中间刻度
        final int total = mTotalGraduation;
        final int space = mSpace;
        final float smallGraduationHeight = mSmallGraduationHeight;
        final float bigGraduationHeight = mBigGraduationHeight;
        //i值从0到9循环变化,startX从0-30循环变化
        final int index = computeStartIndex();
        int startX = computeStartDistance();
        Log.e(TAG, "index = " + index + ", startX = " + startX + ", scrollerX = " + getScrollX());
        for (int i = index; i < total + index; i++) {
            if (i % 9 == 0) {
                //画大刻度
                canvas.drawLine(startX, 0, startX, bigGraduationHeight, mBigGraduationPaint);

                //画文字
                String text = String.valueOf(i / 9);
                float textWidth = mTextPaint.measureText(text);
                final float textX = startX - textWidth / 2.0f;
                final float textY = mHeight - mTextPaddingBottom + mFontMetrics.bottom;
                canvas.drawText(text, textX, textY, mTextPaint);
            } else {
                //画小刻度
                canvas.drawLine(startX, 0, startX, smallGraduationHeight, mSmallGraduationPaint);
            }
            startX += space;
        }

        //画中间刻度
        final float middleX = mWidth / 2.0f + getScrollX();
        final float middleGraduationHeight = mMiddleGraduationHeight;
        canvas.drawLine(middleX, 0, middleX, middleGraduationHeight, mMiddleGraduationPaint);

//        canvas.restoreToCount(saveCount);
    }

    private int computeStartIndex() {
        final int scrollX = getScrollX();
        final int space = mSpace;
        return scrollX / space;
    }

    private int computeStartDistance() {
        final int scrollX = getScrollX();
        final int space = mSpace;
        return scrollX % space;
    }
}

/*
* 绘制的部分:
* 1.背景--->灰色
* 2.主体--->刻度,文字
* 3.
* */
