package com.example.home.popularmovies.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.home.popularmovies.Models.Movie;
import com.example.home.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by home on 10/25/2015.
 */
public class MoviesAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MoviesAdapter.class.getSimpleName();

    public MoviesAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
                Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(
                    getContext()).inflate(R.layout.list_item_movie, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.list_item_poster);
        Picasso.with(getContext()).load(movie.getPosterURL()).into(iconView);

        return convertView;
    }
}
