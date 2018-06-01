package com.mjy.customview.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mjy.customview.R;

/**
 * 子RecyclerView的Adapter
 */

public class SubAdapter extends RecyclerView.Adapter<SubViewHolder> {
    private MultiBean mBean;

    public SubAdapter(MultiBean bean) {
        this.mBean = bean;
    }

    public void setData(MultiBean bean){
        this.mBean = bean;
        notifyDataSetChanged();
    }

    @Override
    public SubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub, parent, false);
        return new SubViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SubViewHolder holder, int position) {
        holder.mTvContent.setText(mBean.content.get(position));
    }

    @Override
    public int getItemCount() {
        return mBean == null ? 0 : mBean.content.size();
    }
}
