package com.andrastoth.nearby.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andrastoth.nearby.R;
import com.andrastoth.nearby.base.BaseFragment;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class MainFragment extends BaseFragment {
    private static final String TAG = "MainFragment";
    private static final int LAYOUT = R.layout.main_fragment;

    @InjectView(R.id.authButton)
    protected LoginButton authButton;

    @InjectView(R.id.authImage)
    protected ImageView authImage;

    @InjectView(R.id.authFragment)
    ViewGroup authFragment;

    @InjectView(R.id.friendsFragment)
    ViewGroup friendsFragment;

    private UiLifecycleHelper uiHelper;
    private GraphUser user;

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    private LoginButton.UserInfoChangedCallback userInfoChangedCallback = new LoginButton.UserInfoChangedCallback() {
        @Override
        public void onUserInfoFetched(GraphUser graphUser) {
            MainFragment.this.user = graphUser;
            updateUI();
        }
    };

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void updateUI() {
        if (user != null) {
            Log.i(TAG, user.getName());
            authFragment.setVisibility(View.INVISIBLE);
            friendsFragment.setVisibility(View.VISIBLE);
        } else {
            authFragment.setVisibility(View.VISIBLE);
            friendsFragment.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
        uiHelper.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        ButterKnife.inject(this, view);
        // TODO Use "injected" views...
        authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("email", "user_friends"));
        authButton.setUserInfoChangedCallback(userInfoChangedCallback);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
