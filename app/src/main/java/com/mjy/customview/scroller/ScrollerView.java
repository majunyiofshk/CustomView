package com.mjy.customview.scroller;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Scroller用法
 */

public class ScrollerView extends AppCompatTextView {
    public ScrollerView(Context context) {
        super(context);
    }

    public ScrollerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initScrollerView();
    }

    private void initScrollerView() {

    }
}
