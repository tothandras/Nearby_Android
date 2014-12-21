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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;

import org.json.JSONException;
import org.json.JSONObject;

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

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                googleMap.setOnMyLocationChangeListener(onLocationChangedListener);
                return false;
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                googleMap.setOnMyLocationChangeListener(null);
                return false;
            }
        });

        googleMap.setOnMyLocationChangeListener(onLocationChangedListener);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String jsonData = bundle.getString("com.parse.Data");
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject geoPointJsonObject = jsonObject.getJSONObject("friendLocation");
                ParseGeoPoint geoPoint = new ParseGeoPoint(geoPointJsonObject.getDouble("latitude"), geoPointJsonObject.getDouble("longitude"));
                String friendName = jsonObject.getString("alert");
                LatLng f = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(f, 15));

                googleMap.addMarker(new MarkerOptions()
                        .title(friendName)
                        .snippet("Your friend is here.")
                        .position(f));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
