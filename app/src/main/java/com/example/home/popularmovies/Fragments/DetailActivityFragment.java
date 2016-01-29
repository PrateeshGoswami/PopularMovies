package com.example.home.popularmovies.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.home.popularmovies.Models.Movie;
import com.example.home.popularmovies.Models.MovieReview;
import com.example.home.popularmovies.Models.Trailer;
import com.example.home.popularmovies.OnDetailDataLoadListener;
import com.example.home.popularmovies.OnReviewDataLoadListener;
import com.example.home.popularmovies.OnTrailerDataLoadListener;
import com.example.home.popularmovies.R;
import com.example.home.popularmovies.SaveMovieId;
import com.example.home.popularmovies.fetchingData.FetchDetailsTask;
import com.example.home.popularmovies.fetchingData.FetchReviewsTask;
import com.example.home.popularmovies.fetchingData.FetchTrailersTask;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitTextView;

//import com.example.home.popularmovies.Adapters.ReviewsAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements OnDetailDataLoadListener, OnReviewDataLoadListener, OnTrailerDataLoadListener {
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private String movieID;
    @Bind(R.id.titleTextView)
    public AutofitTextView mTitleTextView;
    @Bind(R.id.yearOfReleaseTextView)
    public TextView mReleaseYearTextView;
    @Bind(R.id.durationTextView)
    public TextView mDurationTextView;
    @Bind(R.id.ratingsTextView)
    public TextView mRatingsTextView;
    @Bind(R.id.expand_text_view)
    public ExpandableTextView mOverviewTextView;
    @Bind(R.id.posterImageView)
    public ImageView mPosterImageTextView;
    @Bind(R.id.containerForReviews)
    public LinearLayout mLinearLayout;
    @Bind(R.id.containerForTrailers)
    public LinearLayout tLinearLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public DetailActivityFragment() {
    }

    public void getMovieInfo(String movieID) {
        FetchDetailsTask movieInfoTask = new FetchDetailsTask(this);
        movieInfoTask.setOnloadfinishedlistener(this);
        movieInfoTask.execute(movieID);

    }


    public void getReviews(String movieID) {
        FetchReviewsTask reviewTask = new FetchReviewsTask(this);
        reviewTask.setOnDataLoadFinished(this);


        reviewTask.execute(movieID);
    }

    public void getTrailers(String movieID) {
        FetchTrailersTask trailersTask = new FetchTrailersTask(this);
        trailersTask.setOnTrailerLoad(this);
        trailersTask.execute(movieID);
    }

    public void updatedetailFragment() {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String movieID = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.d("test", "at update id is" + movieID);

            getMovieInfo(movieID);
            getReviews(movieID);
            getTrailers(movieID);
        }

    }

    public void updateForTwoPane(String movieID) {
        getMovieInfo(movieID);
        getReviews(movieID);
        getTrailers(movieID);
    }


    @Override
    public void onStart() {
        super.onStart();
        updatedetailFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            movieID = arguments.getString("movieid");
//            favourite movie comes back from detail activity
        }


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);


        updateForTwoPane(movieID);


//  here when the mark favourite button is clicked
// the movie Id passes through the SaveMovieId interface
// to main activity

        Button button = (Button) rootView.findViewById(R.id.favButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "movieid :" + movieID);

                ((SaveMovieId) getActivity()).onFavBtnClicked(movieID);

            }
        });

        return rootView;


    }


    @Override
    public void onDataReady(Movie result) {
        String releaseDate = result.getReleaseDate();
        int separatorIndex = releaseDate.indexOf('-');
        String releaseYear = releaseDate.substring(0, separatorIndex);

        mTitleTextView.setText(result.getOriginalTitle());
        mReleaseYearTextView.setText(releaseYear);
        mDurationTextView.setText(result.getDuration() + "min");
        mRatingsTextView.setText(result.getRating() + "/10.0");
        mOverviewTextView.setText(result.getSynopsis());
        mPosterImageTextView.setImageBitmap(result.getPosterImage());
        mOverviewTextView.setText(result.getSynopsis());


    }

    @Override
    public void onDataReady(ArrayList<MovieReview> results) {
        mLinearLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
        if (results != null && !results.isEmpty()) {

            for (MovieReview movieReview : results) {
                View view = inflater.inflate(R.layout.list_item_movie_review, null);
                TextView textView = (TextView) view.findViewById(R.id.list_item_reviewer_text);
                textView.setText(movieReview.getStrReviewer());
                ExpandableTextView textView1 = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
                textView1.setText(movieReview.getStrReview());
                mLinearLayout.addView(view);

            }

        } else {
            View view = inflater.inflate(R.layout.list_item_movie_review, null);
            TextView textView = (TextView) view.findViewById(R.id.list_item_reviewer_text);
            textView.setText("Sorry no reviews for this movie  :( ");
            mLinearLayout.addView(view);
        }


    }

    @Override
    public void onTrailerDataReady(ArrayList<Trailer> results) {
        tLinearLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (results != null && !results.isEmpty()) {

            for (final Trailer trailers : results) {
                View view = inflater.inflate(R.layout.list_item_movie_trailer, null);
                TextView textView = (TextView) view.findViewById(R.id.list_item_trailer_name);
                textView.setText(trailers.getTrailerName());
//                Log.v(LOG_TAG,"trailer name :" + trailers.getTrailerSource());
//        to play the trailer
                ImageView play = (ImageView) view.findViewById(R.id.imgPlay);
                play.setOnClickListener(new View.OnClickListener() {
                    String source = trailers.getTrailerSource();

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.
                                ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + source)));
                    }
                });

                tLinearLayout.addView(view);

            }

        } else {
            View view = inflater.inflate(R.layout.list_item_movie_trailer, null);
            TextView textView = (TextView) view.findViewById(R.id.list_item_trailer_name);
            textView.setText("Sorry no trailers for this movie  :( ");
            ImageView imageView = (ImageView) view.findViewById(R.id.imgPlay);
            imageView.setVisibility(view.GONE);
            tLinearLayout.addView(view);
        }
//


    }
}

