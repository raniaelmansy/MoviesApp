package com.example.movies.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.movies.R;
import com.example.movies.adapters.PopularArtistsAdapter;
import com.example.movies.listeners.PopularArtistsListener;
import com.example.movies.services.APIResponseData.Artist;
import com.example.movies.utils.Utils;
import com.example.movies.viewmodels.PopularArtistsViewModel;

import java.util.List;

/**
 * Created by Rania on 9/16/2018.
 */

public class PopularArtistsActivity extends AppCompatActivity implements PopularArtistsListener{

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    LinearLayoutManager mLinearLayoutManager;
    PopularArtistsAdapter mAdapter;

    Context mContext;
    PopularArtistsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_artists);
        mContext = this;

        mRecyclerView = findViewById(R.id.main_recycler);
        mProgressBar = findViewById(R.id.main_progress);

        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mAdapter = new PopularArtistsAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);

        mViewModel = new PopularArtistsViewModel(mContext, this);
        mViewModel.getPopularArtists(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.onDestroy();
    }

    @Override
    public void onSuccess(List<Artist> artistList) {

        mProgressBar.setVisibility(View.GONE);
        Utils.showToast(this, "Success, size=" + artistList.size());

        if (mAdapter != null) {
            mAdapter.setArtistsItems(artistList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String message) {
        mProgressBar.setVisibility(View.GONE);
        Utils.showToast(this, "onError: "+ message);
    }
}
