package com.andrastoth.nearby;

import android.app.Application;

import com.parse.Parse;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class NearbyApplication extends Application {
    private static String TAG = "NearbyApplication";

    private ObjectGraph applicationGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationGraph = ObjectGraph.create(getModules().toArray());

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        String PARSE_APPLICATION_ID = getResources().getString(R.string.parse_app_id);
        String PARSE_CLIENT_KEY = getResources().getString(R.string.parse_client_key);

        Parse.initialize(this, PARSE_APPLICATION_ID, PARSE_CLIENT_KEY);
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