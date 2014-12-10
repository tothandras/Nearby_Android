package com.andrastoth.nearby.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class LocationScheduleReceiver extends BroadcastReceiver {

    // 30 seconds
    private static final long REPEAR_TIME = 1000 * 30;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Assumes LocationService is a registered service
        Intent serviceStartIntent = new Intent(context, LocationService.class);
//        serviceStartIntent.putExtra(LocationService.USER_ID, );
        context.startService(serviceStartIntent);
    }
}
