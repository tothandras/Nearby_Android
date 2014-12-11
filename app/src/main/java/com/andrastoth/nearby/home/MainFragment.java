package com.andrastoth.nearby.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andrastoth.nearby.R;
import com.andrastoth.nearby.base.BaseFragment;
import com.andrastoth.nearby.data.User;
import com.andrastoth.nearby.navigation.NavigationActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class MainFragment extends BaseFragment {
    private static final String TAG = "MainFragment";
    private static final int LAYOUT = R.layout.main_fragment;

    @Inject
    List<User> users;

    @Inject
    User.Builder userBuilder;

    @Inject
    Gson gson;

    @Inject
    RecyclerView.Adapter recyclerViewAdapter;

    @Inject
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    @InjectView(R.id.authButton)
    LoginButton authButton;

    @InjectView(R.id.authImage)
    ImageView authImage;

    @InjectView(R.id.authFragment)
    ViewGroup authFragment;

    @InjectView(R.id.friendsFragment)
    ViewGroup friendsFragment;

    @InjectView(R.id.friends_recycler_view)
    RecyclerView recyclerView;

    private UiLifecycleHelper uiHelper;

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            authFragment.setVisibility(View.INVISIBLE);
            getUserFriends(session);
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
            authFragment.setVisibility(View.VISIBLE);
        }
    }

    private void getUserFriends(final Session session) {
        Request.GraphUserCallback callback = new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser graphUser, Response response) {
                if (graphUser != null && session == Session.getActiveSession()) {
                    Request.GraphUserListCallback callback = new Request.GraphUserListCallback() {
                        @Override
                        public void onCompleted(List<GraphUser> graphUsers, Response response) {
                            for (GraphUser user : graphUsers) {
                                String url = "";
                                try {
                                    // TODO replace with GSON
                                    url = user.getInnerJSONObject().getJSONObject("picture").getJSONObject("data").getString("url");
                                } catch (JSONException e) {
                                    Log.e(TAG, e.getMessage());
                                }
                                User u = userBuilder.id(user.getId()).name(user.getName()).picture(url).build();
                                users.add(u);
                            }
                        }
                    };
                    Bundle params = new Bundle();
                    params.putString("fields", "id,name,picture");
                    Request friendsRequest = Request.newMyFriendsRequest(session, callback);
                    friendsRequest.setParameters(params);
                    friendsRequest.executeAsync();
                }
            }
        };
        Request.newMeRequest(session, callback).executeAsync();
    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

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
                (session.isOpened() || session.isClosed())) {
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

        authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("email", "user_friends"));

        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.start_navigation_button)
    public void startNavigation(View view) {
        Intent intent = new Intent(getActivity(), NavigationActivity.class);
        Bundle bundle = new Bundle();
        float[] friendLocation = {47.476621f, 19.058588f};
        bundle.putFloatArray(NavigationActivity.FRIEND_LOCATION, friendLocation);
        bundle.putString(NavigationActivity.FRIEND_NAME, "Test User");
        intent.putExtra(NavigationActivity.LAT_LNG, bundle);
        startActivity(intent);
    }
}
