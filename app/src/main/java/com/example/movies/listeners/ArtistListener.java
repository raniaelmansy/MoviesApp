package com.example.movies.listeners;

import com.example.movies.services.APIResponseData.Artist;
import com.example.movies.services.APIResponseData.ProfilesItem;

import java.util.List;

/**
 * Created by Rania on 9/16/2018.
 */

public interface ArtistListener {

    void onGetArtistDataSuccess(Artist artist);
    void onGetArtistImagesSuccess(List<ProfilesItem> profilesItems);
    void onError(String message);
}
