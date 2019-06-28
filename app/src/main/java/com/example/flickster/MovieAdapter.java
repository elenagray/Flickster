package com.example.flickster;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flickster.models.Config;
import com.example.flickster.models.Movie;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter  extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    // list of movies
    ArrayList<Movie> movies;
    // define config as a field here for image url
    Config config;
    //context for any rendering
    Context context;

    //initialize with list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    //creates and inflates a new view
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //get context from parent
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create the view using item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        // return new View Holder
        return new ViewHolder(movieView);
    }

    @Override
    //binds an inflated view to a new item
    public void onBindViewHolder(ViewHolder holder, int position) {
        // get movie data at specified position
        Movie movie = movies.get(position);
        //populate view with movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        //determine portrait or landscape
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        String imageUrl = null;
        //build url for poster image
        if(isPortrait){
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        }
        else{
            imageUrl = config.getImageUrl(config.getBackdropSize(),movie.getBackdropPath());
        }

        //get placeholder and imageview for current orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;


        //load image using glid
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 20, 0))//radius = how rounded, margin = how much cropped
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(imageView);
    }

    //return total number of items in list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create view holder as static inner class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // track view objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivBackdropImage;

        public ViewHolder(View itemView) {
            super(itemView);
            //lookup view object by id
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage) ;
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

        }
    }
}
