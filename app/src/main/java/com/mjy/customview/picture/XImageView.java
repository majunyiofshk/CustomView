package com.mjy.customview.picture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.mjy.customview.utils.DisplayUtils;

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
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //滚动过程,图片伴随移动
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //fling事件,退出
            imageView.back();
            return true;
        }
    }

    private static final String TAG = "XImageView";
    private OnBackListener mOnBackListener;
    private boolean original;

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
    private float lastY;

    private void handleDoubleTap(MotionEvent e) {
        Matrix matrix = new Matrix(getImageMatrix());
        Log.e(TAG, "matrix = " + matrix.toShortString());
        setScaleType(ScaleType.MATRIX);
        if (original) {
            //变大,记录变大时的坐标点
            lastX = e.getX();
            lastY = e.getX();
            matrix.postScale(2.0f, 2.0f, 100, 100);
        } else {
            //变小
            matrix.postScale(0.5f, 0.5f, 100, 100);
        }
        Log.e(TAG, "setImageMatrix = " + matrix.toShortString());
        setImageMatrix(matrix);
        original = !original;
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
        Log.e(TAG, "屏幕高 = " + displayHeight + ", 图片高 = " + drawableHeight + ", 屏幕宽 = "
                + displayWidth + ", 图片高 = " + drawableWidth);
        return displayHeight / drawableHeight;
    }

//    @Override
//    public Matrix getImageMatrix() {
//        Matrix imageMatrix = super.getImageMatrix();
//        Log.e(TAG, "getImageMatrix = " + imageMatrix.toShortString());
//        return imageMatrix;
//    }


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
* [1.35, 0.0, 0.0]
* [0.0, 1.35, 583.57495]
* [0.0, 0.0, 1.0]
* */