package com.example.flickster.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {
    // values from API
    private String title;
    private String overview;
    private String posterPath; // only path of url
    private String backdropPath;

    //initialize from JSON data constructor
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}
