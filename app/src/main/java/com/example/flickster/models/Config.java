package com.example.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    //base url for loading images
    String imageBaseUrl;
    //poster size to use when fetching imagines, part of url
    String posterSize;
    //backdrop size (for landscape view)
    String backdropSize;


    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        // get image base url
        imageBaseUrl = images.getString("secure_base_url");
        // get poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //use option at index 3 or w342 as fall back
        posterSize = posterSizeOptions.optString(3, "w342");
        // parse backdrop size use index 1 or w780
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }
    // helper method to construct url
    public String getImageUrl(String size, String path){
        return String.format("%s%s%s", imageBaseUrl, size, path); //connect parts into one url
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
