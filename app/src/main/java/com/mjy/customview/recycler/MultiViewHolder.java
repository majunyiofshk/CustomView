package com.mjy.customview.recycler;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ViewHolder,处理子RecyclerView
 */

public class MultiViewHolder extends RecyclerView.ViewHolder {

    public MultiViewHolder(View itemView) {
        super(itemView);
        RecyclerView rvItem = (RecyclerView) itemView;
        GridLayoutManager layout = new GridLayoutManager(itemView.getContext(), 4);
        rvItem.setLayoutManager(layout);
        SubAdapter adapter = new SubAdapter(null);
        rvItem.setAdapter(adapter);
    }
}
