package com.example.movies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.movies.R;
import com.example.movies.activities.ArtistImageActivity;
import com.example.movies.services.APIResponseData.ProfilesItem;
import com.example.movies.utils.Constants;

import java.util.List;

/**
 * Created by Rania on 9/17/2018.
 */

public class ArtistImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ProfilesItem> mProfilesItems;
    Context mContext;

    public ArtistImagesAdapter(Context context){
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_image_item_list, null);
        RecyclerView.ViewHolder viewHolder = new ImagesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ProfilesItem profileImage = mProfilesItems.get(position); // Image
        final ImagesViewHolder imagesViewHolder = (ImagesViewHolder) holder;

        Glide.with(mContext)
                .load(Constants.ImagesPath_w154 + profileImage.getFilePath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade(1000)
                .into(imagesViewHolder.mArtistImage);

        imagesViewHolder.mContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ArtistImageActivity.class);
                intent.putExtra(Constants.ARTIST_IMAGE_PATH_EXTRA_KEY, profileImage.getFilePath());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProfilesItems != null ? mProfilesItems.size() : 0;
    }

    public void setProfilesItems(List<ProfilesItem> profilesItems){
        mProfilesItems = profilesItems;
    }

    protected class ImagesViewHolder extends RecyclerView.ViewHolder {

        private ImageView mArtistImage;
        private LinearLayout mContainerLayout;

        public ImagesViewHolder(View itemView) {
            super(itemView);

            mArtistImage = itemView.findViewById(R.id.artistImage);
            mContainerLayout = itemView.findViewById(R.id.containerLayout);
        }
    }
}
