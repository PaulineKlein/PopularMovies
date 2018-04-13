package com.pklein.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class FavoriteMovieProvider extends ContentProvider {

    /* This class has been set up thanks to code from https://github.com/udacity/android-content-provider */
    private static final UriMatcher mUriMatcher = buildUriMatcher();
    private FavoriteMovieDbHelper mFMDbHelper;

    /* Codes for the UriMatcher */
    private static final int FAV_MOVIES = 100;
    private static final int FAV_MOVIES_WITH_ID = 101;


    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteMovieContract.CONTENT_AUTHORITY;

        // TYPE of URI :
        matcher.addURI(authority, FavoriteMovieContract.FavoriteMovie.TABLE_NAME, FAV_MOVIES);
        matcher.addURI(authority, FavoriteMovieContract.FavoriteMovie.TABLE_NAME + "/#", FAV_MOVIES_WITH_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {

        mFMDbHelper = new FavoriteMovieDbHelper(getContext());
        return true;
    }


    @Override
    public String getType(Uri uri){
        final int match = mUriMatcher.match(uri);

        switch (match){
            case FAV_MOVIES:{
                return FavoriteMovieContract.FavoriteMovie.CONTENT_DIR_TYPE;
            }
            case FAV_MOVIES_WITH_ID:{
                return FavoriteMovieContract.FavoriteMovie.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        Cursor retCursor;
        switch(mUriMatcher.match(uri)){
            // All Movies selected
            case FAV_MOVIES:{
                retCursor = mFMDbHelper.getReadableDatabase().query(
                        FavoriteMovieContract.FavoriteMovie.TABLE_NAME, // The table to query
                        projection,                                     // The columns to return
                        selection,                                      // The columns for the WHERE clause
                        selectionArgs,                                  // The values for the WHERE clause
                        null,                                   // don't group the rows
                        null,                                    // don't filter by row groups
                        sortOrder);                                    // The sort order
                return retCursor;
            }
            // Individual Movie based on Id selected
            case FAV_MOVIES_WITH_ID:{
                retCursor = mFMDbHelper.getReadableDatabase().query(
                        FavoriteMovieContract.FavoriteMovie.TABLE_NAME,                         // The table to query
                        projection,                                                             // The columns to return
                        FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID + " = ?",   // The columns for the WHERE clause
                        new String[] {String.valueOf(ContentUris.parseId(uri))},                // The values for the WHERE clause
                        null,                                                           // don't group the rows
                        null,                                                            // don't filter by row groups
                        sortOrder);                                                             // The sort order
                return retCursor;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        final SQLiteDatabase db = mFMDbHelper.getWritableDatabase();
        Uri returnUri =null;
        switch (mUriMatcher.match(uri)) {
            case FAV_MOVIES: {
                long _id = db.insert(FavoriteMovieContract.FavoriteMovie.TABLE_NAME, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = FavoriteMovieContract.FavoriteMovie.buildMovieUri((int)_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mFMDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int numDeleted;
        switch(match){
            case FAV_MOVIES:
                numDeleted = db.delete(FavoriteMovieContract.FavoriteMovie.TABLE_NAME, selection, selectionArgs);
                break;
            case FAV_MOVIES_WITH_ID:
                numDeleted = db.delete(FavoriteMovieContract.FavoriteMovie.TABLE_NAME,
                        FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mFMDbHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch(mUriMatcher.match(uri)){
            case FAV_MOVIES:{
                numUpdated = db.update(FavoriteMovieContract.FavoriteMovie.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case FAV_MOVIES_WITH_ID: {
                numUpdated = db.update(FavoriteMovieContract.FavoriteMovie.TABLE_NAME,
                        contentValues,
                        FavoriteMovieContract.FavoriteMovie.COLUMN_MOVIE_ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
