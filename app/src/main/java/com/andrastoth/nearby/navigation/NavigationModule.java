package com.andrastoth.nearby.navigation;

import android.content.Context;

import com.andrastoth.nearby.NearbyModule;
import com.andrastoth.nearby.base.BaseActivity;
import com.andrastoth.nearby.base.InjectActivityContext;
import com.andrastoth.nearby.data.DataModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
@Module(
        injects = {
                NavigationActivity.class
        },
        includes = {
                DataModule.class
        },
        addsTo = NearbyModule.class,
        library = true
)
public class NavigationModule {
    private final BaseActivity activity;

    public NavigationModule(BaseActivity activity) {
        this.activity = activity;
    }

    /**
     * Allow the activity context to be injected but require that it be annotated with
     * {@link com.andrastoth.nearby.base.InjectActivityContext @ForActivity} to explicitly differentiate it from application context.
     */
    @Provides
    @Singleton
    @InjectActivityContext
    Context provideActivityContext() {
        return activity;
    }
}
