package com.pklein.popularmovies.tools;

import android.util.Log;

import com.pklein.popularmovies.data.Movie;
import com.pklein.popularmovies.data.Review;
import com.pklein.popularmovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String TAG= "JsonUtils";

    private static final String JSON_RESULTS = "results";
    private static final String JSON_ID = "id";

    //for movie Information
    private static final String JSON_VOTE_COUNT = "vote_count";
    private static final String JSON_VIDEO = "video";
    private static final String JSON_VOTE_AVERAGE = "vote_average";
    private static final String JSON_TITLE = "title";
    private static final String JSON_POPULARITY = "popularity";
    private static final String JSON_POSTER_PATH = "poster_path";
    private static final String JSON_ORIGINAL_LANGUAGE = "original_language";
    private static final String JSON_ORIGINAL_TITLE = "original_title";
    private static final String JSON_GENRE_IDS = "genre_ids";
    private static final String JSON_BACKDROP_PATH = "backdrop_path";
    private static final String JSON_ADULT = "adult";
    private static final String JSON_OVERVIEW = "overview";
    private static final String JSON_RELEASE_DATE = "release_date";

    //for Reviews
    private static final String JSON_AUTHOR = "author";
    private static final String JSON_CONTENT = "content";
    private static final String JSON_URL = "url";

    // for TRAILERS
    private static final String JSON_ISO_639_1 = "iso_639_1";
    private static final String JSON_ISO_3166_1 = "iso_3166_1";
    private static final String JSON_KEY = "key";
    private static final String JSON_NAME = "name";
    private static final String JSON_SITE = "site";
    private static final String JSON_SIZE = "size";
    private static final String JSON_TYPE = "type";

    /**
     * This method returns a list of Movie described inside a JSON file
     *
     * @param json  The Json with the list of the movies to parse
     * @return List<Movie> : a list of Movie objects
     */
    public static List<Movie> parseMovieJson(String json) throws JSONException {

        Log.i(TAG, "Start parseMovieJson");

        JSONObject MovieJson = new JSONObject(json);
        List<Movie> ListMovie = new ArrayList<>();

        if (MovieJson.has(JSON_RESULTS)) {
            JSONArray ListMoviesJson = MovieJson.getJSONArray(JSON_RESULTS);

            for (int i = 0; i < ListMoviesJson.length(); i++) {

                JSONObject obj = ListMoviesJson.getJSONObject(i);
                Movie mov = new Movie();

                if(obj.has(JSON_VOTE_COUNT)){ mov.setmVote_count(obj.optInt(JSON_VOTE_COUNT));}
                if(obj.has(JSON_ID)){ mov.setmId(obj.optInt(JSON_ID));}
                if(obj.has(JSON_VIDEO)){ mov.setmVideo(obj.optBoolean(JSON_VIDEO));}
                if(obj.has(JSON_VOTE_AVERAGE)){ mov.setmVote_average(obj.optDouble(JSON_VOTE_AVERAGE));}
                if(obj.has(JSON_TITLE)){ mov.setmTitle(obj.optString(JSON_TITLE));}
                if(obj.has(JSON_POPULARITY)){ mov.setmPopularity(obj.optDouble(JSON_POPULARITY));}
                if(obj.has(JSON_POSTER_PATH)){ mov.setmPoster_path(obj.optString(JSON_POSTER_PATH));}
                if(obj.has(JSON_ORIGINAL_LANGUAGE)){ mov.setmOriginal_language(obj.optString(JSON_ORIGINAL_LANGUAGE));}
                if(obj.has(JSON_ORIGINAL_TITLE)){ mov.setmOriginal_title(obj.optString(JSON_ORIGINAL_TITLE));}
                if(obj.has(JSON_BACKDROP_PATH)){ mov.setmBackdrop_path(obj.optString(JSON_BACKDROP_PATH));}
                if(obj.has(JSON_ADULT)){ mov.setmAdult(obj.optBoolean(JSON_ADULT));}
                if(obj.has(JSON_OVERVIEW)){ mov.setmOverview(obj.optString(JSON_OVERVIEW));}
                if(obj.has(JSON_RELEASE_DATE)){ mov.setmRelease_date(obj.optString(JSON_RELEASE_DATE));}

                if(obj.has(JSON_GENRE_IDS)){
                    int[] ids = new int[obj.getJSONArray(JSON_GENRE_IDS).length()];
                    for (int j = 0; j < obj.getJSONArray(JSON_GENRE_IDS).length(); j++) {
                        ids[j]=obj.getJSONArray(JSON_GENRE_IDS).optInt(j);
                    }
                    mov.setmGenre_ids(ids);
                }

                ListMovie.add(mov);
            }
        }

        Log.i(TAG, "End parseMovieJson");
        return ListMovie;
    }

    /**
     * This method returns a list of Reviews described inside a JSON file
     *
     * @param json  The Json with the list of the reviews to parse
     * @return List<Review> : a list of Review objects
     */
    public static List<Review> parseReviewJson(String json) throws JSONException {
        Log.i(TAG, "Start parseReviewJson");

        JSONObject ReviewJson = new JSONObject(json);
        List<Review> ListReviews = new ArrayList<>();

        if (ReviewJson.has(JSON_RESULTS)) {
            JSONArray ListReviewsJson = ReviewJson.getJSONArray(JSON_RESULTS);

            for (int i = 0; i < ListReviewsJson.length(); i++) {

                JSONObject obj = ListReviewsJson.getJSONObject(i);
                Review rev = new Review();

                if(obj.has(JSON_AUTHOR)){rev.setmAuthor(obj.optString(JSON_AUTHOR));}
                if(obj.has(JSON_CONTENT)){rev.setmContent(obj.optString(JSON_CONTENT));}
                if(obj.has(JSON_ID)){rev.setmId(obj.optString(JSON_ID));}
                if(obj.has(JSON_URL)){rev.setmUrl(obj.optString(JSON_URL));}

                Log.i(TAG, "INFO :"+obj.optString(JSON_AUTHOR));
                ListReviews.add(rev);
            }
        }

        Log.i(TAG, "End parseReviewJson");
        return ListReviews;
    }

    /**
     * This method returns a list of Trailers described inside a JSON file
     *
     * @param json  The Json with the list of the trailers to parse
     * @return List<Trailer> : a list of Trailer objects
     */
    public static List<Trailer> parseTrailerJson(String json) throws JSONException {
        Log.i(TAG, "Start parseTrailerJson");

        JSONObject ReviewJson = new JSONObject(json);
        List<Trailer> ListTrailers = new ArrayList<>();

        if (ReviewJson.has(JSON_RESULTS)) {
            JSONArray ListReviewsJson = ReviewJson.getJSONArray(JSON_RESULTS);

            for (int i = 0; i < ListReviewsJson.length(); i++) {

                JSONObject obj = ListReviewsJson.getJSONObject(i);
                Trailer trailer = new Trailer();

                if(obj.has(JSON_ID)){trailer.setmId(obj.optString(JSON_ID));}
                if(obj.has(JSON_ISO_639_1)){trailer.setmIso_639_1(obj.optString(JSON_ISO_639_1));}
                if(obj.has(JSON_ISO_3166_1)){trailer.setmIso_3166_1(obj.optString(JSON_ISO_3166_1));}
                if(obj.has(JSON_KEY)){trailer.setmKey(obj.optString(JSON_KEY));}
                if(obj.has(JSON_NAME)){trailer.setmName(obj.optString(JSON_NAME));}
                if(obj.has(JSON_SITE)){trailer.setmSite(obj.optString(JSON_SITE));}
                if(obj.has(JSON_TYPE)){trailer.setmType(obj.optString(JSON_TYPE));}
                if(obj.has(JSON_SIZE)){trailer.setmSize(obj.optInt(JSON_SIZE));}

                Log.i(TAG, "INFO :"+obj.optString(JSON_NAME));

                ListTrailers.add(trailer);
            }
        }

        Log.i(TAG, "End parseTrailerJson");
        return ListTrailers;
    }
}
