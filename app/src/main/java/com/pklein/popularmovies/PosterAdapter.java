package com.pklein.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Pauline on 22/02/2018.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder> {

    private String[] mMovieData;

    public PosterAdapter() {
    }

    public class PosterAdapterViewHolder extends RecyclerView.ViewHolder {

        public final ImageView movieIv;

        public PosterAdapterViewHolder(View view) {
            super(view);
            movieIv = view.findViewById(R.id.image_iv);
        }
    }

    @Override
    public PosterAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_poster;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new PosterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterAdapterViewHolder posterAdapterViewHolder, int position) {
        String movieUrlForThisPos = mMovieData[position];
        Context context = posterAdapterViewHolder.itemView.getContext();

        Picasso.with(context)
                .load(movieUrlForThisPos)
                .into(posterAdapterViewHolder.movieIv);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.length;
    }

    public void setMovieData(String[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
