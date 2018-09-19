package com.example.movies.viewmodels;

import android.content.Context;

import com.example.movies.R;
import com.example.movies.listeners.SearchListener;
import com.example.movies.services.helper.SearchArtistHelper;
import com.example.movies.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Rania on 9/16/2018.
 */

public class SearchArtistViewModel {

    Context mContext;
    SearchListener mListener;
    SearchArtistHelper mSearchArtistHelper;

    public SearchArtistViewModel(Context context, SearchListener searchListener){
        mContext = context;
        mListener = searchListener;
        mSearchArtistHelper = new SearchArtistHelper(mContext);
    }

    public void onResume() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void onPause() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onDestroy() {
        mListener = null;
    }

    public void searchArtists(int pageNumber, String queryString){
        if (Utils.isInternetConnectionExist(mContext)) {
            mSearchArtistHelper.searchArtists(pageNumber, queryString);
        }else{
            mListener.onError(mContext.getString(R.string.no_internet_connection));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchArtistsEvent(SearchArtistHelper.SearchArtistsEvent artistsEvent) {
        switch (artistsEvent.getEventType()) {
            case Success:
                mListener.onSuccess(artistsEvent.getArtistList(),
                        artistsEvent.getTotalNumberOfPages(),
                        artistsEvent.getCurrentPage(), artistsEvent.getQueryString());
                break;
            case Error:
                mListener.onError(artistsEvent.getErrorMessage());
                break;
            case NoResults:
                mListener.onNoResults();
                break;
        }
    }
}
