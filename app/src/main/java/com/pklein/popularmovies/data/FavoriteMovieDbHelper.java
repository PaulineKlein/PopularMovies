package com.pklein.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {

    private static final String TAG= FavoriteMovieDbHelper.class.getSimpleName();

    // The Database name
    private static final String DATABASE_NAME = "FavoriteMovie.db";

    // If we change the Database schema, we must increment the Database version
    private static final int DATABASE_VERSION = 1;

    public FavoriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create the table
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + FavoriteMovieContract.FavoriteMovie.TABLE_NAME + " (" +
                FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID + " INTEGER NOT NULL PRIMARY KEY, " +
                FavoriteMovieContract.FavoriteMovie.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMovieContract.FavoriteMovie.COLUMN_ORIGINAL_TITLE + " TEXT NULL, " +
                FavoriteMovieContract.FavoriteMovie.COLUMN_OVERVIEWS + " TEXT NULL, " +
                FavoriteMovieContract.FavoriteMovie.COLUMN_ORIGINAL_LANGUAGE + " TEXT NULL, " +
                FavoriteMovieContract.FavoriteMovie.COLUMN_RELEASE_DATE + " TEXT NULL, " +
                FavoriteMovieContract.FavoriteMovie.COLUMN_POSTER_PATH + " TEXT NULL, " +
                FavoriteMovieContract.FavoriteMovie.COLUMN_BACKDROP_PATH + " TEXT NULL, " +
                FavoriteMovieContract.FavoriteMovie.COLUMN_VOTE_COUNT + " INTEGER NULL, " +
                FavoriteMovieContract.FavoriteMovie.COLUMN_VOTE_AVERAGE + " INTEGER NULL, " +
                FavoriteMovieContract.FavoriteMovie.COLUMN_ADULT + " INTEGER NULL, " +
                FavoriteMovieContract.FavoriteMovie.COLUMN_VIDEO + " INTEGER NULL, " +
                FavoriteMovieContract.FavoriteMovie.COLUMN_POPULARITY + " INTEGER NULL " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w(TAG, "Upgrading database from version " + i + " to " + i1 + ". OLD DATA WILL BE DESTROYED");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieContract.FavoriteMovie.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
