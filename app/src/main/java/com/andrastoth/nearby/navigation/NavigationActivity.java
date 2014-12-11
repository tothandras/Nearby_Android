package com.andrastoth.nearby.navigation;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.andrastoth.nearby.R;
import com.andrastoth.nearby.base.BaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
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
    public static final String FRIEND_NAME = "FriendName";

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
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);

        final GoogleMap.OnMyLocationChangeListener onLocationChangedListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng u = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(u, 15));
            }
        };

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Location myLocation = googleMap.getMyLocation();
                if (myLocation != null) {
                    LatLng u = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    if (!cameraPosition.target.equals(u)) {
                        googleMap.setOnMyLocationChangeListener(null);
                    } else {
                        googleMap.setOnMyLocationChangeListener(onLocationChangedListener);
                    }
                }
            }
        });

        googleMap.setOnMyLocationChangeListener(onLocationChangedListener);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(LAT_LNG);
        if (bundle != null) {
            float[] friendLocation = bundle.getFloatArray(FRIEND_LOCATION);
            String friendName = bundle.getString(FRIEND_NAME);
            if (friendLocation.length == 2 && friendName != null) {
                LatLng f = new LatLng(friendLocation[0], friendLocation[1]);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(f, 15));

                googleMap.addMarker(new MarkerOptions()
                        .title(friendName)
                        .snippet("Your friend is here.")
                        .position(f));
            }
        }
    }
}
