package com.andrastoth.nearby.friends;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrastoth.nearby.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> dataset;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }

    public MenuAdapter(List<String> dataset, OnItemClickListener listener) {
        if (dataset == null) {
            throw new IllegalArgumentException("dataset must not be null");
        }
        this.dataset = dataset;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;

        public ViewHolder(TextView v) {
            super(v);
            textView = v;
        }

        public TextView getTextView() {
            return textView;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_list_item, parent, false);
        TextView textView = ButterKnife.findById(view, android.R.id.text1);
        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.getTextView().setText(dataset.get(position));
        viewHolder.getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
