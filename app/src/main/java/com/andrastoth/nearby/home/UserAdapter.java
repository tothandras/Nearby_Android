package com.andrastoth.nearby.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrastoth.nearby.R;
import com.andrastoth.nearby.base.InjectActivityContext;
import com.andrastoth.nearby.data.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEWTYPE_USER_NAME = 0;
    private static final int VIEWTYPE_USER_PICTURE = 1;

    @Inject
    @InjectActivityContext
    Context context;

    private List<User> dataset;

    public UserAdapter(List<User> dataset) {
        if (dataset == null) {
            throw new IllegalArgumentException("dataset must not be null");
        }
        this.dataset = dataset;
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextViewHolder(TextView textView) {
            super(textView);
            this.textView = textView;
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ImageViewHolder(ImageView imageView) {
            super(imageView);
            this.imageView = imageView;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_item, parent, false);
        TextView textView = ButterKnife.findById(view, R.id.list_item_profile_name);
        ImageView imageView = ButterKnife.findById(view, R.id.list_item_profile_picture);
        switch (viewType) {
            case VIEWTYPE_USER_NAME: return new TextViewHolder(textView);
            case VIEWTYPE_USER_PICTURE: return new ImageViewHolder(imageView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEWTYPE_USER_NAME:
                TextViewHolder textViewHolder = (TextViewHolder) holder;
                textViewHolder.textView.setText(dataset.get(position).getName());
                break;
            case VIEWTYPE_USER_PICTURE:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                Picasso.with(context)
                        .load(dataset.get(position).getPicture())
                        .placeholder(R.drawable.com_facebook_profile_picture_blank_portrait)
                        .tag(context)
                        .into(imageViewHolder.imageView);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
