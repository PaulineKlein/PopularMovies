package com.pklein.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.pklein.popularmovies.data.Movie;
import com.pklein.popularmovies.tools.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MovieInformation extends AppCompatActivity {
    private TextView mtv_title;
    private TextView mtv_release_date;
    private TextView mtv_overview;
    private TextView mtv_vote_counting;
    private ImageView miv_back;
    private ImageView miv_thumbnail;
    private RatingBar mratingBar;
    private static final String TAG= "MovieInformation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "Start MovieInformation");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information);

        mtv_title = findViewById(R.id.tv_title);
        mtv_release_date = findViewById(R.id.tv_release_date);
        mtv_overview = findViewById(R.id.tv_overview);
        mtv_vote_counting = findViewById(R.id.tv_vote_counting);
        miv_back = findViewById(R.id.image_iv_back);
        miv_thumbnail = findViewById(R.id.image_iv_thumbnail);
        mratingBar = findViewById(R.id.ratingBar);

        Intent intentThatStarted = getIntent();

        if(intentThatStarted.hasExtra("Movie")){
            Movie movie= intentThatStarted.getExtras().getParcelable("Movie");
            mtv_title.setText(movie.getmTitle());
            mtv_overview.setText(movie.getmOverview());
            mtv_vote_counting.setText(movie.getmVote_count() +" votes");

            mratingBar.setRating((float)(movie.getmVote_average())/2);
            Log.i(TAG, "rate " + (float) movie.getmVote_average());

            String[] dateseparated = movie.getmRelease_date().split("-");
            mtv_release_date.setText(dateseparated[2]+"/"+dateseparated[1]+"/"+dateseparated[0]);

            URL posterRequestUrl = NetworkUtils.buildPosterUrl(movie.getmPoster_path());
            Picasso.with(this)
                    .load(posterRequestUrl.toString())
                    .into(miv_back);

            Picasso.with(this)
                    .load(posterRequestUrl.toString())
                    .into(miv_thumbnail);
        }

        Log.i(TAG, "End MovieInformation");
    }
}
