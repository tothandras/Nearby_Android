package com.andrastoth.nearby.home;

import android.os.Bundle;

import com.andrastoth.nearby.R;
import com.andrastoth.nearby.base.BaseActivity;
import com.facebook.AppEventsLogger;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class MainActivity extends BaseActivity  {
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
}