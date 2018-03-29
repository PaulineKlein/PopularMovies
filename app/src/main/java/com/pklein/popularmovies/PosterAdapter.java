package com.pklein.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.pklein.popularmovies.data.Movie;
import com.pklein.popularmovies.tools.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

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

        // if we are not connected try from internal storage :
        if(!NetworkUtils.isconnected((ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE)))
        {
            String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "MyMoviesPosters"+ File.separator +"Movie_"+MovieSelected.getmId()+".jpg";
            Picasso.with(context)
            .load("file://"+path)
            .error(R.drawable.ic_local_movies)
            .into(posterAdapterViewHolder.movieIv);
        }
        else {
            URL posterRequestUrl = NetworkUtils.buildPosterUrl(mMovieData.get(position).getmPoster_path(), "w342");
            String movieUrlForThisPos = posterRequestUrl.toString();
            Picasso.with(context)
                    .load(movieUrlForThisPos)
                    .error(R.drawable.ic_local_movies)
                    .into(posterAdapterViewHolder.movieIv);
        }

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
