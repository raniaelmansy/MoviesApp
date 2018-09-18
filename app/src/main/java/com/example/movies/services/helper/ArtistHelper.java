package com.example.movies.services.helper;

import android.content.Context;

import com.example.movies.R;
import com.example.movies.services.APIResponseData.Artist;
import com.example.movies.services.APIResponseData.ArtistProfileResponse;
import com.example.movies.services.APIResponseData.ProfilesItem;
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

public class ArtistHelper {

    private Context mContext;
    private Call<Artist> mArtistResponseCall;
    private Call<ArtistProfileResponse> mArtistProfileResponseCall;

    public ArtistHelper(Context context){
        mContext = context;
    }

    PopularAPIInterface popularAPIInterface = RetrofitServiceGenerator.createService(PopularAPIInterface.class);

    public void getPopularArtists(int artistID) {

        mArtistResponseCall = popularAPIInterface.getArtistDetails(artistID, Constants.API_KEY);
        mArtistResponseCall.enqueue(new Callback<Artist>() {
            @Override
            public void onResponse(Call<Artist> call, Response<Artist> response) {
                if (response.body() != null) {

                    EventBus.getDefault().post(new ArtistEvent(ArtistEvent.EventType.SuccessData,
                            response.body()));
                } else {
                    EventBus.getDefault().post(new ArtistEvent(ArtistEvent.EventType.Error,
                            mContext.getString(R.string.no_items)));
                }
            }

            @Override
            public void onFailure(Call<Artist> call, Throwable t) {
                EventBus.getDefault().post(new ArtistEvent(ArtistEvent.EventType.Error,
                        mContext.getString(R.string.something_went_wrong)));
            }
        });
    }

    public void getProfileImages(int artistID){

        mArtistProfileResponseCall = popularAPIInterface.getArtistImages(artistID, Constants.API_KEY);
        mArtistProfileResponseCall.enqueue(new Callback<ArtistProfileResponse>() {
            @Override
            public void onResponse(Call<ArtistProfileResponse> call, Response<ArtistProfileResponse> response) {
                if(response.body() != null && response.body().getProfiles() != null
                        && response.body().getProfiles().size() != 0){

                    EventBus.getDefault().post(new ArtistEvent(ArtistEvent.EventType.SuccessImages,
                            response.body().getProfiles()));

                }else{
                    EventBus.getDefault().post(new ArtistEvent(ArtistEvent.EventType.Error,
                            mContext.getString(R.string.no_items)));
                }
            }

            @Override
            public void onFailure(Call<ArtistProfileResponse> call, Throwable t) {
                EventBus.getDefault().post(new ArtistEvent(ArtistEvent.EventType.Error,
                        mContext.getString(R.string.something_went_wrong)));
            }
        });
    }

    public static class ArtistEvent {

        private EventType eventType;
        private String errorMessage;
        private Artist artist;
        private List<ProfilesItem> profilesItems;

        ArtistEvent(EventType eventType, String errorMessage) {
            this.eventType = eventType;
            this.errorMessage = errorMessage;
        }

        ArtistEvent(EventType eventType, Artist artist) {
            this.eventType = eventType;
            this.artist = artist;
        }

        ArtistEvent(EventType eventType, List<ProfilesItem> profilesItems) {
            this.eventType = eventType;
            this.profilesItems = profilesItems;
        }

        public enum EventType {
            SuccessData,
            SuccessImages,
            Error;

            public String getName() {
                switch (this) {
                    case SuccessData:
                        return "SuccessData";
                    case SuccessImages:
                        return "SuccessImages";
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

        public Artist getArtist() {
            return artist;
        }

        public List<ProfilesItem> getProfilesItems(){ return profilesItems; }
    }
}
