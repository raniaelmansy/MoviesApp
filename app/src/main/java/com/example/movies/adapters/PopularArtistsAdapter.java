package com.example.movies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.movies.R;
import com.example.movies.activities.ArtistDetailsActivity;
import com.example.movies.services.APIResponseData.Artist;
import com.example.movies.utils.Constants;

import java.util.List;

/**
 * Created by Rania on 9/16/2018.
 */

public class PopularArtistsAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<Artist> mArtistList;
    Context mContext;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public PopularArtistsAdapter(Context context){
        mContext = context;
    }

    public void setArtistsItems(List<Artist> artistsItems) {
        this.mArtistList = artistsItems;
    }
    public List<Artist> getArtistsItems() {
        return mArtistList;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v = inflater.inflate(R.layout.popular_item_list, parent, false);
        viewHolder = new ArtistsViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Artist artist = mArtistList.get(position); // Artist

        switch (getItemViewType(position)) {
            case ITEM:
                final ArtistsViewHolder artistHolder = (ArtistsViewHolder) holder;

                artistHolder.mName.setText(artist.getName());
                double popularity =  Math.round(artist.getPopularity());
                artistHolder.mPopularity.setText(mContext.getString(R.string.popularity, String.valueOf(popularity)));

                Glide.with(mContext)
                        .load(Constants.ImagesPath_w342 + artist.getProfilePath())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade(1000)
                        .error(R.drawable.default_avatar)
                        .into(artistHolder.mArtistImage);

                artistHolder.mContainerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ArtistDetailsActivity.class);
                        intent.putExtra(Constants.ARTIST_OBJECT_EXTRA_KEY, artist);
                        mContext.startActivity(intent);
                    }
                });
                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mArtistList!= null?mArtistList.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mArtistList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    protected class ArtistsViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private TextView mPopularity;
        private ImageView mArtistImage;
        private LinearLayout mContainerLayout;

        public ArtistsViewHolder(View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.nameTextView);
            mPopularity = itemView.findViewById(R.id.popularityTextView);
            mArtistImage = itemView.findViewById(R.id.artistImage);
            mContainerLayout = itemView.findViewById(R.id.containerLayout);
        }
    }

    protected class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }


       /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Artist r) {
        mArtistList.add(r);
        notifyItemInserted(mArtistList.size() - 1);
    }

    public void addAll(List<Artist> mArtistList) {
        for (Artist result : mArtistList) {
            add(result);
        }
    }

    public void remove(Artist r) {
        int position = mArtistList.indexOf(r);
        if (position > -1) {
            mArtistList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Artist());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = mArtistList.size() - 1;
        Artist result = getItem(position);

        if (result != null) {
            mArtistList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Artist getItem(int position) {
        return mArtistList.get(position);
    }

    //////////////////////////////////////////////////////////////////////
}
