package com.example.movies.services.interfaces;

import com.example.movies.services.APIResponseData.Artist;
import com.example.movies.services.APIResponseData.ArtistProfileResponse;
import com.example.movies.services.APIResponseData.ArtistsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PersonAPIInterface {

    @GET("person/popular")
    Call<ArtistsResponse> getPopularArtists(@Query("api_key") String apiKey, @Query("page") int pageNumber);

    @GET("person/{person_id}")
    Call<Artist> getArtistDetails(@Path("person_id") int artistID, @Query("api_key") String apiKey);

    @GET("person/{person_id}/images")
    Call<ArtistProfileResponse> getArtistImages(@Path("person_id") int artistID, @Query("api_key") String apiKey);

    @GET("search/person")
    Call<ArtistsResponse> searchArtists(@Query("api_key") String apiKey,
                                        @Query("page") int pageNumber, @Query("query") String queryString);

}
