package com.pklein.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private List<Movie> mListMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recyclerview_poster);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager= new GridLayoutManager(this,3);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mPosterAdapter = new PosterAdapter();

        mRecyclerView.setAdapter(mPosterAdapter);

        loadMovieData();

    }

    private void loadMovieData() {
        showPosterListView();
       // String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchPosterTask().execute("popular");
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

                //String[] listPoster = new String[mListMovie.size()];
                //for (int i=0; i<mListMovie.size(); i++) {
                //    URL posterRequestUrl = NetworkUtils.buildPosterUrl(mListMovie.get(i).getmPoster_path());
                //    listPoster[i]= posterRequestUrl.toString();
                //}

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
}
