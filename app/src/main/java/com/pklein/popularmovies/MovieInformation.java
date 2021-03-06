package com.pklein.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pklein.popularmovies.data.FavoriteMovieContract;
import com.pklein.popularmovies.data.Movie;
import com.pklein.popularmovies.tools.NetworkUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class MovieInformation extends Fragment {

    private static final String TAG= MovieInformation.class.getSimpleName();
    private ImageButton miv_favorite;
    private boolean misFavorite;
    private Movie movie;
    private URL mPosterRequestUrl;
    private URL mPosterRequestUrl2;

   // public static final String ARG_PAGE = "ARG_PAGE";
   // private int mPage;

    public static MovieInformation newInstance(/*int page*/) {
        Bundle args = new Bundle();
        //args.putInt(ARG_PAGE, page);
        MovieInformation fragment = new MovieInformation();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        TextView mtv_title;
        TextView mtv_title_original;
        TextView mtv_release_date;
        TextView mtv_overview;
        TextView mtv_vote_counting;
        ImageView miv_back;
        ImageView miv_thumbnail;
        RatingBar mratingBar;

        Log.i(TAG, "Start MovieInformation");
        View view = inflater.inflate(R.layout.movie_information, container, false);

        mtv_title = view.findViewById(R.id.tv_title);
        mtv_title_original = view.findViewById(R.id.tv_title_original);
        mtv_release_date = view.findViewById(R.id.tv_release_date);
        mtv_overview = view.findViewById(R.id.tv_overview);
        mtv_vote_counting = view.findViewById(R.id.tv_vote_counting);
        miv_back = view.findViewById(R.id.image_iv_back);
        miv_thumbnail = view.findViewById(R.id.image_iv_thumbnail);
        mratingBar = view.findViewById(R.id.ratingBar);
        miv_favorite = view.findViewById(R.id.image_iv_favorite);

        Intent intentThatStarted = getActivity().getIntent();

        if(intentThatStarted.hasExtra("Movie")){
            movie = intentThatStarted.getExtras().getParcelable("Movie");
            mtv_title.setText(movie.getmTitle());
            mtv_title_original.setText(movie.getmOriginal_title());
            mtv_overview.setText(movie.getmOverview());
            mtv_vote_counting.setText(this.getResources().getQuantityString(R.plurals.votes, movie.getmVote_count(), movie.getmVote_count()));

            mratingBar.setRating((float) (movie.getmVote_average()) / 2);
            Log.i(TAG, "rate " + (float) movie.getmVote_average());

            String[] dateseparated = movie.getmRelease_date().split("-");
            mtv_release_date.setText(String.format(getString(R.string.release_date), dateseparated[2], dateseparated[1], dateseparated[0]));

            misFavorite=isFavorite(movie.getmId());
            setFavoriteIcon(misFavorite);

            // if we are not connected try from internal storage :
            if(!NetworkUtils.isconnected((ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE)))
            {
                String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "MyMoviesPosters"+ File.separator +"Movie_"+movie.getmId()+".jpg";
                Picasso.with(getActivity())
                        .load("file://"+path)
                        .error(R.drawable.ic_local_movies)
                        .into(miv_back);

                Picasso.with(getActivity())
                        .load("file://"+path)
                        .error(R.drawable.ic_local_movies)
                        .into(miv_thumbnail);
            }
            else {
                mPosterRequestUrl = NetworkUtils.buildPosterUrl(movie.getmBackdrop_path(), "w500");
                Picasso.with(getActivity())
                        .load(mPosterRequestUrl.toString())
                        .error(R.drawable.ic_local_movies)
                        .into(miv_back);

                mPosterRequestUrl2 = NetworkUtils.buildPosterUrl(movie.getmPoster_path(), "w342");
                Picasso.with(getActivity())
                        .load(mPosterRequestUrl2.toString())
                        .error(R.drawable.ic_local_movies)
                        .into(miv_thumbnail);

                Log.i(TAG, "url1 " + mPosterRequestUrl + " url2 " + mPosterRequestUrl2);
            }

            miv_favorite.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(misFavorite) // We want to delete it from database
                    {
                        misFavorite = false;
                        Uri uri = FavoriteMovieContract.FavoriteMovie.buildMovieUri(movie.getmId());
                        int result = getActivity().getContentResolver().delete(uri, null, null);

                        //delete image too
                        File file = new File(Environment.getExternalStorageDirectory().getPath() +File.separator + "MyMoviesPosters","Movie_"+movie.getmId()+".jpg");
                        file.delete();

                        if(result > -1)
                            showToast(getString(R.string.delete_favorite));
                        else
                            showToast(getString(R.string.error_favorite));
                    }
                    else // we want to save it as a favorite
                    {
                        misFavorite = true;
                        Uri uri =FavoriteMovieContract.FavoriteMovie.CONTENT_URI;
                        ContentValues contentValues = transformMovieToContentValues(movie);
                        Uri result = getActivity().getContentResolver().insert(uri,contentValues );

                        //save image too : https://stackoverflow.com/questions/32799353/saving-image-from-url-using-picasso
                        Picasso.with(getActivity().getApplicationContext())
                                .load(mPosterRequestUrl2.toString())
                                .into(generateTarget(movie.getmId()));

                        if(result !=null)
                            showToast(getString(R.string.save_favorite));
                        else
                            showToast(getString(R.string.error_favorite));
                    }
                    setFavoriteIcon(misFavorite);
                }
            });
        }

        Log.i(TAG, "End MovieInformation");
        return view;
    }

    private Target generateTarget(final int nameFile){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        File myDir = new File(Environment.getExternalStorageDirectory().getPath() +File.separator + "MyMoviesPosters");

                        if (!myDir.exists()) {
                            myDir.mkdirs();
                        }

                        File file = new File(myDir,"Movie_"+nameFile+".jpg");
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e(TAG,"IOException : "+ e.getLocalizedMessage());
                        }
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };
        return target;
    }

    private void setFavoriteIcon(boolean isFavorite)
    {
        if(isFavorite)
            miv_favorite.setImageResource(R.drawable.ic_favorite);
        else
            miv_favorite.setImageResource(R.drawable.ic_favorite_border);
    }

    private void showToast(String message)
    {
        Toast toast =Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private ContentValues transformMovieToContentValues(Movie movie){
        ContentValues values = new ContentValues();
        values.put(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID, movie.getmId());
        values.put(FavoriteMovieContract.FavoriteMovie.COLUMN_TITLE, movie.getmTitle());
        values.put(FavoriteMovieContract.FavoriteMovie.COLUMN_ORIGINAL_TITLE, movie.getmOriginal_title());
        values.put(FavoriteMovieContract.FavoriteMovie.COLUMN_OVERVIEWS, movie.getmOverview());
        values.put(FavoriteMovieContract.FavoriteMovie.COLUMN_ORIGINAL_LANGUAGE, movie.getmOriginal_language());
        values.put(FavoriteMovieContract.FavoriteMovie.COLUMN_RELEASE_DATE, movie.getmRelease_date());
        values.put(FavoriteMovieContract.FavoriteMovie.COLUMN_POSTER_PATH, movie.getmPoster_path());
        values.put(FavoriteMovieContract.FavoriteMovie.COLUMN_BACKDROP_PATH, movie.getmBackdrop_path());
        values.put(FavoriteMovieContract.FavoriteMovie.COLUMN_VOTE_COUNT, movie.getmVote_count());
        values.put(FavoriteMovieContract.FavoriteMovie.COLUMN_VOTE_AVERAGE, movie.getmVote_average());
        values.put(FavoriteMovieContract.FavoriteMovie.COLUMN_ADULT, movie.ismAdult());
        values.put(FavoriteMovieContract.FavoriteMovie.COLUMN_VIDEO, movie.ismVideo());
        values.put(FavoriteMovieContract.FavoriteMovie.COLUMN_POPULARITY, movie.getmPopularity());

        return values;
    }

    private boolean isFavorite(int id) {
        Log.i(TAG, "start isFavorite "+ id);
        Uri uri = FavoriteMovieContract.FavoriteMovie.buildMovieUri(id);
        Log.i(TAG, "URI : "+ uri);
        String[] projection = {FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID};
        boolean isfavorite = false;

        Cursor cursor = getActivity().getContentResolver().query(uri,projection,null,null,null );

        if (cursor.getCount() > 0){
            isfavorite = true;
        }

        cursor.close();
        Log.i(TAG, "end isFavorite "+ id);
        return isfavorite;
    }
}
