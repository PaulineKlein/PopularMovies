package com.pklein.popularmovies.tools;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.pklein.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";

    private static final String BASE_LIST_URL = "http://api.themoviedb.org/3/movie/";

    /* API KEY !! */
    private static final String API_KEY = "api_key";
    private static final String API_KEY_TO_USE = BuildConfig.API_KEY;

    /* PARAMETERS */
    private static final String size = "w342"; // or  "w92", "w154", "w185", "w342", "w500", "w780", "original"


    /**
     * Builds the URL used to talk to the API of themoviedb.org
     * this URL will return all the informations used to display movies poster
     *
     * @param filter The filter for the list of Movies : popular or top_rated
     * @return The URL to use to query the themoviedb server.
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

    /**
     * Builds the URL used to talk to the API of themoviedb.org
     * this URL will return the image of a movie poster
     *
     * @param PosterNumber The key of the poster we want to display
     * @param size The size of the poster we want to display (it can be "w92", "w154", "w185", "w342", "w500", "w780", "original")
     * @return The URL to use to query the themoviedb server.
     */
    public static URL buildPosterUrl(String PosterNumber, String size) {
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

    public static boolean isconnected(ConnectivityManager cm){

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean Connect = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            return Connect;
    }
}
