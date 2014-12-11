package com.andrastoth.nearby.home;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrastoth.nearby.R;
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            this.textView = ButterKnife.findById(v, R.id.list_item_profile_name);
            this.imageView = ButterKnife.findById(v, R.id.list_item_profile_picture);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.friends_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.textView.setText(dataset.get(position).getName());
        Picasso.with(context)
                .load(dataset.get(position).getPicture())
                .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                .tag(context)
                .into(viewHolder.imageView);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User clicked = dataset.get(position);
                clicked.setSelected(!clicked.isSelected());
                if (clicked.isSelected()) {
                    ((ImageView) v).setColorFilter(context.getResources().getColor(R.color.accent), PorterDuff.Mode.OVERLAY);
                } else {
                    ((ImageView) v).clearColorFilter();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
