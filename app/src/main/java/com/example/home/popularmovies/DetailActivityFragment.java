package com.example.home.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.home.popularmovies.Models.MovieReview;
import com.example.home.popularmovies.fetchingData.FetchDetailsTask;
import com.example.home.popularmovies.fetchingData.FetchReviewsTask;
import com.example.home.popularmovies.fetchingData.FetchTrailersTask;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

import me.grantland.widget.AutofitTextView;

//import com.example.home.popularmovies.Adapters.ReviewsAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    public TextView textViewForReviews;
    public TextView textViewFortrailers;


//    public ReviewsAdapter reviewsAdapter;
    public ArrayList<MovieReview> reviewList;


    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    public AutofitTextView mTitleTextView;
    public TextView mReleaseYearTextView;
    public TextView mDurationTextView;
    public TextView mRatingsTextView;
    public ExpandableTextView mOverviewTextView;
    public ImageView mPosterImageTextView;
    public LinearLayout mLinearLayout;
    public LinearLayout tLinearLayout;


    public DetailActivityFragment() {
    }

    private void getMovieInfo(String movieID) {
        FetchDetailsTask movieInfoTask = new FetchDetailsTask(this);
        movieInfoTask.execute(movieID);

    }

    private void getReviews(String movieID) {
        FetchReviewsTask reviewTask = new FetchReviewsTask(this);
        reviewTask.execute(movieID);
    }
    private void getTrailers(String movieID){
        FetchTrailersTask trailersTask = new FetchTrailersTask(this);
        trailersTask.execute(movieID);
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String movieID = intent.getStringExtra(Intent.EXTRA_TEXT);
            getMovieInfo(movieID);
            getReviews(movieID);
            getTrailers(movieID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);



        mTitleTextView = (AutofitTextView) rootView.findViewById(R.id.titleTextView);
        mReleaseYearTextView = (TextView) rootView.findViewById(R.id.yearOfReleaseTextView);
        mDurationTextView = (TextView) rootView.findViewById(R.id.durationTextView);
        mRatingsTextView = (TextView) rootView.findViewById(R.id.ratingsTextView);
        mPosterImageTextView = (ImageView) rootView.findViewById(R.id.posterImageView);
        mOverviewTextView = (ExpandableTextView) rootView.findViewById(R.id.expand_text_view);
        mLinearLayout = (LinearLayout)rootView.findViewById(R.id.containerForReviews);
        tLinearLayout = (LinearLayout)rootView.findViewById(R.id.containerForTrailers);


        return rootView;
    }

}

