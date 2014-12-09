package com.andrastoth.nearby;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class NearbyApplication extends Application {
    private ObjectGraph applicationGraph;

    @Override public void onCreate() {
        super.onCreate();

        applicationGraph = ObjectGraph.create(getModules().toArray());
    }

    /**
     * A list of modules to use for the application graph. Subclasses can override this method to
     * provide additional modules provided they call {@code super.getModules()}.
     */
    protected List<Object> getModules() {
        return Arrays.<Object>asList(new NearbyModule(this));
    }

    public ObjectGraph getApplicationGraph() {
        return applicationGraph;
    }
}