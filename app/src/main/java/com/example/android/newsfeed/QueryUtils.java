package com.example.android.newsfeed;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.newsfeed.NewsActivity.LOG_TAG;

/**
 * Created by rirg9 on 3/21/2017.
 */

public class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object,
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed)
     */

    private QueryUtils(){

    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the URL is null, then return early.
        if(url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //If the request was successful (response code 200)
            //then read the input and parse the response.
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Query the News API dataset and return a list of {@link News} objects.
     */
    public static List<News> fetchNewData(String requestUrl) {

        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        //Extract relevant fields from the JSON response and create a list of {@link News)
        List<News> news = extractFeatureFromJson(jsonResponse);

        //Return the list of {@link News}
        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the whole JSON response from
     * the server
     */
    private static String readFromStream(InputStream inputStream) throws  IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<News> extractFeatureFromJson(String newJSON) {
        //If the JSON string is empty or null, then return early
        if(TextUtils.isEmpty(newJSON)){
            return null;
        }

        //Create an empty ArrayList that we can start adding news to
        ArrayList<News> news = new ArrayList<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(newJSON);

            JSONArray newsArray = baseJsonResponse.optJSONArray("articles");

            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i=0; i<newsArray.length(); i++){
                // Get a single new at position i within the list of articles
                JSONObject currentNew = newsArray.getJSONObject(i);

                // Extract the value for the key called "place"
                String title = currentNew.getString("title");

                // Extract the value for the key called "publishedAt"
                String date = currentNew.getString("publishedAt");

                // Extract the value for the key called "author"
                String  author = currentNew.getString("author");

                //Extract the value fot the key called "description"
                String description = currentNew.getString("description");

                // Extract the value for the key called "url"
                String url = currentNew.getString("url");

                // URL imageUrl = new URL(currentNew.getString("urlToImage"));
                String imageUrl = currentNew.getString("urlToImage");
                //Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                news.add(new News(author, title, url, imageUrl, description));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }


        //Return the list of news
        return news;
    }

}
