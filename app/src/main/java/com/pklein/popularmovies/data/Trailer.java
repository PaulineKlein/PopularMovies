package com.pklein.popularmovies.data;


public class Trailer {

    private String mId;
    private String mIso_639_1;
    private String mIso_3166_1;
    private String mKey;
    private String mName;
    private String mSite;
    private int mSize;
    private String mType;

    public Trailer(){
    }

    public Trailer(String mId, String mIso_639_1, String mIso_3166_1, String mKey, String mName, String mSite, int mSize, String mType) {
        this.mId = mId;
        this.mIso_639_1 = mIso_639_1;
        this.mIso_3166_1 = mIso_3166_1;
        this.mKey = mKey;
        this.mName = mName;
        this.mSite = mSite;
        this.mSize = mSize;
        this.mType = mType;
    }


    /* GETTER and SETTER */

    public String getmId() { return mId; }
    public void setmId(String mId) {this.mId = mId; }

    public String getmIso_639_1() { return mIso_639_1; }
    public void setmIso_639_1(String mIso_639_1) { this.mIso_639_1 = mIso_639_1; }

    public String getmIso_3166_1() { return mIso_3166_1; }
    public void setmIso_3166_1(String mIso_3166_1) { this.mIso_3166_1 = mIso_3166_1; }

    public String getmKey() { return mKey; }
    public void setmKey(String mKey) { this.mKey = mKey; }

    public String getmName() { return mName; }
    public void setmName(String mName) { this.mName = mName; }

    public String getmSite() { return mSite; }
    public void setmSite(String mSite) { this.mSite = mSite; }

    public int getmSize() {  return mSize; }
    public void setmSize(int mSize) { this.mSize = mSize; }

    public String getmType() { return mType; }
    public void setmType(String mType) {this.mType = mType; }
}
