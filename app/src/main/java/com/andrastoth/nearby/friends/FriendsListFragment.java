package com.andrastoth.nearby.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrastoth.nearby.R;
import com.andrastoth.nearby.base.BaseFragment;
import com.andrastoth.nearby.data.User;
import com.andrastoth.nearby.login.LoginActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class FriendsListFragment extends BaseFragment {
    private static final String TAG = "FriendsListFragment";
    private static final int LAYOUT = R.layout.friends_list_fragment;

    private Session session;
    private ParseUser currentUser;

    @Inject
    List<User> users;

    @Inject
    User.Builder userBuilder;

    @Inject
    RecyclerView.Adapter recyclerViewAdapter;

    @Inject
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    @InjectView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;

    @InjectView(R.id.friends_recycler_view)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        ButterKnife.inject(this, view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        ParseFacebookUtils.initialize(getResources().getString(R.string.facebook_app_id));
        session = ParseFacebookUtils.getSession();
        if (session != null && session.isOpened()) {
            currentUser = ParseUser.getCurrentUser();
            getFriends();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            startLoginActivity();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void getFriends() {
        Request.newMeRequest(session, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser graphUser, Response response) {
                if (graphUser != null && session == Session.getActiveSession()) {
                    Request.GraphUserListCallback callback = new Request.GraphUserListCallback() {
                        @Override
                        public void onCompleted(List<GraphUser> graphUsers, Response response) {
                            ArrayList<String> friends = new ArrayList<String>();
                            for (GraphUser user : graphUsers) {
                                String url = "";
                                try {
                                    // TODO replace with GSON?
                                    url = user.getInnerJSONObject().getJSONObject("picture").getJSONObject("data").getString("url");
                                } catch (JSONException e) {
                                    Log.e(TAG, e.getMessage());
                                }
                                User u = userBuilder.id(user.getId()).name(user.getName()).picture(url).build();
                                if (!users.contains(u)) {
                                    users.add(u);
                                    friends.add(u.getId());
                                }
                            }
                            progressBar.hide();
                            recyclerViewAdapter.notifyDataSetChanged();
                            currentUser.put("friends", friends);
                            currentUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d(TAG, "Parse user friends saved");
                                    } else {
                                        Log.d(TAG, e.getMessage());
                                    }
                                }
                            });
                        }
                    };
                    Bundle params = new Bundle();
                    params.putString("fields", "id,name,picture");
                    Request friendsRequest = Request.newMyFriendsRequest(session, callback);
                    friendsRequest.setParameters(params);
                    friendsRequest.executeAsync();
                }
            }
        }).executeAsync();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
