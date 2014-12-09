package com.andrastoth.nearby;

import android.content.Context;
import android.location.LocationManager;

import com.andrastoth.nearby.base.InjectApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
@Module(library = true)
public class NearbyModule {
    private final NearbyApplication application;

    public NearbyModule(NearbyApplication application) {
        this.application = application;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link com.andrastoth.nearby.base.InjectApplicationContext @ForApplication} to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @InjectApplicationContext
    Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    LocationManager provideLocationManager() {
        return (LocationManager) application.getSystemService(LOCATION_SERVICE);
    }
}
