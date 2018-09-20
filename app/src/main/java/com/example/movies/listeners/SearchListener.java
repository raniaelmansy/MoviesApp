package com.example.movies.listeners;

import com.example.movies.services.APIResponseData.Artist;

import java.util.List;

/**
 * Created by Rania on 9/19/2018.
 */

public interface SearchListener {

    void onSuccess(List<Artist> artistList, int totalPages, int currentPage, String searchQuery);
    void onError(String message);
    void onNoResults();
}
