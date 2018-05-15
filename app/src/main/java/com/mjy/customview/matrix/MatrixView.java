package com.mjy.customview.matrix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.mjy.customview.R;

import java.util.Arrays;

/**
 * Matrix
 */

public class MatrixView extends View implements View.OnClickListener {
    private static final String TAG = "MatrixView";
    private Matrix mMatrix;
    private int mViewWidth;
    private int mViewHeight;
    private Bitmap mBitmap;

    public MatrixView(Context context) {
        this(context, null);
    }

    public MatrixView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mMatrix = new Matrix();
        setOnClickListener(this);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_xx);
        //计算Matrix
//        mapPoints();
//        mapRadius();
//        mapRect();
//        setXxx();
//        postScale();
        setRectToRect();
    }

    /*
    * 开始: [1.35, 0.0, 0.0][0.0, 1.35, 655.57495][0.0, 0.0, 1.0](x移动)
    * */

    private void postScale() {
        Matrix matrix1 = new Matrix();
        matrix1.postScale(2.0f, 2.0f, 20, 20);
        Log.e(TAG, "matrix1 = " + matrix1.toShortString());

        Matrix matrix2 = new Matrix();
        matrix2.postScale(2.0f, 2.0f, 20, 20);
        matrix2.postScale(5.0f, 5.0f, 50, 50);
        Log.e(TAG, "matrix2 = " + matrix2.toShortString());

        Matrix matrix3 = new Matrix();
        matrix3.postScale(2.0f, 2.0f, 20, 20);
        matrix3.postScale(5.0f, 5.0f, 50, 50);
        matrix3.postScale(0.2f, 0.2f, 50, 50);
        Log.e(TAG, "matrix3 = " + matrix3.toShortString());

        Matrix matrix4 = new Matrix();
        matrix4.postScale(2.0f, 2.0f, 20, 20);
        matrix4.postScale(0.5f, 0.5f, 20, 20);
        Log.e(TAG, "matrix4 = " + matrix4.toShortString());
    }

    private void mapPoints() {
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 2.0f);
        final float[] dst = new float[6];
        final float[] src = new float[]{10, 20, 30, 40, 50, 60};
        Log.e(TAG, "before: dst = " + Arrays.toString(dst));
        Log.e(TAG, "before: src = " + Arrays.toString(src));
        matrix.mapPoints(dst, 0, src, 2, (src.length >> 1) - 1);
        Log.e(TAG, "after: dst = " + Arrays.toString(dst));
        Log.e(TAG, "after: src = " + Arrays.toString(src));
    }

    private void mapRadius() {
        Matrix matrix = new Matrix();
        matrix.setScale(1.0f, 3.0f);

        float radius = 100.0f;
        float result = 0;
        Log.e(TAG, "before: radius = " + radius);
        Log.e(TAG, "before: result = " + result);

        result = matrix.mapRadius(radius);
        Log.e(TAG, "after: radius = " + radius);
        Log.e(TAG, "after: result = " + result);
    }

    private void mapRect() {
        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        RectF dst = new RectF();
        RectF src = new RectF(10, 10, 50, 50);
        Log.e(TAG, "before: dst = " + dst.toString());
        Log.e(TAG, "before: src = " + src.toString());

        boolean isRect = matrix.mapRect(dst, src);
        Log.e(TAG, "after: dst = " + dst.toString());
        Log.e(TAG, "after: src = " + src.toString());
        Log.e(TAG, "after: isRect = " + isRect);

    }

    private void setXxx() {
        Matrix matrix1 = new Matrix();
        matrix1.setScale(1, 2);
        Log.e(TAG, "matrix1 = " + matrix1.toShortString());

        Matrix matrix2 = new Matrix();
        matrix2.setScale(1, 2);
        matrix2.setScale(1, 2);
        Log.e(TAG, "matrix2 = " + matrix2.toShortString());

        Matrix matrix3 = new Matrix();
        matrix3.setScale(1, 2, 100, 100);
        Log.e(TAG, "matrix3 = " + matrix3.toShortString());

        Matrix matrixA = new Matrix();
        matrixA.setScale(2.0f, 2.0f, 100, 100);
        Matrix matrixB = new Matrix();
        matrixB.setScale(3.0f, 3.0f, 100, 100);
        Matrix matrix4 = new Matrix();
        matrix4.setConcat(matrixA, matrixB);
        Log.e(TAG, "matrixA = " + matrixA.toShortString());
        Log.e(TAG, "matrixB = " + matrixB.toShortString());
        Log.e(TAG, "matrix4 = " + matrix4.toShortString());
    }

    private void setRectToRect(){
        Matrix matrix1 = new Matrix();
        RectF src = new RectF(0, 0, 50, 50);
        RectF dst = new RectF(0, 0, 150, 150);
        matrix1.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
        Log.e(TAG, "src = " + src.toString());
        Log.e(TAG, "dst = " + dst.toString());
        Log.e(TAG, "matrix1 = " + matrix1.toShortString());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        RectF src = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        RectF dst = new RectF(0, 0, mViewWidth, mViewHeight);
        // 核心要点
        mMatrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);

        // 根据Matrix绘制一个变换后的图片
        canvas.drawBitmap(mBitmap, mMatrix, new Paint());
//        Log.e(TAG, "图片宽 = " + mBitmap.getWidth() + ", 图片宽 = " + mBitmap.getHeight() +
//        "   控件宽 = " + mViewWidth + ", 控件高 = " + mViewHeight);
//        Log.e(TAG, "mMatrix = " + mMatrix.toShortString());
    }

    @Override
    public void onClick(View v) {
        invalidate();
    }
}

/*
* 对于canvas.getMatrix()方法的说明
* 1.在开启硬件加速时,该方法得到的是单位矩阵。
* 2.在关闭硬件加速时,该方法得到的是真实矩阵。
* 3.单独关闭View的硬件加速,得到的是单位矩阵。
*
* 对于View的onDraw()方法中Canvas.getMatrix()方法说明:
* 1.每一次可以对Canvas的Matrix进行变换,来达到一些图形的变换。
* 2.对于上一次onDraw方法中改变Canvas的Matrix并不会影响到下一次onDraw方法中Canvas的Matrix
* */

/*
* 对于Matrix.setRectToRect()方法的说明
* 对于一个单位矩阵来讲,无论你执行对它调用多少次post,set,pre方法,调用setRectToRect()方法后,
* 最终得到的结果一定是一样的。
* */

/*
* 对于Matrix.postScale()4个参数的方法说明:
* 表示相对于某个点进行缩放,由于缩放都是基于原点的,如果想对与某个点进行缩放,那么需要做以下三个操作,假设原来点坐标为(px,py)
* 1.把坐标原点移动到该缩放点
* 2.进行缩放
* 3.把坐标原点移动到原来的位置
* 公式:
* s(sx, sy, px, py) = T(px, py) * S(sx, sy) * T(-px, -py)
* */

/*
* 矩阵
* MSCALE_X  MSKEW_X   MTRANS_X
* MSKEW_Y   MSCALE_Y  MTRANS_Y
* MPERSP_0  MPEREP_1  MPERSP_2
* */

