package com.andrastoth.nearby.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Assumes LocationService is a registered service
        Intent serviceStartIntent = new Intent(context, LocationService.class);
        context.startService(serviceStartIntent);
    }
}
