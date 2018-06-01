package com.mjy.customview.recycler;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mjy.customview.R;

import java.util.List;

/**
 * 数据的调整,对于可以滑动的View来说,可以使用RecyclerView。
 */

public class MultiAdapter extends RecyclerView.Adapter<MultiViewHolder> {
    private List<MultiBean> mList;

    public MultiAdapter(List<MultiBean> list) {
        this.mList = list;
    }

    @Override
    public MultiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_multi, parent, false);
        return new MultiViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MultiViewHolder holder, int position) {
        RecyclerView itemView = (RecyclerView) holder.itemView;
        itemView.setBackgroundColor(mList.get(position).color);
        SubAdapter adapter = (SubAdapter) itemView.getAdapter();
        adapter.setData(mList.get(position));
        Log.e("MultiAdapter", "开始执行 onBindViewHolder ");
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}

//数据集, id, title, content
