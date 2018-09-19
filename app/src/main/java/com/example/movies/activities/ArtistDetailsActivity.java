package com.example.movies.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.movies.R;
import com.example.movies.adapters.ArtistImagesAdapter;
import com.example.movies.listeners.ArtistListener;
import com.example.movies.services.APIResponseData.Artist;
import com.example.movies.services.APIResponseData.ProfilesItem;
import com.example.movies.utils.Constants;
import com.example.movies.utils.Utils;
import com.example.movies.viewmodels.ArtistDetailsViewModel;

import java.util.List;

/**
 * Created by Rania on 9/17/2018.
 */

public class ArtistDetailsActivity extends AppCompatActivity implements ArtistListener, View.OnClickListener{

    private TextView mName;
    private TextView mSpeciality;
    private TextView mBirthData;
    private TextView mBio;
    private TextView mPopularity;
    private ImageView mProfileImage;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private ImageView mBackImage;

    Context mContext;
    ArtistDetailsViewModel mViewModel;
    ArtistImagesAdapter mArtistImagesAdapter;
    private Artist mArtist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constants.ARTIST_OBJECT_EXTRA_KEY)) {
            mArtist = bundle.getParcelable(Constants.ARTIST_OBJECT_EXTRA_KEY);
        }

        mContext = this;
        mViewModel = new ArtistDetailsViewModel(mContext, this);

        mName = findViewById(R.id.nameTextView);
        mSpeciality = findViewById(R.id.specialityTextView);
        mBirthData = findViewById(R.id.birthDataTextView);
        mBio = findViewById(R.id.bioTextView);
        mPopularity = findViewById(R.id.popularityTextView);
        mProfileImage = findViewById(R.id.artistImage);
        mProgressBar = findViewById(R.id.main_progress);
        mRecyclerView = findViewById(R.id.main_recycler);
        mBackImage = findViewById(R.id.backImageView);
        mBackImage.setOnClickListener(this);

        mArtistImagesAdapter = new ArtistImagesAdapter(mContext);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.setAdapter(mArtistImagesAdapter);
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);

        if(mArtist != null){
            loadInitialArtistData();
            mViewModel.getArtistData(mArtist.getId());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.onDestroy();
    }

    private void loadInitialArtistData(){
        mName.setText(mArtist.getName());
        double popularity =  Math.round(mArtist.getPopularity());
        mPopularity.setText(mContext.getString(R.string.popularity, String.valueOf(popularity)));
        Glide.with(mContext)
                .load(Constants.ImagesPath_w342 + mArtist.getProfilePath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .error(R.drawable.default_avatar)
                .crossFade(1000)
                .into(mProfileImage);
    }

    @Override
    public void onGetArtistDataSuccess(Artist artist) {
        mViewModel.getProfileImages(mArtist.getId());
        mSpeciality.setText(artist.getKnownForDepartment());
        if(artist.getBirthday() != null && artist.getPlaceOfBirth() != null) {
            mBirthData.setText(getString(R.string.birth, artist.getBirthday(), artist.getPlaceOfBirth()));
        }
        mBio.setText(artist.getBiography());
        double popularity =  Math.round(artist.getPopularity());
        mPopularity.setText(mContext.getString(R.string.popularity, String.valueOf(popularity)));
        Glide.with(mContext)
                .load(Constants.ImagesPath_w342 + artist.getProfilePath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade(1000)
                .into(mProfileImage);
    }

    @Override
    public void onGetArtistImagesSuccess(List<ProfilesItem> profilesItems) {
        Utils.showToast(this, "Success, size=" + profilesItems.size());
        mProgressBar.setVisibility(View.GONE);
        if (mArtistImagesAdapter != null) {
            mArtistImagesAdapter.setProfilesItems(profilesItems);
            mArtistImagesAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onError(String message) {
        mProgressBar.setVisibility(View.GONE);
        Utils.showToast(this, "onError: "+ message);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
