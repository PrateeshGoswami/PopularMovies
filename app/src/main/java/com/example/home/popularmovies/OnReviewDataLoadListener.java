package com.example.home.popularmovies;

import com.example.home.popularmovies.Models.MovieReview;

import java.util.ArrayList;

/**
 * Created by home on 1/28/2016.
 */
public interface OnReviewDataLoadListener {
    public void onDataReady(ArrayList<MovieReview> results);
}
