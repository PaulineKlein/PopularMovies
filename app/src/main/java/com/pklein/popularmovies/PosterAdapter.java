package com.pklein.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.pklein.popularmovies.data.Movie;
import com.pklein.popularmovies.tools.NetworkUtils;
import com.squareup.picasso.Picasso;
import java.net.URL;
import java.util.List;

/**
 * Created by Pauline on 22/02/2018.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder> {

    private List<Movie> mMovieData;

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

        final Context context = posterAdapterViewHolder.itemView.getContext();
        final Movie MovieSelected = mMovieData.get(position);

        URL posterRequestUrl = NetworkUtils.buildPosterUrl(mMovieData.get(position).getmPoster_path());
        String movieUrlForThisPos = posterRequestUrl.toString();
        Picasso.with(context)
                .load(movieUrlForThisPos)
                .into(posterAdapterViewHolder.movieIv);

        posterAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent startChildActivityIntent = new Intent(context, MovieInformation.class);
                if(MovieSelected != null)
                {
                    startChildActivityIntent.putExtra("Movie", MovieSelected);
                }
                context.startActivity(startChildActivityIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    public void setMovieData(List<Movie> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
