package com.andrastoth.nearby.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.andrastoth.nearby.R;
import com.andrastoth.nearby.base.BaseActivity;
import com.andrastoth.nearby.base.InjectApplicationContext;
import com.andrastoth.nearby.friends.FriendsActivity;
import com.andrastoth.nearby.location.LocationReceiver;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private static final int LAYOUT = R.layout.login_activity;

    private Dialog progressDialog;

    @Inject
    @InjectApplicationContext
    Context context;

    @Override
    protected List<Object> getModules() {
        List<Object> modules = Arrays.<Object>asList(new LoginModule(this));
        modules.addAll(super.getModules());
        return modules;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        ButterKnife.inject(this);

        // Check if there is a currently logged in user
        // and it's linked to a Facebook account.
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            saveUser();
            sendLoggedInBroadcast();
            showFriendsActivity();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    private void saveUser() {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground();
    }

    private void sendLoggedInBroadcast() {
        Intent i = new Intent(context, LocationReceiver.class);
        i.setAction("com.tothandras.nearby.LOGGED_IN");
        sendBroadcast(i);
    }

    private void showFriendsActivity() {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

    public void onLoginClick(View view) {
        progressDialog = ProgressDialog.show(LoginActivity.this, "", "Logging in...", true);

        List<String> permissions = Arrays.asList("public_profile", "email", "user_friends");

        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                progressDialog.dismiss();
                if (user == null) {
                    Log.d(TAG, "The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d(TAG, "User signed up and logged in through Facebook!");
                    saveUser();
                    sendLoggedInBroadcast();
                    showFriendsActivity();
                } else {
                    Log.d(TAG, "User logged in through Facebook!");
                    saveUser();
                    sendLoggedInBroadcast();
                    showFriendsActivity();
                }
            }
        });
    }
}
