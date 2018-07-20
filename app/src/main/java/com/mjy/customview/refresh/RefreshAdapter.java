package com.mjy.customview.refresh;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mjy.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据
 */

public class RefreshAdapter extends RecyclerView.Adapter<RefreshViewHolder> {

    private List<String> mList;

    public RefreshAdapter() {
        createData();
    }

    private void createData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("条目" + (i + 1));
        }
        mList = list;
    }

    @Override
    public RefreshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_refresh, parent, false);
        return new RefreshViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RefreshViewHolder holder, int position) {
        holder.mTextView.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}
