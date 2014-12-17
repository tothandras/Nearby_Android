package com.andrastoth.nearby.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrastoth.nearby.R;
import com.andrastoth.nearby.common.CircleTransformation;
import com.andrastoth.nearby.data.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> dataset;
    private Context context;

    public UserAdapter(List<User> dataset) {
        if (dataset == null) {
            throw new IllegalArgumentException("dataset must not be null");
        }
        this.dataset = dataset;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        private ImageView imageView;
        private OnItemClickListener onItemClickListener;

        private ViewHolder(View itemView) {
            super(itemView);
        }

        public ViewHolder(View v, OnItemClickListener onItemClickListener) {
            super(v);
            textView = ButterKnife.findById(v, R.id.list_item_profile_name);
            imageView = ButterKnife.findById(v, R.id.list_item_profile_picture);
            imageView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getPosition());
            }
        }

        public static interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.friends_list_item, parent, false);
        ViewHolder.OnItemClickListener onItemClickListener = new ViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                User user = dataset.get(position);
                user.setSelected(!user.isSelected());
            }
        };
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.getTextView().setText(dataset.get(position).getName());
        CircleTransformation circleTransformation = new CircleTransformation();
        Picasso.with(context)
                .load(dataset.get(position).getPicture())
                .noPlaceholder()
                .transform(circleTransformation)
                .into(viewHolder.getImageView());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
