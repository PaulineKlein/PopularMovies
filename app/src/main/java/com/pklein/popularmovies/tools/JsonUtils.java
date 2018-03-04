package com.pklein.popularmovies.tools;

import android.util.Log;

import com.pklein.popularmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String TAG= "JsonUtils";

    private static final String JSON_RESULTS = "results";
    private static final String JSON_VOTE_COUNT = "vote_count";
    private static final String JSON_ID = "id";
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
    private static final String JSON_OVERIEW = "overview";
    private static final String JSON_RELEASE_DATE = "release_date";

    public static List<Movie> parseMovieJson(String json) throws JSONException {

        Log.i(TAG, "Start parseMovieJson");

        JSONObject MovieJson = new JSONObject(json);
        List<Movie> ListMovie = new ArrayList<>();

        if (MovieJson.has(JSON_RESULTS)) {
            JSONArray ListMoviesJson = MovieJson.getJSONArray(JSON_RESULTS);

            for (int i = 0; i < ListMoviesJson.length(); i++) {

                Log.i(TAG, "title " + ListMoviesJson.getJSONObject(i).optString(JSON_TITLE));
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
                if(obj.has(JSON_OVERIEW)){ mov.setmOverview(obj.optString(JSON_OVERIEW));}
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

        Log.i(TAG, "End parseSandwichJson");
        return ListMovie;
    }

}
