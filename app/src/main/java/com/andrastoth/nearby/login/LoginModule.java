package com.andrastoth.nearby.login;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.andrastoth.nearby.NearbyModule;
import com.andrastoth.nearby.base.BaseActivity;
import com.andrastoth.nearby.base.InjectActivityContext;
import com.andrastoth.nearby.common.CommonModule;
import com.andrastoth.nearby.data.DataModule;
import com.andrastoth.nearby.data.User;
import com.andrastoth.nearby.login.LoginActivity;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
@Module(
        injects = {
                LoginActivity.class
        },
        addsTo = NearbyModule.class,
        library = true
)
public class LoginModule {
    private final BaseActivity activity;

    public LoginModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    @Singleton
    @InjectActivityContext
    public Context provideActivityContext() {
        return activity;
    }
}