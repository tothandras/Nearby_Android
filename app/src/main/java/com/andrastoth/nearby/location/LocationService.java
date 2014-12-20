package com.andrastoth.nearby.location;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.andrastoth.nearby.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseFacebookUtils;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class LocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private static final String TAG = "LocationService";

    private static final int INTERVAL = 10000;
    private static final int DISPLACEMENT = 10;

    private static final String LOCATION = "location";

    Location currentLocation;

    GoogleApiClient googleApiClient;
    LocationRequest request;

    @Override
    public void onCreate() {
        super.onCreate();
        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));

        int code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (code == ConnectionResult.SUCCESS) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            request = LocationRequest.create().setInterval(INTERVAL).setSmallestDisplacement(DISPLACEMENT).setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            googleApiClient.connect();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        currentLocation = location;
        // Check if there is a currently logged in user
        // and it's linked to a Facebook account.
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            currentUser.put(LOCATION, new ParseGeoPoint(location.getLatitude(), location.getLongitude()));
            currentUser.saveInBackground();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onFailed");
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }
}
