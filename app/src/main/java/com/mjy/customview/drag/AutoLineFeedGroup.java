package com.mjy.customview.drag;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 自动换行的ViewGroup
 */

public class AutoLineFeedGroup extends ViewGroup {
    public AutoLineFeedGroup(Context context) {
        super(context);
    }

    public AutoLineFeedGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
