package com.example.movies.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.movies.R;
import com.example.movies.adapters.PopularArtistsAdapter;
import com.example.movies.listeners.PaginationScrollListener;
import com.example.movies.listeners.PopularArtistsListener;
import com.example.movies.services.APIResponseData.Artist;
import com.example.movies.utils.Utils;
import com.example.movies.viewmodels.PopularArtistsViewModel;

import java.util.List;

/**
 * Created by Rania on 9/19/2018.
 */

public class PopularArtistsFragment extends Fragment implements PopularArtistsListener {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private LinearLayoutManager mLinearLayoutManager;
    private PopularArtistsAdapter mAdapter;

    Context mContext;
    PopularArtistsViewModel mViewModel;

    private int mTotalPages = 0;
    private int mCurrentPage = 1;
    private boolean mIsLoading = false;
    private boolean mIsLastPage = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_popular_artists, container, false);
        mContext = getActivity();

        mRecyclerView = rootView.findViewById(R.id.main_recycler);
        mProgressBar = rootView.findViewById(R.id.main_progress);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mAdapter = new PopularArtistsAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);

        mViewModel = new PopularArtistsViewModel(mContext, this);
        mViewModel.getPopularArtists(mCurrentPage);

        mRecyclerView.addOnScrollListener(new PaginationScrollListener(mLinearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                mIsLoading = true;
                mCurrentPage += 1;
                mViewModel.getPopularArtists(mCurrentPage);
            }

            @Override
            public int getTotalPageCount() {
                return mTotalPages;
            }

            @Override
            public boolean isLastPage() {
                return mIsLastPage;
            }

            @Override
            public boolean isLoading() {
                return mIsLoading;
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.onDestroy();
    }

    @Override
    public void onSuccess(List<Artist> artistList, int totalPagesNumber, int currentLoadedPage) {

        mTotalPages = totalPagesNumber;

        //Utils.showToast(mContext, "Success, Current page:"+ currentLoadedPage +", size=" + artistList.size() + ", total= " + mTotalPages );

        if(mAdapter != null) {
            if (mCurrentPage == 1) {
                mProgressBar.setVisibility(View.GONE);
                mAdapter.setArtistsItems(artistList);
                mAdapter.notifyDataSetChanged();

                if (mCurrentPage <= mTotalPages) {
                    mAdapter.addLoadingFooter();
                }
            } else {
                mAdapter.removeLoadingFooter();
                mIsLoading = false;
                mAdapter.addAll(artistList);
                if (mCurrentPage != mTotalPages) mAdapter.addLoadingFooter();
                else mIsLoading = true;
            }
        }
    }

    @Override
    public void onError(String message) {
        mProgressBar.setVisibility(View.GONE);
        Utils.showToast(mContext, "onError: "+ message);
    }

}
