package com.mjy.customview.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mjy.customview.R;

/**
 * 总ViewHolder
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView mTvRecycler;
    public FrameLayout mFlRecycler;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mTvRecycler = (TextView) itemView.findViewById(R.id.tv_recycler);
        mFlRecycler = (FrameLayout) itemView.findViewById(R.id.fl_recycler);
    }
}
