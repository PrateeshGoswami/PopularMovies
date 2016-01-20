package com.example.home.popularmovies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.home.popularmovies.Fragments.DetailActivityFragment;
import com.example.home.popularmovies.R;
import com.example.home.popularmovies.SaveMovieId;

public class DetailActivity extends AppCompatActivity implements SaveMovieId{
    DetailActivityFragment detailActivityFragment;


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
        Intent intent = DetailActivity.this.getIntent();
        String id = intent.getExtras().getString("favmovieId");
       Bundle args = new Bundle();
        args.putString("favMovieId",id);
        DetailActivityFragment frag = new DetailActivityFragment();
        frag.setArguments(args);

    }
}