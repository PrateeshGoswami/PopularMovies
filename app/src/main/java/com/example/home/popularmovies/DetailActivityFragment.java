package com.example.home.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.home.popularmovies.Adapters.ReviewsAdapter;
import com.example.home.popularmovies.Models.MovieReview;
import com.example.home.popularmovies.fetchingData.FetchDetailsTask;
import com.example.home.popularmovies.fetchingData.FetchReviewsTask;

import java.util.ArrayList;

import me.grantland.widget.AutofitTextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    public LinearLayout myLInearLayout;
    public TextView textViewForReviews;
    public TextView textViewFortrailers;


    public ReviewsAdapter reviewsAdapter;
    public ArrayList<MovieReview> reviewList;


    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    public AutofitTextView mTitleTextView;
    public TextView mReleaseYearTextView;
    public TextView mDurationTextView;
    public TextView mRatingsTextView;
    public TextView mOverviewTextView;
    public ImageView mPosterImageTextView;
    public TextView mReviewerTextView;
    public TextView mReviewTextView;

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

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String movieID = intent.getStringExtra(Intent.EXTRA_TEXT);
            getMovieInfo(movieID);
            getReviews(movieID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        set the reviewAdapter
        reviewsAdapter = new ReviewsAdapter(
                getActivity(),
                reviewList
        );

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

//        to play the trailer
//        Button play = (Button)rootView.findViewById(R.id.playTrailer);
//        play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=ZiS7akYy4yA")));
//            }
//        });


        mTitleTextView = (AutofitTextView) rootView.findViewById(R.id.titleTextView);
        mReleaseYearTextView = (TextView) rootView.findViewById(R.id.yearOfReleaseTextView);
        mDurationTextView = (TextView) rootView.findViewById(R.id.durationTextView);
        mRatingsTextView = (TextView) rootView.findViewById(R.id.ratingsTextView);
        mPosterImageTextView = (ImageView) rootView.findViewById(R.id.posterImageView);
        mOverviewTextView = (TextView) rootView.findViewById(R.id.overviewTextView);


//        mOverviewTextView.setMovementMethod(new ScrollingMovementMethod());

//        we need some textViews for reviews
//        add layout
//        myLInearLayout = (LinearLayout) rootView.findViewById(R.id.containerForReviews);
//add layout params
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        myLInearLayout.setOrientation(LinearLayout.VERTICAL);

//add textview
//        for (int count = 0;count < 4;count++) {
//            textViewForReviews = new TextView(getContext());
//            textViewForReviews.setText("reviews will go here" + count);
//            textViewForReviews.setId(count + 5);
//            textViewForReviews.setLayoutParams(params);
//            myLInearLayout.addView(textViewForReviews);
//        }

        return rootView;
    }

}

