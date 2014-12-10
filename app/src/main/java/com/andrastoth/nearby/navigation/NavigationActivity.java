package com.andrastoth.nearby.navigation;

import android.content.Intent;
import android.os.Bundle;

import com.andrastoth.nearby.R;
import com.andrastoth.nearby.base.BaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class NavigationActivity extends BaseActivity implements OnMapReadyCallback {
    private static final String TAG = "NavigationFragment";
    private static final int LAYOUT = R.layout.navigation_activity;
    private static final int MAP_FRAGMENT = R.id.map;

    public static final String LAT_LNG = "LatLng";
    public static final String FRIEND_LOCATION = "FriendLocation";
    public static final String USER_LOCATION = "UserLocation";

    @Override
    protected List<Object> getModules() {
        List<Object> modules = Arrays.<Object>asList(new NavigationModule(this));
        modules.addAll(super.getModules());
        return modules;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        ButterKnife.inject(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(MAP_FRAGMENT);

        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Intent intent = getIntent();

        LatLng sydney = new LatLng(-33.867, 151.206);

        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        googleMap.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
    }
}
