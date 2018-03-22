package com.pklein.popularmovies;

import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.pklein.popularmovies.data.Movie;
import com.pklein.popularmovies.tools.JsonUtils;
import com.pklein.popularmovies.tools.NetworkUtils;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

        mRecyclerView = findViewById(R.id.recyclerview_poster);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mLayoutManager= new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPosterAdapter = new PosterAdapter();
        mRecyclerView.setAdapter(mPosterAdapter);

        // if there is no internet connection show error message
        if(!NetworkUtils.isconnected((ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE)))
        {
            showErrorMessage();
        }
        else {
            if (savedInstanceState != null) { // if a filter is already saved, read it
                if (savedInstanceState.containsKey(LIFECYCLE_MOVIE_FILTER_KEY)) {
                    mMovie_filter = savedInstanceState.getString(LIFECYCLE_MOVIE_FILTER_KEY);
                }
            } else {
                mMovie_filter = "popular";
            }
            loadMovieData(mMovie_filter);
        }
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

            /* If there's no URL, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String filter = params[0];
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

        @Override
        protected void onPostExecute(List<Movie> movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showPosterListView();
                mPosterAdapter.setMovieData(movieData);
                // if a scroll position is saved, read it.
                if(mSavedRecyclerViewState!=null){
                    mLayoutManager.onRestoreInstanceState(mSavedRecyclerViewState);
                }
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
            //clear save instance to avoid bad scroll position when we switch between item menu
            mSavedRecyclerViewState = null;
            loadMovieData(mMovie_filter);
            return true;
        }

        if (id == R.id.action_top_rated) {
            mMovie_filter = "top_rated";
            //clear save instance to avoid bad scroll position when we switch between item menu
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
