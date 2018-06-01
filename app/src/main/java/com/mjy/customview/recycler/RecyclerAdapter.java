package com.mjy.customview.recycler;

import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mjy.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 总Adapter
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    public static final String[] texts = new String[]{"第一页", "第二页", "第三页", "第四页", "第五页"
            , "第六页", "第七页", "第八页", "第九页",};
    public static final
    @ColorInt
    int[] colors = new int[]{R.color.rulerBackground,
            R.color.rulerGraduation, R.color.rulerGraduationText, R.color.rulerMiddleGraduation,
            R.color.itemPressed, R.color.textBlack, R.color.colorAccent, R.color.colorPrimary,
            R.color.itemPressed};

    private List<RecyclerData> datas;

    public RecyclerAdapter() {
        this.datas = createData();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_recycler, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final RecyclerData data = datas.get(position);
        holder.mFlRecycler.setBackgroundResource(data.color);
        holder.mTvRecycler.setText(data.text);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public static List<RecyclerData> createData() {
        List<RecyclerData> datas = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            RecyclerData data = new RecyclerData();
            data.text = texts[i];
            data.color = colors[i];
            datas.add(data);
        }
        return datas;
    }
}
