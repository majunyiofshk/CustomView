package com.mjy.customview.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * 屏幕相关工具类
 */

public class DisplayUtils {

    /**
     * 获取屏幕的分辨率(整个)
     *
     * @param activity
     * @return 包装屏幕分辨率的点, x代表width, y代表height
     */
    public static Point getDisplaySize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);
        return point;
    }

    /**
     * 获取屏幕的分辨率(不包括状态栏和导航栏)
     */
    public static Point getDisplayNoStatusBarAndNavigationBar(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        final int width = dipToPx(context, configuration.screenWidthDp);
        final int height = dipToPx(context, configuration.screenHeightDp);
        return new Point(width, height);
    }

    /**
     * 获取屏幕的分辨率(不包括导航栏)
     */
    public static Point getDisplaySizeNoNavigationBar(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return new Point(metrics.widthPixels, metrics.heightPixels);
    }

    /**
     * dip转px
     */
    public static int dipToPx(Context context, int dip){
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (displayMetrics.density * dip + 0.5f);
    }

    /**
     * px转dip
     */
    public static int pxToDip(Context context, int px){
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (px / displayMetrics.density + 0.5f);
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context){
        Resources resources = context.getResources();
        int resId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resId);
    }
}
