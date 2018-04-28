package com.mjy.customview.touch;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.mjy.customview.R;

/**
 * 手势探测器
 */

public class ImageViewActivity extends AppCompatActivity {

    public static final String TAG = "ImageViewActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        init();
    }

    private void init() {
        ImageView ivImage = (ImageView) findViewById(R.id.iv_image);
        printDrawableSize(ivImage);
//        ivImage.post(new AfterRunnable(ivImage));

        //测试每个文件夹下图片的大小
        printBitmapSize(getResources(), R.drawable.test, "drawable");
        printBitmapSize(getResources(), R.drawable.test_m, "drawable-mdpi");
        printBitmapSize(getResources(), R.drawable.test_h, "drawable-hdpi");
        printBitmapSize(getResources(), R.drawable.test_x, "drawable-xhdpi");
        printBitmapSize(getResources(), R.drawable.test_xx, "drawable-xxhdpi");
        printBitmapSize(getResources(), R.drawable.test_xxx, "drawable-xxxhdpi");
    }

    /**
     * getIntrinsicWidth()和getIntrinsicHeight()获得的是图片的宽高是经过缩放后的宽高,
     * 并不是图片的原始宽高,在测绘前和测绘后的结果都是一样的。
     */
    private static void printDrawableSize(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Log.e(TAG, "DrawableSize ---> 宽：" + drawable.getIntrinsicWidth() +
                ", 高：" + drawable.getIntrinsicHeight());
    }

    /**
     * Drawable使用一个Rect来存储宽高信息的,必须在测绘后才能获得Rect的宽高信息。它的大小也是经过缩放后的大小。
     */
    private static void printDrawableRect(ImageView imageView) {
        Rect bounds = imageView.getDrawable().getBounds();
        Log.e(TAG, bounds.toString());
    }

    private static void printBitmapSize(Resources resources, @DrawableRes int drawableRes
            , String message) {
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawableRes);
        Log.e(TAG, "BitmapSize(" + message + ")---> 宽：" + bitmap.getWidth() +
                ", 高：" + bitmap.getHeight());
    }

    public static class AfterRunnable implements Runnable {

        private ImageView view;

        public AfterRunnable(ImageView view) {
            this.view = view;
        }

        @Override
        public void run() {
        }
    }
}

/*
* ImageView图片类型和大小与什么有关
* */

/*
* 疑惑1:View.post()方法怎么保证在View测绘完后执行
* 疑惑2:不同drawable文件夹下放置相同大小的图片,加载该图片时,图片的大小会改变。
* */

/*
* 疑惑1原因:
* View测绘其实是Handler中发送的一个消息,而View.post()方法中执行的Runnable也是被封装为一个消息被Handler发送的
* MessageQueue当中,只不过这个消息是在执行View测绘消息当中发送的一个消息,那么当View测绘消息完成后,就会执行Runnable
* 这个任务。
* */

/*
* 疑惑2原因:
* 加载图片之前系统会计算相应手机的dpi,然后根据dpi去找相应的drawable文件夹,如果该文件夹下没有相应的该图片,那么就会去别的
* 文件夹下去找,如果那张照片存在于底像素的drawable文件,那么系统就会认为你在高像素的手机下使用这张低像素的图片,那么系统就会
* 放大这张图片,反之则缩小。
* */