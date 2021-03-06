package com.example.home.popularmovies.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.home.popularmovies.Models.FavMovie;
import com.example.home.popularmovies.Models.Movie;
import com.example.home.popularmovies.Models.MovieReview;
import com.example.home.popularmovies.Models.Trailer;
import com.example.home.popularmovies.OnDetailDataLoadListener;
import com.example.home.popularmovies.OnReviewDataLoadListener;
import com.example.home.popularmovies.OnTrailerDataLoadListener;
import com.example.home.popularmovies.R;
import com.example.home.popularmovies.fetchingData.FetchDetailsTask;
import com.example.home.popularmovies.fetchingData.FetchReviewsTask;
import com.example.home.popularmovies.fetchingData.FetchTrailersTask;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitTextView;

//import com.example.home.popularmovies.Adapters.ReviewsAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements OnDetailDataLoadListener, OnReviewDataLoadListener, OnTrailerDataLoadListener {

    FavMovie favMovie = new FavMovie();
    String favMovieid;
    String favUrl;
    Movie movie = new Movie();
    ArrayList<MovieReview> reviewArrayList = new ArrayList<MovieReview>();
    ArrayList<Trailer> trailerArrayList = new ArrayList<Trailer>();

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
    @Bind(R.id.star)
    public CheckBox checkBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null ) {
            movie = savedInstanceState.getParcelable("movieDetails");
            reviewArrayList = savedInstanceState.getParcelableArrayList("reviewsList");
            trailerArrayList = savedInstanceState.getParcelableArrayList("trailerList");
            Log.d("testing","onsave" + movie.getOriginalTitle());



        }
    }

    public DetailActivityFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        String releaseDate = movie.getReleaseDate();
//        int separatorIndex = releaseDate.indexOf('-');
//        String releaseYear = releaseDate.substring(0, separatorIndex);
        mReleaseYearTextView.setText(releaseDate);

        mTitleTextView.setText(movie.getOriginalTitle());
        mPosterImageTextView.setImageBitmap(movie.getPosterImage());
        mDurationTextView.setText(movie.getDuration() + "min");
        mRatingsTextView.setText(movie.getRating() + "/10.0");
        mOverviewTextView.setText(movie.getSynopsis());
        LayoutInflater rinflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (reviewArrayList != null && !reviewArrayList.isEmpty()) {
            for (MovieReview movieReview : reviewArrayList) {
                View view = rinflater.inflate(R.layout.list_item_movie_review, null);
                TextView textView = (TextView) view.findViewById(R.id.list_item_reviewer_text);
                textView.setText(movieReview.getStrReviewer());
                ExpandableTextView textView1 = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
                textView1.setText(movieReview.getStrReview());
                mLinearLayout.addView(view);
            }
        } else {
            View view = rinflater.inflate(R.layout.list_item_movie_review, null);
            TextView textView = (TextView) view.findViewById(R.id.list_item_reviewer_text);
            textView.setText("Sorry no reviews for this movie  :( ");
            mLinearLayout.addView(view);
        }
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (trailerArrayList != null && !trailerArrayList.isEmpty()) {
            for (final Trailer trailers : trailerArrayList) {
                View view = inflater.inflate(R.layout.list_item_movie_trailer, null);
                TextView textView = (TextView) view.findViewById(R.id.list_item_trailer_name);
                textView.setText(trailers.getTrailerName());
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
            favMovieid = movieID;

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

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("favMoviedata", 0);

        Map<String, ?> keys = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            if (entry.getValue().toString()!=null) {
                Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                if (entry.getKey().equals(favMovieid)) {
                    checkBox.setChecked(true);
                }
            }
        }


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null ) {

            movie = savedInstanceState.getParcelable("movieDetails");
            reviewArrayList = savedInstanceState.getParcelableArrayList("reviewsList");
            trailerArrayList = savedInstanceState.getParcelableArrayList("trailerList");
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            movieID = arguments.getString("movieid");
            favMovieid = movieID;

        }
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        updateForTwoPane(movieID);


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SharedPreferences mPrefs = getActivity().getSharedPreferences("favMoviedata", 0);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    if (!mPrefs.contains(favMovieid)) {
                        prefsEditor.putString(favMovieid, favUrl);
                        prefsEditor.apply();
                    }


                } else {
                    SharedPreferences mPrefs = getActivity().getSharedPreferences("favMoviedata", 0);
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    prefsEditor.remove(favMovieid);
                    prefsEditor.apply();

                }

            }
        });
        return rootView;

    }

    @Override
    public void onDataReady(Movie result) {
        movie = result;
        String releaseDate = movie.getReleaseDate();
        int separatorIndex = releaseDate.indexOf('-');
        String releaseYear = releaseDate.substring(0, separatorIndex);
        mTitleTextView.setText(movie.getOriginalTitle());
        mReleaseYearTextView.setText(releaseYear);
        mDurationTextView.setText(movie.getDuration() + "min");
        mRatingsTextView.setText(movie.getRating() + "/10.0");
        mOverviewTextView.setText(movie.getSynopsis());
        mPosterImageTextView.setImageBitmap(movie.getPosterImage());
        favMovie.setPosterURL(movie.getPosterURL());
        favMovie.setId(movie.getId());
        favUrl = movie.getPosterURL();
    }

    @Override
    public void onDataReady(ArrayList<MovieReview> results) {
        reviewArrayList = results;
        mLinearLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (reviewArrayList != null && !reviewArrayList.isEmpty()) {
            for (MovieReview movieReview : reviewArrayList) {
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
        trailerArrayList = results;
        tLinearLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (trailerArrayList != null && !trailerArrayList.isEmpty()) {
            for (final Trailer trailers : trailerArrayList) {
                View view = inflater.inflate(R.layout.list_item_movie_trailer, null);
                TextView textView = (TextView) view.findViewById(R.id.list_item_trailer_name);
                textView.setText(trailers.getTrailerName());
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("movieDetails", movie);
        outState.putParcelableArrayList("reviewsList", (ArrayList<? extends Parcelable>) reviewArrayList);
        outState.putParcelableArrayList("trailerList", (ArrayList<? extends Parcelable>) trailerArrayList);
    }

}

