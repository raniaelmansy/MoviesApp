package com.example.movies.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movies.R;
import com.example.movies.adapters.PopularArtistsAdapter;
import com.example.movies.listeners.PaginationScrollListener;
import com.example.movies.listeners.SearchListener;
import com.example.movies.services.APIResponseData.Artist;
import com.example.movies.utils.Utils;
import com.example.movies.viewmodels.SearchArtistViewModel;

import java.util.List;

/**
 * Created by Rania on 9/19/2018.
 */

public class SearchFragment extends Fragment implements SearchListener{

    private EditText mSearchText;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private LinearLayoutManager mLinearLayoutManager;
    private PopularArtistsAdapter mAdapter;
    private Button mSearchButton;
    private TextView mResultsLabelTextView;

    Context mContext;
    SearchArtistViewModel mViewModel;

    private int mTotalPages = 0;
    private int mCurrentPage = 1;
    private boolean mIsLoading = false;
    private boolean mIsLastPage = false;
    String mCurrentQueryString = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        mContext = getActivity();

        mSearchText = rootView.findViewById(R.id.searchEditText);
        mRecyclerView = rootView.findViewById(R.id.main_recycler);
        mProgressBar = rootView.findViewById(R.id.main_progress);
        mSearchButton = rootView.findViewById(R.id.searchButton);
        mResultsLabelTextView = rootView.findViewById(R.id.resultsLabelTextView);
        mProgressBar.setVisibility(View.GONE);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mAdapter = new PopularArtistsAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);

        mViewModel = new SearchArtistViewModel(mContext, this);

        mCurrentQueryString = mSearchText.getText().toString().trim();
        mSearchButton.setEnabled(false);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideSoftKeyboard(getActivity(), null);
                String searchQuery = mSearchText.getText().toString();

                mCurrentQueryString = searchQuery;
                mCurrentPage = 1;
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.VISIBLE);
                mResultsLabelTextView.setText(getString(R.string.search_results, mCurrentQueryString));
                mViewModel.searchArtists(mCurrentPage, searchQuery);
            }
        });
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchQuery = s.toString();

               if(searchQuery.length() >= 3){
                   mSearchButton.setEnabled(true);
               }else{
                   mSearchButton.setEnabled(false);
                   mProgressBar.setVisibility(View.GONE);
               }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mRecyclerView.addOnScrollListener(new PaginationScrollListener(mLinearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                mIsLoading = true;
                mCurrentPage += 1;
                mViewModel.searchArtists(mCurrentPage, mCurrentQueryString);
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
    public void onSuccess(List<Artist> artistList, int totalPagesNumber, int currentPage, String queryString) {
        mTotalPages = totalPagesNumber;

       // Utils.showToast(mContext, "Current page:"+ currentPage +", size=" + artistList.size() + ", total= " + mTotalPages );

        if(mAdapter != null) {
            if (mCurrentPage == 1) {
                mProgressBar.setVisibility(View.GONE);
                mAdapter.setArtistsItems(artistList);
                mAdapter.notifyDataSetChanged();

                if (mCurrentPage < mTotalPages) {
                    mAdapter.addLoadingFooter();
                }
            } else {
                mAdapter.removeLoadingFooter();
                mIsLoading = false;
                mAdapter.addAll(artistList);
                if (mCurrentPage != mTotalPages) {
                    mAdapter.addLoadingFooter();
                }
                else {
                    mIsLoading = true;
                }
            }
        }
    }

    @Override
    public void onError(String message) {
        mProgressBar.setVisibility(View.GONE);
        Utils.showToast(mContext, "onError: "+ message);
    }

    @Override
    public void onNoResults() {
        mProgressBar.setVisibility(View.GONE);
        Utils.showToast(mContext, getString(R.string.no_items));
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }
}
