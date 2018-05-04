package com.mjy.customview.picture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 手势View
 */

public class XImageView extends ImageView {

    private GestureDetector mDetector;

    public interface OnBackListener {
        void onBack();
    }

    public static class LitOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        private XImageView imageView;

        public LitOnGestureListener(XImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //单击事件,退出
            imageView.back();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            //长按事件,显示Dialog
            super.onLongPress(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //双击事件,图片变大
            imageView.handleDoubleTap(e);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //滚动过程,图片伴随移动
            imageView.handleScroll(e1, e2, distanceX, distanceY);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //fling事件,原图退出,大图fling
            imageView.handleFling(e1, e2, velocityX, velocityY);
            return true;
        }
    }

    private static final String TAG = "XImageView";
    private OnBackListener mOnBackListener;

    public XImageView(Context context) {
        this(context, null);
    }

    public XImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initXImageView(context);
    }

    private void initXImageView(Context context) {
        original = true;
        setScaleType(ScaleType.FIT_CENTER);
        mDetector = new GestureDetector(context, new LitOnGestureListener(this));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDetector.onTouchEvent(event)) {
            return true;
        }
        final int actionMasked = MotionEventCompat.getActionMasked(event);
        switch (actionMasked) {
            case MotionEvent.ACTION_UP:
                //回到原位置的前提:处于拖动状态 && 原图样式
                if (mIsScrolled && original) {
                    Matrix matrix = new Matrix(getImageMatrix());
                    Log.e(TAG, "抬手事件 = " + matrix.toShortString());
                    checkScaleType();
                    matrix.postTranslate(-mTotalDistanceX, -mTotalDistanceY);
                    setImageMatrix(matrix);
                    mIsScrolled = false;
                    mTotalDistanceX = 0;
                    mTotalDistanceY = 0;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setOnBackListener(OnBackListener listener) {
        this.mOnBackListener = listener;
    }

    /**
     * 关闭显示图片的界面
     */
    private void back() {
        if (mOnBackListener != null) {
            mOnBackListener.onBack();
        }
    }

    private float lastX;
    private boolean original = true;

    private void handleDoubleTap(MotionEvent e) {
        Matrix matrix = new Matrix(getImageMatrix());
        checkScaleType();
        if (original) {
            //变大,记录变大时的坐标点
            lastX = e.getX();
            matrix.postScale(computeScale(), computeScale(), lastX, getHeight() / 2.0f);
        } else {
            //变小
            matrix.postScale(1.0f / computeScale(), 1.0f / computeScale(), lastX, getHeight() / 2.0f);
        }
        setImageMatrix(matrix);
        original = !original;
    }

    private boolean mIsScrolled;
    private float mTotalDistanceX;
    private float mTotalDistanceY;

    private void handleScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        //滑动操作处理的前提: 竖直方向滑动|| 变大操作
        if (Math.abs(distanceY) > Math.abs(distanceX) || !original) {
            mIsScrolled = true;
        }
        if (mIsScrolled) {
            Matrix matrix = new Matrix(getImageMatrix());
            checkScaleType();
            matrix.postTranslate(-distanceX, -distanceY);
            mTotalDistanceX += -distanceX;
            mTotalDistanceY += -distanceY;
            Log.e(TAG, "mTotalDistanceX = " + mTotalDistanceX + ", mTotalDistanceY = " + mTotalDistanceY);
            Log.e(TAG, "handleScroll = " + matrix.toShortString());
            setImageMatrix(matrix);
        }
    }

    private void handleFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
        if (original){
            back();
        }else {
            //滑翔

        }
    }

    /**
     * @return 缩放率
     */
    private float computeScale() {
        //得到原图的宽高,得到屏幕的大小
        float displayHeight = getHeight();
        float displayWidth = getWidth();
        float drawableHeight = getDrawable().getIntrinsicHeight();
        float drawableWidth = getDrawable().getIntrinsicWidth();
        return displayHeight * drawableWidth / (displayWidth * drawableHeight);
    }

    private void checkScaleType() {
        if (getScaleType() != ScaleType.MATRIX) {
            setScaleType(ScaleType.MATRIX);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        computeScale();
    }
}

/*
* 1.多点触控
*  (1)放大(在滑动过程中不能通过多手指进行放大)
* 2.单点触控
*  (1)随着手指移动
*  (2)图片变小(位置向下,向上大小不变)
*  (3)背景透明度的变化
*  (4)activity退出
*  (5)长按出现Dialog
*  (6)双击变大
*  (7)点击退出
* */

/*
* 自定义ImageView还是自定义ViewGroup
* 选择自定义ImageView原因
* 1.上诉多个功能都是基于图片进行变化的
* 2.方便与宿主Activity进行交互
*
* 实现功能的核心是GestureDetector与ScaleGestureDetector
* */

/*
* 1.让父类ImageView的ScaleType始终为Matrix模式。
* 2.在执行onDraw()方法前重组成ImageView真实的BaseMatrix。
* 3.对图片所做的变化存储在SupportMatrix中。
* 4.绘制时的Matrix为DrawMatrix,它是由BaseMatrix和SupportMatrix
* */