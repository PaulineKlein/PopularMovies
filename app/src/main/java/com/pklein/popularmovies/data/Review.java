package com.pklein.popularmovies.data;


public class Review {

    private String mAuthor;
    private String mContent;
    private String mId;
    private String mUrl;

    public Review(){
    }

    public Review(String author, String content, String id, String url){
        mAuthor =author;
        mContent = content;
        mId= id;
        mUrl = url;
    }

    /* GETTER and SETTER */

    public String getmAuthor() { return mAuthor; }
    public void setmAuthor(String mAuthor) { this.mAuthor = mAuthor; }

    public String getmContent() { return mContent; }
    public void setmContent(String mContent) { this.mContent = mContent; }

    public String getmId() { return mId; }
    public void setmId(String mId) { this.mId = mId; }

    public String getmUrl() { return mUrl; }
    public void setmUrl(String mUrl) { this.mUrl = mUrl; }
}
