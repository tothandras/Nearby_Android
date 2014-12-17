package com.andrastoth.nearby.common;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
@Module(
        injects = {
                CircleTransformation.class
        },
        library = true
)
public class CommonModule {
    @Singleton
    @Provides
    public CircleTransformation provideCircleTransformation() {
        return new CircleTransformation();
    }
}