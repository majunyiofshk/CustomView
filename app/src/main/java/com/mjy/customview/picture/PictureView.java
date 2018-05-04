package com.mjy.customview.picture;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 *
 */

public class PictureView extends ImageView {

    private static final String TAG = "PictureView";
    private ScaleType pendingScaleType;
    private ScaleType mScaleType = ScaleType.FIT_CENTER;
    private Matrix mBaseMatrix = new Matrix(); //不同ScaleType所对应的Matrix(基于ScaleType.MATRIX)
    private Matrix mOperateMatrix = new Matrix(); //手指操作形成的Matrix
    private Matrix mDrawMatrix = new Matrix(); //正在绘制时的Matrix
    private final float[] mMatrixValues = new float[9];
    private GestureDetector mDetector;

    private GestureDetector.OnGestureListener mOnGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    //单击事件,退出

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
                    final float scale = getScale(mOperateMatrix);
                    final float x = e.getX();
                    final float y = e.getY();
                    mOperateMatrix.setScale(scale, scale, x, y);
                    applyMatrix();
                    Log.e(TAG, "双击事件");
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    //滚动过程,图片伴随移动
                    mOperateMatrix.postTranslate(-distanceX, -distanceY);
                    applyMatrix();
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    //fling事件,原图退出,大图fling

                    return true;
                }
            };

    public PictureView(Context context) {
        this(context, null);
    }

    public PictureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPictureView(context);
    }

    private void initPictureView(Context context) {
        super.setScaleType(ScaleType.MATRIX);
        if (pendingScaleType != null) {
            setScaleType(pendingScaleType);
        }

        mDetector = new GestureDetector(context, mOnGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (pendingScaleType == null) {
            pendingScaleType = scaleType;
        } else {
            mScaleType = scaleType;
            updateBaseMatrix();
        }
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        if (pendingScaleType != null){
            updateBaseMatrix();
        }
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        if (pendingScaleType != null){
            updateBaseMatrix();
        }
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        if (pendingScaleType != null){
            updateBaseMatrix();
        }
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        final boolean change = super.setFrame(l, t, r, b);
        updateBaseMatrix();
        return change;
    }


    private float getScale(Matrix matrix) {
        matrix.getValues(mMatrixValues);
        float scale = mMatrixValues[Matrix.MSCALE_X];
        return scale == 1.0f ? 2.0f : 1.0f;
    }

    private void applyMatrix() {
        if (checkMatrix()) {
            setImageViewMatrix(getDrawMatrix());
        }
    }

    private boolean checkMatrix() {
        final RectF rect = getDisplayRect(getDrawMatrix());
        if (rect == null) {
            return false;
        }

        final float height = rect.height(), width = rect.width();
        float deltaX = 0, deltaY = 0;

        final int viewHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        if (height <= viewHeight) {
            switch (mScaleType) {
                case FIT_START:
                    deltaY = -rect.top;
                    break;
                case FIT_END:
                    deltaY = viewHeight - height - rect.top;
                    break;
                default:
                    deltaY = (viewHeight - height) / 2 - rect.top;
                    break;
            }
        } else if (rect.top > 0) {
            deltaY = -rect.top;
        } else if (rect.bottom < viewHeight) {
            deltaY = viewHeight - rect.bottom;
        }

        final int viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        if (width <= viewWidth) {
            switch (mScaleType) {
                case FIT_START:
                    deltaX = -rect.left;
                    break;
                case FIT_END:
                    deltaX = viewWidth - width - rect.left;
                    break;
                default:
                    deltaX = (viewWidth - width) / 2 - rect.left;
                    break;
            }
//            mScrollEdge = EDGE_BOTH;
        } else if (rect.left > 0) {
//            mScrollEdge = EDGE_LEFT;
            deltaX = -rect.left;
        } else if (rect.right < viewWidth) {
            deltaX = viewWidth - rect.right;
//            mScrollEdge = EDGE_RIGHT;
        } else {
//            mScrollEdge = EDGE_NONE;
        }

        // Finally actually translate the matrix
        mOperateMatrix.postTranslate(deltaX, deltaY);
        return true;
    }

    private RectF getDisplayRect(Matrix matrix) {
        Drawable d = getDrawable();
        if (d != null) {
            RectF displayRect = new RectF();
            displayRect.set(0, 0, d.getIntrinsicWidth(),
                    d.getIntrinsicHeight());
            matrix.mapRect(displayRect);
            return displayRect;
        }
        return null;
    }

    private void updateBaseMatrix() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        final float viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        final float viewHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        final float drawableWidth = drawable.getIntrinsicWidth();
        final float drawableHeight = drawable.getIntrinsicHeight();

        mBaseMatrix.reset();

        final float widthScale = viewWidth / drawableWidth;
        final float heightScale = viewHeight / drawableHeight;

        if (mScaleType == ScaleType.CENTER) {
            mBaseMatrix.postTranslate(Math.round((viewWidth - drawableWidth) * 0.5f),
                    Math.round((viewHeight - drawableHeight) * 0.5f));
        } else if (mScaleType == ScaleType.CENTER_CROP) {
            float scale = Math.max(widthScale, heightScale);
            mBaseMatrix.postScale(scale, scale);
            mBaseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2F,
                    (viewHeight - drawableHeight * scale) / 2F);
        } else if (mScaleType == ScaleType.CENTER_INSIDE) {
            float scale = Math.min(1.0f, Math.min(widthScale, heightScale));
            mBaseMatrix.postScale(scale, scale);
            mBaseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2F,
                    (viewHeight - drawableHeight * scale) / 2F);
        } else {
            RectF tempSrc = new RectF(0, 0, drawableWidth, drawableHeight);
            RectF tempDst = new RectF(0, 0, viewWidth, viewHeight);
            switch (mScaleType) {
                case FIT_CENTER:
                    mBaseMatrix.setRectToRect(tempSrc, tempDst, Matrix.ScaleToFit.CENTER);
                    break;

                case FIT_START:
                    mBaseMatrix.setRectToRect(tempSrc, tempDst, Matrix.ScaleToFit.START);
                    break;

                case FIT_END:
                    mBaseMatrix.setRectToRect(tempSrc, tempDst, Matrix.ScaleToFit.END);
                    break;

                case FIT_XY:
                    mBaseMatrix.setRectToRect(tempSrc, tempDst, Matrix.ScaleToFit.FILL);
                    break;
            }
        }

        resetMatrix();
    }

    private void resetMatrix() {
        mOperateMatrix.reset();
        setImageViewMatrix(getDrawMatrix());
//        TODO:检查边界
    }

    private Matrix getDrawMatrix() {
        mDrawMatrix.set(mBaseMatrix);
        mDrawMatrix.postConcat(mOperateMatrix);
        return mDrawMatrix;
    }

    private void setImageViewMatrix(Matrix matrix) {
        super.setImageMatrix(matrix);
    }
}
