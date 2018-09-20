package com.example.movies.viewmodels;

import android.content.Context;

import com.example.movies.R;
import com.example.movies.listeners.PopularArtistsListener;
import com.example.movies.services.helper.ArtistsHelper;
import com.example.movies.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Rania on 9/16/2018.
 */

public class PopularArtistsViewModel {

    private Context mContext;
    private PopularArtistsListener mListener;
    private ArtistsHelper mPopularArtistsHelper;

    public PopularArtistsViewModel(Context context, PopularArtistsListener popularArtistsListener){
        mContext = context;
        mListener = popularArtistsListener;
        mPopularArtistsHelper = new ArtistsHelper(mContext);
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

    public void getPopularArtists(int pageNumber){
        if (Utils.isInternetConnectionExist(mContext)) {
            mPopularArtistsHelper.getPopularArtists(pageNumber);
        }else{
            mListener.onError(mContext.getString(R.string.no_internet_connection));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onArtistsEvent(ArtistsHelper.ArtistsEvent popularArtistsEvent) {
        switch (popularArtistsEvent.getEventType()) {
            case Success:
                mListener.onSuccess(popularArtistsEvent.getArtistList(),
                        popularArtistsEvent.getTotalNumberOfPages(),
                        popularArtistsEvent.getCurrentPage());
                break;
            case Error:
                mListener.onError(popularArtistsEvent.getErrorMessage());
                break;
        }
    }
}
