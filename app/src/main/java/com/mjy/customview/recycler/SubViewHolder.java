package com.mjy.customview.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mjy.customview.R;

/**
 *
 */

public class SubViewHolder extends RecyclerView.ViewHolder {
    public TextView mTvContent;

    public SubViewHolder(View itemView) {
        super(itemView);
        mTvContent = (TextView) itemView.findViewById(R.id.tv_sub_item);
    }
}
