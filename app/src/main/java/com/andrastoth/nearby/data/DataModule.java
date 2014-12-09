package com.andrastoth.nearby.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
@Module(
        injects = {
                User.class
        },
        library = true
)
public class DataModule {
    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().serializeNulls().create();
    }

    @Provides
    @Singleton
    List<User> provideUserList() {
        return new ArrayList<User>();
    }

    @Provides
    User.Builder provideUserBuilder() {
        return new User.Builder();
    }
}
