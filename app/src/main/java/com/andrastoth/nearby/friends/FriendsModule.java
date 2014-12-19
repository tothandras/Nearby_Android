package com.andrastoth.nearby.friends;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.andrastoth.nearby.NearbyModule;
import com.andrastoth.nearby.base.BaseActivity;
import com.andrastoth.nearby.base.InjectActivityContext;
import com.andrastoth.nearby.common.CommonModule;
import com.andrastoth.nearby.data.DataModule;
import com.andrastoth.nearby.data.User;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
@Module(
        injects = {
                FriendsActivity.class,
                FriendsListFragment.class,
                UserAdapter.class
        },
        includes = {
                DataModule.class,
                CommonModule.class
        },
        addsTo = NearbyModule.class,
        library = true
)
public class FriendsModule {
    private final BaseActivity activity;

    public FriendsModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    @Singleton
    public FriendsListFragment provideFriendsListFragment() {
        return new FriendsListFragment();
    }

    /**
     * Allow the activity context to be injected but require that it be annotated with
     * {@link com.andrastoth.nearby.base.InjectActivityContext @ForActivity} to explicitly differentiate it from application context.
     */
    @Provides
    @Singleton
    @InjectActivityContext
    public Context provideActivityContext() {
        return activity;
    }

    @Provides
    public RecyclerView.LayoutManager provideRecyclerViewLayoutManager() {
        return new LinearLayoutManager(activity);
    }

    @Provides
    public RecyclerView.Adapter provideRecyclerViewAdapter(List<User> dataset){
        return new UserAdapter(dataset);
    }
}