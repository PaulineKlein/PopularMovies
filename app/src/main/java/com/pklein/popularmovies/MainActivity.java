package com.pklein.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pklein.popularmovies.data.Movie;
import com.pklein.popularmovies.tools.JsonUtils;
import com.pklein.popularmovies.tools.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import static com.pklein.popularmovies.tools.JsonUtils.parseMovieJson;

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

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_poster);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        StaggeredGridLayoutManager layoutManager= new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);

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


    public class FetchPosterTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {

            /* If there's no URL, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String filter = params[0];
            URL movieRequestUrl = NetworkUtils.buildListUrl(filter);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                mListMovie = JsonUtils.parseMovieJson(jsonMovieResponse);

                String[] listPoster = new String[mListMovie.size()];
                for (int i=0; i<mListMovie.size(); i++) {
                    URL posterRequestUrl = NetworkUtils.buildPosterUrl(mListMovie.get(i).getmPoster_path());
                    listPoster[i]= posterRequestUrl.toString();
                }

                return listPoster;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movieData) {
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
