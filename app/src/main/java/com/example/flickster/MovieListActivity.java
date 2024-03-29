package com.example.flickster;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.flickster.models.Config;
import com.example.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    // constants for base URL for API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // parameter name for API key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging calls from class
    public final static String TAG = "MovieListActivity";

    //instance fields
    AsyncHttpClient client;
    //list of currently playing movies
    ArrayList<Movie> movies;
    // recycler view
    RecyclerView rvMovies;
    //the adapter wired to recycler view
    MovieAdapter adapter;
    //image config, use to track
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //initialize client
        client = new AsyncHttpClient();
        // initialize list of movies
        movies = new ArrayList<>();

        //initialize adapter -- movies array cant be reinitialized
        adapter = new MovieAdapter(movies);

        //resolve recycler view and connect layout manager and adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        //get configuration
        getConfiguration();
    }

    //get list of currently playing movies from API
    private void getNowPlaying(){
        //create the url
        String url = API_BASE_URL + "/movie/now_playing";
        // set request parameter
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));// API key always required
        client.get(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               //load results into movies list
                try{
                    JSONArray results = response.getJSONArray("results");
                    //iterate through results and create movie objects
                    for(int i = 0; i < results.length(); i++){
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        //notify adapter that row was added
                        adapter.notifyItemInserted(movies.size()-1);
                    }
                    Log.i(TAG, String.format("Loaded %s movie", results.length()));
                } catch (JSONException e){
                    logError("Failed to parse now playing movies", e, true);
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now_playing endpoint", throwable, true);
            }

        });

    }

    //get configuration from API
    private void getConfiguration(){
        //create the url
        String url = API_BASE_URL + "/configuration";
        // set request parameter
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));// API key always required
        client.get(url, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try{
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseURL %s and posterSize %s", config.getImageBaseUrl(), config.getPosterSize()));
                    getNowPlaying();
                    // pass config object to adapter
                    adapter.setConfig(config);
                } catch(JSONException e){
                    logError("Failed parsing configuration",e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration",throwable, true);

            }

        });
    }

    //handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser){
        // always log the error
        Log.e(TAG, message, error);
        //alert the user to avoid silent errors
        if(alertUser){
            // show a long toast with error message
            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();


        }
    }
}
