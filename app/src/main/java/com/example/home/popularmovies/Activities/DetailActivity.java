package com.example.home.popularmovies.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.home.popularmovies.Fragments.DetailActivityFragment;
import com.example.home.popularmovies.R;
import com.example.home.popularmovies.SaveMovieId;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements SaveMovieId{
    DetailActivityFragment detailActivityFragment;
    ArrayList<String> movieIdList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("MovieDetail");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, new DetailActivityFragment())
                    .commit();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onFavBtnClicked(String movieID) {

    }
}