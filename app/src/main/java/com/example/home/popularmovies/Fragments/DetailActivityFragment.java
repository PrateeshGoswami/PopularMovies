package com.example.home.popularmovies.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.home.popularmovies.R;
import com.example.home.popularmovies.SaveMovieId;
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
    private String movieID;
    private String favMovieid;
    ArrayList<String> favmovieIdList = new ArrayList<String>();
    Activity activity = getActivity();




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    public void getMovieInfo(String movieID) {
        FetchDetailsTask movieInfoTask = new FetchDetailsTask(this);
        movieInfoTask.execute(movieID);

    }

    public void getReviews(String movieID) {
        FetchReviewsTask reviewTask = new FetchReviewsTask(this);
        reviewTask.execute(movieID);
    }

    public void getTrailers(String movieID) {
        FetchTrailersTask trailersTask = new FetchTrailersTask(this);
        trailersTask.execute(movieID);
    }

    public void updatedetailFragment() {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String movieID = intent.getStringExtra(Intent.EXTRA_TEXT);
            getMovieInfo(movieID);
            getReviews(movieID);
            getTrailers(movieID);
        }

    }
    public void updateForTwoPane(String movieID){
        getMovieInfo(movieID);
        getReviews(movieID);
        getTrailers(movieID);
    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onStart() {
        super.onStart();
        updatedetailFragment();



    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments != null){
            movieID = arguments.getString("movieid");
//            favourite movie comes back from detail activity
            favMovieid = arguments.getString("favMovieId");
        }



        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        mTitleTextView = (AutofitTextView) rootView.findViewById(R.id.titleTextView);
        mReleaseYearTextView = (TextView) rootView.findViewById(R.id.yearOfReleaseTextView);
        mDurationTextView = (TextView) rootView.findViewById(R.id.durationTextView);
        mRatingsTextView = (TextView) rootView.findViewById(R.id.ratingsTextView);
        mPosterImageTextView = (ImageView) rootView.findViewById(R.id.posterImageView);
        mOverviewTextView = (ExpandableTextView) rootView.findViewById(R.id.expand_text_view);
        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.containerForReviews);
        tLinearLayout = (LinearLayout) rootView.findViewById(R.id.containerForTrailers);

        updateForTwoPane(movieID);


//  here when the mark favourite button is clicked
// the movie Id passes through the SaveMovieId interface
// to main activity

        Button button = (Button)rootView.findViewById(R.id.favButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((SaveMovieId) getActivity()).onFavBtnClicked(movieID);
            }
        });

//        it comes back on line number 125
//        then its added to an arrayList favmovieIdList
//

        if (favmovieIdList.contains(favMovieid)){
            Toast.makeText(activity, "this is already a favourite ", Toast.LENGTH_SHORT).show();


        }else {
            favmovieIdList.add(favMovieid);
        }
//            each item of favmovieIdlist is added to the shared pref


            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("favMovieData", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            for (int i = 0; i < favmovieIdList.size(); i++) {

                editor.putString("Status_" + i, favmovieIdList.get(i));



            editor.commit();
        }



        return rootView;



    }

    @Override
    public void onResume() {
        super.onResume();


    }

//    public interface SaveMovieId{
//        public void onFavBtnClicked(String movieID);
//    }

}

