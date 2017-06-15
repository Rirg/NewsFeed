package com.example.android.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**Adapter for the list of news*/
    public static NewsAdapter mAdapter;

    /** URL for news data */
    private static final String NEWS_REQUEST_URL = "https://newsapi.org/v1/articles";

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;

    /** TextView that is displayed when the list is empty*/
    private TextView mEmptyStateTextView;



    public static final String LOG_TAG = NewsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        //Find a reference to the (@link GridView) in the layout
        ListView newsListView = (ListView) findViewById(R.id.list_view);

        //Create a new {@link ArrayAdapter} of news
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        //Set the adapter on the {@link GridView}
        //so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);

        //Set an item click listener on the GirdView, which sends an intent to a web browser
        //to open a website with more information about the selected new.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){

                //Create a new intent to view the new URI
                Intent intent = new Intent(NewsActivity.this, DetailsActivity.class);
                intent.putExtra("position", position);

                //Send the intent to lunch a new activity
                startActivity(intent);

            }
        });

        //Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get details on the currently active default data network
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();

        //If there is a network connection, fetch data
        if(networkInfo != null && networkInfo.isConnected()){
            //Get a reference to the LoadManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            //Initialize the loader. Pass the int ID constant defined above and pass in null for the
            //bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid because
            //this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }
        else{
            //Otherwise, display error
            //First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);

            //Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sharedSource = sharedPrefs.getString(
                getString(R.string.settings_source_key),
                getString(R.string.settings_source_default));
        String sharedOrder = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        Uri baseUri = Uri.parse(NEWS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        Log.d("SHARED PREF",sharedSource + " " +sharedOrder);

        uriBuilder.appendQueryParameter("source", sharedSource);
        uriBuilder.appendQueryParameter("sortBy", sharedOrder);
        uriBuilder.appendQueryParameter("apiKey", "e9d2d590d048448ba5b97b8f7cce88a4");

        Log.d("API URL",uriBuilder.toString());
        return new NewsLoader(this, uriBuilder.toString());
    }
    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        //Set empty state text to display "No news found."
        mEmptyStateTextView.setText(R.string.no_news);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        //Clear the adapter of the previous new data
        mAdapter.clear();

        //If there is a valid list of {@link News}s, then add them to the adapter's data set.
        //This will trigger the GridView to update.
        if(news != null && !news.isEmpty()){
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        //Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
