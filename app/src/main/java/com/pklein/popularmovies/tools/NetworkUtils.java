package com.pklein.popularmovies.tools;

import android.net.Uri;
import android.util.Log;

import com.pklein.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Pauline on 23/02/2018.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";

    private static final String BASE_LIST_URL = "http://api.themoviedb.org/3/movie/";

    /* API KEY !! */
    private static final String API_KEY = "api_key";
    private static final String API_KEY_TO_USE = BuildConfig.API_KEY;;

    /* PARAMETERS */
    private static final String format = "json";

   // private static final String filter = "popular"; // or top_rated

    final static String size = "w185"; // or  "w92", "w154", "w185", "w342", "w500", "w780", "original"


    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param filter The filter for the list of Movies : popular or top_rated
     * @return The URL to use to query the weather server.
     */
    public static URL buildListUrl(String filter) {
        Uri builtUri = Uri.parse(BASE_LIST_URL+filter).buildUpon()
                .appendQueryParameter(API_KEY, API_KEY_TO_USE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildPosterUrl(String PosterNumber) {
        Uri builtUri = Uri.parse(BASE_POSTER_URL+size+'/'+PosterNumber).buildUpon()
                .appendQueryParameter(API_KEY, API_KEY_TO_USE)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}