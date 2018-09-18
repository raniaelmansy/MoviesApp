package com.example.movies.listeners;

import com.example.movies.services.APIResponseData.Artist;

import java.util.List;

/**
 * Created by Rania on 9/16/2018.
 */

public interface PopularArtistsListener {

    void onSuccess(List<Artist> artistList, int totalPages, int currentPage);
    void onError(String message);
}
