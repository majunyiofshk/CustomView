package com.mjy.customview.refresh;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mjy.customview.R;

/**
 *
 */

public class RefreshViewHolder extends RecyclerView.ViewHolder {

    public TextView mTextView;

    public RefreshViewHolder(View itemView) {
        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.tv_refresh);
    }
}
