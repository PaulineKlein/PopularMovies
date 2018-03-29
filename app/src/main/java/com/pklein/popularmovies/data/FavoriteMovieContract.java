package com.pklein.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMovieContract {

    public static final String CONTENT_AUTHORITY = "com.pklein.popularmovies.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class FavoriteMovie implements BaseColumns {
        public static final String TABLE_NAME = "FavoriteMovie";
        public static final String COLUMN_MOVIE_ID = "Movie_Id";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_VOTE_COUNT = "Vote_count";
        public static final String COLUMN_VOTE_AVERAGE = "Vote_average";
        public static final String COLUMN_OVERVIEWS = "Overview";
        public static final String COLUMN_RELEASE_DATE = "Release_date";
        public static final String COLUMN_ORIGINAL_TITLE = "Original_title";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "Original_language";
        public static final String COLUMN_ADULT = "Adult";
        public static final String COLUMN_VIDEO = "Video";
        public static final String COLUMN_POPULARITY = "Popularity";
        public static final String COLUMN_POSTER_PATH = "Poster_path";
        public static final String COLUMN_BACKDROP_PATH = "Backdrop_path";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // for building URIs on insertion
        public static Uri buildMovieUri(int id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}

