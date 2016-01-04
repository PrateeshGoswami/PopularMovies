package com.example.home.popularmovies.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.home.popularmovies.R;
import com.example.home.popularmovies.fetchingData.FetchDetailsTask;
import com.example.home.popularmovies.fetchingData.FetchReviewsTask;
import com.example.home.popularmovies.fetchingData.FetchTrailersTask;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import me.grantland.widget.AutofitTextView;

//import com.example.home.popularmovies.Adapters.ReviewsAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null || !savedInstanceState.containsKey("savedTitle") || !savedInstanceState.containsKey("bitmap")) {
            updatedetailFragment();
        }else {
            mTitleTextView.setText(savedInstanceState.getCharSequence("savedTitle"));
            mReleaseYearTextView.setText(savedInstanceState.getCharSequence("savedRelease"));
            mDurationTextView.setText(savedInstanceState.getCharSequence("savedDur"));
            mRatingsTextView.setText(savedInstanceState.getCharSequence("savedRate"));
            mOverviewTextView.setText(savedInstanceState.getCharSequence("savedOver"));
            mPosterImageTextView.setImageBitmap(savedInstanceState.<Bitmap>getParcelable("bitmap"));


        }

    }

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

    private void getTrailers(String movieID) {
        FetchTrailersTask trailersTask = new FetchTrailersTask(this);
        trailersTask.execute(movieID);
    }

    private void updatedetailFragment() {
        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String movieID = intent.getStringExtra(Intent.EXTRA_TEXT);
            getMovieInfo(movieID);
            getReviews(movieID);
            getTrailers(movieID);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        updatedetailFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        mTitleTextView = (AutofitTextView) rootView.findViewById(R.id.titleTextView);
        mReleaseYearTextView = (TextView) rootView.findViewById(R.id.yearOfReleaseTextView);
        mDurationTextView = (TextView) rootView.findViewById(R.id.durationTextView);
        mRatingsTextView = (TextView) rootView.findViewById(R.id.ratingsTextView);
        mPosterImageTextView = (ImageView) rootView.findViewById(R.id.posterImageView);
        mOverviewTextView = (ExpandableTextView) rootView.findViewById(R.id.expand_text_view);
        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.containerForReviews);
        tLinearLayout = (LinearLayout) rootView.findViewById(R.id.containerForTrailers);


        return rootView;

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        CharSequence titleText = mTitleTextView.getText();
        CharSequence releaseText = mReleaseYearTextView.getText();
        CharSequence durationText = mDurationTextView.getText();
        CharSequence ratingText = mRatingsTextView.getText();
        mPosterImageTextView.buildDrawingCache();
        Bitmap bitmap = mPosterImageTextView.getDrawingCache();
        CharSequence overViewText = mOverviewTextView.getText();
        outState.putCharSequence("savedTitle", titleText);
        outState.putCharSequence("savedRelease", releaseText);
        outState.putCharSequence("savedDur", durationText);
        outState.putCharSequence("savedRate", ratingText);
        outState.putCharSequence("savedOver", overViewText);
        outState.putParcelable("bitmap", bitmap);


    }
}

