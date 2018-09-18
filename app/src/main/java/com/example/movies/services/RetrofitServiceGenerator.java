package com.example.movies.services;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceGenerator {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static Retrofit.Builder mBuilder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL);

    public static <T> T createService(Class<T> serviceClass) {
        mBuilder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = mBuilder.build();
        return retrofit.create(serviceClass);
    }

    /**
     * Use a pre-configured Gson parser instance.
     * @param serviceClass
     * @param gson
     * @param <T>
     * @return
     */
    public static <T> T createService(Class<T> serviceClass, Gson gson) {
        mBuilder.addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = mBuilder.build();
        return retrofit.create(serviceClass);
    }
}
