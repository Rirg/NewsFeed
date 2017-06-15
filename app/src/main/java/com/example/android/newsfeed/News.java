package com.example.android.newsfeed;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Created by rirg9 on 3/21/2017.
 */

public class News {

    /** Article author's name*/
    private String mAuthor;

    /** Title of the article*/
    private String mTitle;

    /** Detail information URL of the article*/
    private String mUrl;

    /** Bitmap Image of the article */
    private String mImageUrl = null;

    private String mDescription;

    /** Publish date of the article */
    private String mDate;



    /** Constructor for a new NEWS OBJECT */
    public News(String author, String title, String url, String imageURL, String description){
        mAuthor = author;
        mTitle = title;
        mUrl = url;
        mImageUrl = imageURL;
        mDescription = description;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getDate() {
        return mDate;
    }

    public String getDescription() {
        return  mDescription;
    }
}
