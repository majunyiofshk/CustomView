package com.mjy.customview.touch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mjy.customview.R;

import java.util.List;

/**
 *
 */

public class TouchAdapter extends RecyclerView.Adapter<TouchViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(View v, String action);
    }

    private OnItemClickListener mListener;
    private List<TouchData> mList;

    public TouchAdapter(List<TouchData> list) {
        this.mList = list;
    }

    @Override
    public TouchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_touch, parent, false);
        return new TouchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TouchViewHolder holder, final int position) {
        holder.mTextView.setText(mList.get(position).name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(holder.itemView, mList.get(position).action);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
