package com.pklein.popularmovies;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pklein.popularmovies.data.Movie;
import com.pklein.popularmovies.data.Trailer;
import com.pklein.popularmovies.tools.JsonUtils;
import com.pklein.popularmovies.tools.NetworkUtils;

import java.net.URL;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class MovieTrailers extends Fragment {
    private static final String TAG= MovieTrailers.class.getSimpleName();
    private Movie movie;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private TrailersAdapter mTrailersAdapter;
    private LinearLayoutManager mLayoutManager;
    private View mTrailerview;
    private List<Trailer> mListTrailer;

    public static MovieTrailers newInstance() {
        Bundle args = new Bundle();
        MovieTrailers fragment = new MovieTrailers();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "Start MovieTrailers");
        super.onCreate(savedInstanceState);
        mTrailerview = inflater.inflate(R.layout.movie_reviews_trailers, container, false);

        Intent intentThatStarted = getActivity().getIntent();

        if(intentThatStarted.hasExtra("Movie")) {
            movie = intentThatStarted.getExtras().getParcelable("Movie");
        }

        mRecyclerView = mTrailerview.findViewById(R.id.recyclerview_revtrail);
        mLoadingIndicator = mTrailerview.findViewById(R.id.pb_loading_indicator_revtrail);
        mErrorMessageDisplay = mTrailerview.findViewById(R.id.tv_error_message_display_revtrail);

        mLayoutManager= new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mTrailersAdapter = new TrailersAdapter();
        mRecyclerView.setAdapter(mTrailersAdapter);

        loadTrailerData(movie.getmId());

        Log.i(TAG, "End MovieInformation");
        return mTrailerview;
    }

    private void loadTrailerData(int movieId) {
        showReviewListView();
        new FetchPosterTask().execute(movieId);
    }

    public class FetchPosterTask extends AsyncTask<Integer, Void, List<Trailer>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Trailer> doInBackground(Integer... params) {

            if (params.length == 0) {
                return null;
            }
            int movieId = params[0];

            if(!NetworkUtils.isconnected((ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE)))
            {
                return null;
            }
            else {
                URL reviewRequestUrl = NetworkUtils.buildMovieDetailUrl(movieId, "videos");
                try {
                    String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);
                    mListTrailer = JsonUtils.parseTrailerJson(jsonReviewResponse);
                    return mListTrailer;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        @Override
        protected void onPostExecute(List<Trailer> ReviewData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (ReviewData != null) {
                showReviewListView();
                mTrailersAdapter.setReviewData(ReviewData);
            } else {
                showErrorMessage();
            }
        }
    }

    private void showReviewListView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}
