package com.andrastoth.nearby.home;

import android.os.Bundle;

import com.andrastoth.nearby.R;
import com.andrastoth.nearby.base.BaseActivity;
import com.facebook.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class MainActivity extends BaseActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {
    private static final String TAG = "HomeActivity";
    private static final int LAYOUT = R.layout.main_activity;
    private static final int FRAGMENT_CONTAINER = R.id.fragment_container;

    @Inject
    MainFragment mainFragment;

    @Override
    protected List<Object> getModules() {
        List<Object> modules = Arrays.<Object>asList(new MainModule(this));
        modules.addAll(super.getModules());
        return modules;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(FRAGMENT_CONTAINER, mainFragment)
                    .commit();
        } else {
            mainFragment = (MainFragment) getSupportFragmentManager()
                    .findFragmentById(FRAGMENT_CONTAINER);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully.
     */
    @Override
    public void onConnected(Bundle bundle) {

    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}