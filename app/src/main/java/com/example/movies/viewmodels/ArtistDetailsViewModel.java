package com.example.movies.viewmodels;

import android.content.Context;

import com.example.movies.R;
import com.example.movies.listeners.ArtistListener;
import com.example.movies.services.helper.ArtistDetailsHelper;
import com.example.movies.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Rania on 9/16/2018.
 */

public class ArtistDetailsViewModel {

    private Context mContext;
    private ArtistListener mListener;
    private ArtistDetailsHelper mArtistHelper;

    public ArtistDetailsViewModel(Context context, ArtistListener artistsListener){
        mContext = context;
        mListener = artistsListener;
        mArtistHelper = new ArtistDetailsHelper(mContext);
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

    public void getArtistData(int artistID){
        if (Utils.isInternetConnectionExist(mContext)) {
            mArtistHelper.getArtistDetails(artistID);
        }else{
            mListener.onError(mContext.getString(R.string.no_internet_connection));
        }
    }

    public void getProfileImages(int artistID){
        if (Utils.isInternetConnectionExist(mContext)) {
            mArtistHelper.getProfileImages(artistID);
        }else{
            mListener.onError(mContext.getString(R.string.no_internet_connection));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onArtistDetailsEvent(ArtistDetailsHelper.ArtistDetailsEvent artistEvent) {
        switch (artistEvent.getEventType()) {
            case SuccessData:
                mListener.onGetArtistDataSuccess(artistEvent.getArtist());
                break;
            case SuccessImages:
                mListener.onGetArtistImagesSuccess(artistEvent.getProfilesItems());
                break;
            case Error:
                mListener.onError(artistEvent.getErrorMessage());
                break;
        }
    }
}
