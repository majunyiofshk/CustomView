package com.mjy.customview.touch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mjy.customview.R;

/**
 *
 */

public class TouchViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextView;

    public TouchViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.tv_item);
    }
}
