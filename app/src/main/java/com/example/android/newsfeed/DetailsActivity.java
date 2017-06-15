package com.example.android.newsfeed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import static com.example.android.newsfeed.NewsActivity.mAdapter;
import static com.example.android.newsfeed.R.id.webView;

/**
 * Created by rirg9 on 3/24/2017.
 */

public class DetailsActivity extends AppCompatActivity {

    ProgressBar mProgressBar;
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        //mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_details);

        mWebView = (WebView) findViewById(webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());

        // Set a new WebView client to avoid the problem of opening a default browser

        // Get position from the bundle
        Bundle bundle= getIntent().getExtras();

        //Find the current new that was clicked on
        News currentNew = mAdapter.getItem(bundle.getInt("position"));

        // Finally, load the WebView with the URL passed from the NewsActivity
        mWebView.loadUrl(currentNew.getUrl());

        // TODO set a progress bar for the loading URL

        /*mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                mProgressBar.setProgress(progress);
                if (progress == 100) {
                    mProgressBar.setVisibility(View.GONE);

                } else {
                    mProgressBar.setVisibility(View.VISIBLE);

                }
            }
        });*/


    }

}
