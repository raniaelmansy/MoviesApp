package com.example.movies.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.movies.R;
import com.example.movies.fragments.PopularArtistsFragment;
import com.example.movies.fragments.SearchFragment;
import com.example.movies.utils.Utils;

/**
 * Created by Rania on 9/19/2018.
 */

public class MainActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Toolbar mToolBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        mToolBar.setTitle(getString(R.string.popular_artists));
        setSupportActionBar(mToolBar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView mNavigationView = findViewById(R.id.nav_view);
        MenuItem defaultSelectedItem = mNavigationView.getMenu().findItem(R.id.nav_popular_artists);
        defaultSelectedItem.setChecked(true);

        //Load default Fragment
        loadPopularFragment();

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_popular_artists:
                                loadPopularFragment();
                                break;
                            case R.id.nav_search:
                                loadSearchFragment();
                                break;
                        }
                        return true;
                    }
                });

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}

            @Override
            public void onDrawerOpened(View drawerView) {
                Utils.hideSoftKeyboard(MainActivity.this, null);
            }

            @Override
            public void onDrawerClosed(View drawerView) {}

            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }

    private void loadPopularFragment(){
        mToolBar.setTitle(getString(R.string.popular_artists));
        PopularArtistsFragment popularArtistsFragment = new PopularArtistsFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, popularArtistsFragment, "popularArtistsFragment")
                .commit();
    }

    private void loadSearchFragment(){
        mToolBar.setTitle(getString(R.string.search));
        SearchFragment searchFragment = new SearchFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, searchFragment, "searchFragment")
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}
