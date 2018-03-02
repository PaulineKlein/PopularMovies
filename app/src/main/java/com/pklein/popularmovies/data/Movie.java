package com.pklein.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by Pauline on 23/02/2018.
 */

public class Movie implements Parcelable {

    private int mVote_count;
    private int mId;
    private boolean mVideo;
    private double mVote_average;
    private String mTitle;
    private double  mPopularity;
    private String mPoster_path;
    private String mOriginal_language;
    private String mOriginal_title;
    private int[] mGenre_ids;
    private String mBackdrop_path;
    private boolean mAdult;
    private String mOverview;
    private String mRelease_date;

    public Movie(){
    }

    public Movie(int vote_count, int id, boolean video, double vote_average, String title, double popularity, String poster_path, String original_language, String original_title, int[] genre_ids, String backdrop_path, boolean adult, String overview, String release_date ){

        mVote_count = vote_count;
        mId = id;
        mVideo = video;
        mVote_average = vote_average;
        mTitle = title;
        mPopularity = popularity;
        mPoster_path = poster_path;
        mOriginal_language = original_language;
        mOriginal_title = original_title;
        mGenre_ids = genre_ids;
        mBackdrop_path = backdrop_path;
        mAdult = adult;
        mOverview = overview;
        mRelease_date=  release_date;
    }

    public Movie(Parcel in) {
        mVote_count = in.readInt();
        mId = in.readInt();
       //mVideo = (Boolean) in.readValue( null );
        mVideo = in.readInt() != 0;
        mVote_average = in.readDouble();
        mTitle = in.readString();
        mPopularity = in.readDouble();
        mPoster_path = in.readString();
        mOriginal_language = in.readString();
        mOriginal_title = in.readString();
        mGenre_ids = new int[in.readInt()];
        in.readIntArray(mGenre_ids);
        mBackdrop_path = in.readString();
        mAdult = in.readInt() != 0;
        mOverview = in.readString();
        mRelease_date=  in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mVote_count);
        dest.writeInt(mId);
        //dest.writeValue(mVideo);  // after I need to use boolean myBool = (Boolean) source.readValue( null );
        dest.writeInt(mVideo ? 1 : 0);
        dest.writeDouble(mVote_average);
        dest.writeString(mTitle);
        dest.writeDouble(mPopularity);
        dest.writeString(mPoster_path);
        dest.writeString(mOriginal_language);
        dest.writeString(mOriginal_title);
        dest.writeInt(mGenre_ids.length);
        dest.writeIntArray(mGenre_ids);
        dest.writeString(mBackdrop_path);
        dest.writeInt(mAdult ? 1 : 0);
        dest.writeString(mOverview);
        dest.writeString(mRelease_date);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getmVote_count() {
        return mVote_count;
    }

    public void setmVote_count(int mVote_count) {
        this.mVote_count = mVote_count;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public boolean ismVideo() {
        return mVideo;
    }

    public void setmVideo(boolean mVideo) {
        this.mVideo = mVideo;
    }

    public double getmVote_average() {
        return mVote_average;
    }

    public void setmVote_average(double mVote_average) {
        this.mVote_average = mVote_average;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public double getmPopularity() {
        return mPopularity;
    }

    public void setmPopularity(double mPopularity) {
        this.mPopularity = mPopularity;
    }

    public String getmPoster_path() {
        return mPoster_path;
    }

    public void setmPoster_path(String mPoster_path) {
        this.mPoster_path = mPoster_path;
    }

    public String getmOriginal_language() {
        return mOriginal_language;
    }

    public void setmOriginal_language(String mOriginal_language) {this.mOriginal_language = mOriginal_language; }

    public String getmOriginal_title() {
        return mOriginal_title;
    }

    public void setmOriginal_title(String mOriginal_title) { this.mOriginal_title = mOriginal_title;}

    public int[] getmGenre_ids() { return mGenre_ids; }

    public void setmGenre_ids(int[] mGenre_ids) {
        this.mGenre_ids = mGenre_ids;
    }

    public String getmBackdrop_path() {
        return mBackdrop_path;
    }

    public void setmBackdrop_path(String mBackdrop_path) {
        this.mBackdrop_path = mBackdrop_path;
    }

    public boolean ismAdult() {
        return mAdult;
    }

    public void setmAdult(boolean mAdult) {
        this.mAdult = mAdult;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmRelease_date() {
        return mRelease_date;
    }

    public void setmRelease_date(String mRelease_date) {
        this.mRelease_date = mRelease_date;
    }
}
