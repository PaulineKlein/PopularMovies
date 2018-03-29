package com.pklein.popularmovies;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pklein.popularmovies.data.FavoriteMovieContract;
import com.pklein.popularmovies.data.Movie;
import com.pklein.popularmovies.tools.JsonUtils;
import com.pklein.popularmovies.tools.NetworkUtils;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private PosterAdapter mPosterAdapter;
    private GridLayoutManager mLayoutManager;
    private List<Movie> mListMovie;
    private String mMovie_filter="";

    private Parcelable mSavedRecyclerViewState;
    private static final String RECYCLER_STATE = "recycler";
    private static final String LIFECYCLE_MOVIE_FILTER_KEY = "filter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // to be able to download and display image file for favorite filter
        if (shouldAskPermissions()) {
            askPermissions();
        }
        mRecyclerView = findViewById(R.id.recyclerview_poster);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mLayoutManager= new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPosterAdapter = new PosterAdapter();
        mRecyclerView.setAdapter(mPosterAdapter);

        if (savedInstanceState != null) { // if a filter is already saved, read it
            if (savedInstanceState.containsKey(LIFECYCLE_MOVIE_FILTER_KEY)) {
                mMovie_filter = savedInstanceState.getString(LIFECYCLE_MOVIE_FILTER_KEY);
            }
        } else {
            mMovie_filter = "popular";
        }
        loadMovieData(mMovie_filter);
    }

    private void loadMovieData(String filter) {
        showPosterListView();
        new FetchPosterTask().execute(filter);
    }

    public class FetchPosterTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            /* If there's no filter, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String filter = params[0];

            if(filter.equals("favorite")) // then we check data inside database :
            {
                mListMovie = getMoviesfromDatabase();
                return mListMovie;
            }
            else { // then we check data from the API :
                // if there is no internet connection show error message
                if(!NetworkUtils.isconnected((ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE)))
                {
                    return null;
                }
                else {
                    URL movieRequestUrl = NetworkUtils.buildListUrl(filter);
                    try {
                        String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                        mListMovie = JsonUtils.parseMovieJson(jsonMovieResponse);
                        return mListMovie;

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                // if a scroll position is saved, read it.
                if(mSavedRecyclerViewState!=null) {
                    mLayoutManager.onRestoreInstanceState(mSavedRecyclerViewState);
                }
                else { // create a new layout to return to top of the screen
                    mLayoutManager= new GridLayoutManager(getApplicationContext(),3);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                }
                showPosterListView();
                mPosterAdapter.setMovieData(movieData);
            } else {
                showErrorMessage();
            }
        }
    }

    private void showPosterListView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private List<Movie> getMoviesfromDatabase(){
        Log.i(TAG, "getMoviesfromDatabase ");

        List<Movie> ListMovie = new ArrayList<>();
        Uri uri =FavoriteMovieContract.FavoriteMovie.CONTENT_URI;
        String[] projection = null; // we want all columns return
        String sortOrder = FavoriteMovieContract.FavoriteMovie.COLUMN_TITLE;

        Cursor cursor = getContentResolver().query(uri,projection,null,null,sortOrder );

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Log.i(TAG,"COLUMN_MOVIE_ID " +cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID));
                Movie mov = new Movie();
                mov.setmVote_count(cursor.getInt(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_VOTE_COUNT)));
                mov.setmId(cursor.getInt(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID)));
                mov.setmVideo(cursor.getInt(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_VIDEO)) != 0);
                mov.setmVote_average(cursor.getInt(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_VOTE_AVERAGE)));
                mov.setmTitle(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_TITLE)));
                mov.setmPopularity(cursor.getInt(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_POPULARITY)));
                mov.setmPoster_path(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_POSTER_PATH)));
                mov.setmOriginal_language(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_ORIGINAL_LANGUAGE)));
                mov.setmOriginal_title(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_ORIGINAL_TITLE)));
                mov.setmBackdrop_path(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_BACKDROP_PATH)));
                mov.setmAdult(cursor.getInt(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_ADULT))!= 0);
                mov.setmOverview(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_OVERVIEWS)));
                mov.setmRelease_date(cursor.getString(cursor.getColumnIndex(FavoriteMovieContract.FavoriteMovie.COLUMN_RELEASE_DATE)));
                mov.setmGenre_ids(new int[1]);

                ListMovie.add(mov);
                cursor.moveToNext();
            }
        }
        cursor.close();

        Log.i(TAG, "end getMoviesfromDatabase ");
        return ListMovie;
    }

    /* with the Help of https://stackoverflow.com/questions/8854359/exception-open-failed-eacces-permission-denied-on-android */
    protected boolean shouldAskPermissions() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            || ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))){

            return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
        }
            return false;
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            mMovie_filter = "popular";
            mSavedRecyclerViewState = null;
            loadMovieData(mMovie_filter);
            return true;
        }

        if (id == R.id.action_top_rated) {
            mMovie_filter = "top_rated";
            mSavedRecyclerViewState = null;
            loadMovieData(mMovie_filter);
            return true;
        }

        if (id == R.id.action_favorite) {
            mMovie_filter = "favorite";
            mSavedRecyclerViewState = null;
            loadMovieData(mMovie_filter);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save filter + recyclerview position :
        outState.putString(LIFECYCLE_MOVIE_FILTER_KEY, mMovie_filter);
        outState.putParcelable(RECYCLER_STATE,mLayoutManager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //It will restore recycler view at same position
        if (savedInstanceState != null) {
            mSavedRecyclerViewState = savedInstanceState.getParcelable(RECYCLER_STATE);
        }
    }

}
