package com.example.home.popularmovies;

import com.example.home.popularmovies.Models.Trailer;

import java.util.ArrayList;

/**
 * Created by home on 1/28/2016.
 */
public interface OnTrailerDataLoadListener {
    public void onTrailerDataReady(ArrayList<Trailer> results);

}
