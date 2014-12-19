package com.andrastoth.nearby.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.andrastoth.nearby.R;
import com.andrastoth.nearby.base.BaseActivity;
import com.andrastoth.nearby.base.BaseFragment;
import com.andrastoth.nearby.login.LoginActivity;
import com.facebook.AppEventsLogger;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Copyright (c) 2014 András Tóth (tothandras). All rights Reserved.
 */
public class FriendsActivity extends BaseActivity implements MenuAdapter.OnItemClickListener {
    private static final String TAG = "HomeActivity";
    private static final int LAYOUT = R.layout.friends_activity;
    private static final int FRAGMENT_CONTAINER = R.id.content_frame;

    @Inject
    FriendsListFragment friendsListFragment;

    @InjectView(R.id.drawer_toolbar)
    Toolbar toolbar;

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @InjectView(R.id.left_drawer)
    RecyclerView drawerList;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private CharSequence drawerTitle;
    private CharSequence title;
    private List<String> menuTitles;

    @Override
    protected List<Object> getModules() {
        List<Object> modules = Arrays.<Object>asList(new FriendsModule(this));
        modules.addAll(super.getModules());
        return modules;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        ButterKnife.inject(this);

        title = drawerTitle = getTitle();

        menuTitles = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.drawer_array)));
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // improve performance by indicating the list if fixed size
        drawerList.setHasFixedSize(true);
        drawerList.setLayoutManager(new LinearLayoutManager(this));

        drawerList.setAdapter(new MenuAdapter(menuTitles, this));

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        // menu.findItem(R.id.action_*).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            // case R.id.action_*
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onClick(View view, int position) {
        selectItem(position);
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        BaseFragment fragment;
        switch (position) {
            case 1:
                logOut();
            default:
                fragment = friendsListFragment;
        }
        if ((fragment != null) && !fragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(FRAGMENT_CONTAINER, fragment)
                    .commit();
        }

        drawerLayout.closeDrawer(drawerList);
    }

    public void logOut() {
        ParseUser.logOut();
        startLoginActivity();
    }

    public void onLogOutClick(View view) {
        logOut();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}