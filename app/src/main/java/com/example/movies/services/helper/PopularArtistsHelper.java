package com.example.movies.services.helper;

import android.content.Context;

import com.example.movies.R;
import com.example.movies.services.APIResponseData.Artist;
import com.example.movies.services.APIResponseData.PopularArtistsResponse;
import com.example.movies.services.RetrofitServiceGenerator;
import com.example.movies.services.interfaces.PopularAPIInterface;
import com.example.movies.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rania on 9/16/2018.
 */

public class PopularArtistsHelper {

    Context mContext;
    Call<PopularArtistsResponse> mPopularArtistsResponseCall;

    public PopularArtistsHelper(Context context){
        mContext = context;
    }

    public void getPopularArtists(int pageNumber){

        PopularAPIInterface popularAPIInterface = RetrofitServiceGenerator.createService(PopularAPIInterface.class);
        mPopularArtistsResponseCall = popularAPIInterface.getPopularArtists(Constants.API_KEY, pageNumber);

        mPopularArtistsResponseCall.enqueue(new Callback<PopularArtistsResponse>() {
            @Override
            public void onResponse(Call<PopularArtistsResponse> call, Response<PopularArtistsResponse> response) {
                if(response.body() != null && response.body().getResults() != null && response.body().getResults().size() != 0){

                    EventBus.getDefault().post(new PopularArtistsEvent(PopularArtistsEvent.EventType.Success,
                            response.body().getPage(), response.body().getTotalPages(), response.body().getResults()));
                }else{
                    EventBus.getDefault().post(new PopularArtistsEvent(PopularArtistsEvent.EventType.Error,
                          mContext.getString(R.string.no_items)));
                }
            }

            @Override
            public void onFailure(Call<PopularArtistsResponse> call, Throwable t) {
                EventBus.getDefault().post(new PopularArtistsEvent(PopularArtistsEvent.EventType.Error,
                      mContext.getString(R.string.something_went_wrong)));
            }
        });
    }

    public static class PopularArtistsEvent {

        private EventType eventType;
        private String errorMessage;
        private int currentPage;
        private int totalNumberOfPages;
        private List<Artist> artistList;

        PopularArtistsEvent(EventType eventType, String errorMessage) {
            this.eventType = eventType;
            this.errorMessage = errorMessage;
        }

        PopularArtistsEvent(EventType eventType, int currentPage, int totalNumberOfPages, List<Artist> artistList) {
            this.eventType = eventType;
            this.currentPage = currentPage;
            this.totalNumberOfPages = totalNumberOfPages;
            this.artistList = artistList;
        }

        public enum EventType {
            Success,
            Error;

            public String getName() {
                switch (this) {
                    case Success:
                        return "Success";
                    case Error:
                        return "Error";
                }
                return null;
            }
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public EventType getEventType() {
            return eventType;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public int getTotalNumberOfPages() {
            return totalNumberOfPages;
        }

        public List<Artist> getArtistList(){ return artistList; }
    }
}
