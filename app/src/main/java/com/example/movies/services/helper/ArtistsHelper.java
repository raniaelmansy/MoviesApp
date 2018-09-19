package com.example.movies.services.helper;

import android.content.Context;

import com.example.movies.R;
import com.example.movies.services.APIResponseData.Artist;
import com.example.movies.services.APIResponseData.ArtistsResponse;
import com.example.movies.services.RetrofitServiceGenerator;
import com.example.movies.services.interfaces.PersonAPIInterface;
import com.example.movies.utils.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rania on 9/16/2018.
 */

public class ArtistsHelper {

    Context mContext;
    Call<ArtistsResponse> mPopularArtistsResponseCall;

    public ArtistsHelper(Context context){
        mContext = context;
    }

    public void getPopularArtists(int pageNumber){

        PersonAPIInterface popularAPIInterface = RetrofitServiceGenerator.createService(PersonAPIInterface.class);
        mPopularArtistsResponseCall = popularAPIInterface.getPopularArtists(Constants.API_KEY, pageNumber);

        mPopularArtistsResponseCall.enqueue(new Callback<ArtistsResponse>() {
            @Override
            public void onResponse(Call<ArtistsResponse> call, Response<ArtistsResponse> response) {
                if(response.body() != null && response.body().getResults() != null && response.body().getResults().size() != 0){

                    EventBus.getDefault().post(new ArtistsEvent(ArtistsEvent.EventType.Success,
                            response.body().getPage(), response.body().getTotalPages(), response.body().getResults()));
                }else{
                    EventBus.getDefault().post(new ArtistsEvent(ArtistsEvent.EventType.Error,
                          mContext.getString(R.string.no_items)));
                }
            }

            @Override
            public void onFailure(Call<ArtistsResponse> call, Throwable t) {
                EventBus.getDefault().post(new ArtistsEvent(ArtistsEvent.EventType.Error,
                      mContext.getString(R.string.something_went_wrong)));
            }
        });
    }

    public static class ArtistsEvent {

        private EventType eventType;
        private String errorMessage;
        private int currentPage;
        private int totalNumberOfPages;
        private List<Artist> artistList;

        ArtistsEvent(EventType eventType, String errorMessage) {
            this.eventType = eventType;
            this.errorMessage = errorMessage;
        }

        ArtistsEvent(EventType eventType, int currentPage, int totalNumberOfPages, List<Artist> artistList) {
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
