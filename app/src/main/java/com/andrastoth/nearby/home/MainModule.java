package com.andrastoth.nearby.home;

import android.content.Context;

import com.andrastoth.nearby.NearbyModule;
import com.andrastoth.nearby.base.BaseActivity;
import com.andrastoth.nearby.base.InjectActivityContext;
import com.andrastoth.nearby.data.DataModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This module represents objects which exist only for the scope of a single activity. We can
 * safely create singletons using the activity instance because the entire object graph will only
 * ever exist inside of that activity.
 */
@Module(
        injects = {
                MainActivity.class,
                MainFragment.class
        },
        includes = {
                DataModule.class
        },
        addsTo = NearbyModule.class,
        library = true
)
public class MainModule {
    private final BaseActivity activity;

    public MainModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    @Singleton
    public MainFragment provideLoginFragment() {
        return new MainFragment();
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