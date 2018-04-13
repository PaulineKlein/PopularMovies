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
import com.pklein.popularmovies.data.Review;
import com.pklein.popularmovies.tools.JsonUtils;
import com.pklein.popularmovies.tools.NetworkUtils;

import java.net.URL;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class MovieReviews extends Fragment {
    private static final String TAG= MovieReviews.class.getSimpleName();
    private Movie movie;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private ReviewsAdapter mReviewsAdapter;
    private LinearLayoutManager mLayoutManager;
    private View mReviewview;
    private List<Review> mListReview;

    public static MovieReviews newInstance() {
        Bundle args = new Bundle();
        MovieReviews fragment = new MovieReviews();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "Start MovieReviews");
        super.onCreate(savedInstanceState);
        mReviewview = inflater.inflate(R.layout.movie_reviews_trailers, container, false);

        Intent intentThatStarted = getActivity().getIntent();

        if(intentThatStarted.hasExtra("Movie")) {
            movie = intentThatStarted.getExtras().getParcelable("Movie");
        }

        mRecyclerView = mReviewview.findViewById(R.id.recyclerview_revtrail);
        mLoadingIndicator = mReviewview.findViewById(R.id.pb_loading_indicator_revtrail);
        mErrorMessageDisplay = mReviewview.findViewById(R.id.tv_error_message_display_revtrail);

        mLayoutManager= new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mReviewsAdapter = new ReviewsAdapter();
        mRecyclerView.setAdapter(mReviewsAdapter);

        loadReviewData(movie.getmId());

        Log.i(TAG, "End MovieInformation");
        return mReviewview;
    }

    private void loadReviewData(int movieId) {
        showReviewListView();
        new FetchPosterTask().execute(movieId);
    }

    public class FetchPosterTask extends AsyncTask<Integer, Void, List<Review>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Review> doInBackground(Integer... params) {

            if (params.length == 0) {
                return null;
            }
            int movieId = params[0];

            if(!NetworkUtils.isconnected((ConnectivityManager)getActivity().getSystemService(CONNECTIVITY_SERVICE)))
            {
                return null;
            }
            else {
                    URL reviewRequestUrl = NetworkUtils.buildMovieDetailUrl(movieId, "reviews");
                    try {
                        String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);
                        mListReview = JsonUtils.parseReviewJson(jsonReviewResponse);
                        return mListReview;

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }

        @Override
        protected void onPostExecute(List<Review> ReviewData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (ReviewData != null) {
                showReviewListView();
                mReviewsAdapter.setReviewData(ReviewData);
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
