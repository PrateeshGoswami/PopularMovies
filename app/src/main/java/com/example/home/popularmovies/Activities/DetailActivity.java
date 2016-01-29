package com.example.home.popularmovies.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.home.popularmovies.Fragments.DetailActivityFragment;
import com.example.home.popularmovies.R;
import com.example.home.popularmovies.SaveMovieId;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements SaveMovieId{
    DetailActivityFragment detailActivityFragment;
    private String favMovieID;
    ArrayList<String> movieIdList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = this.getSharedPreferences("Data",0);
        int size = sharedPreferences.getInt("Status_Size",0);
        for (int i=0;i<size;i++){
            String string = sharedPreferences.getString("Status_" + i,null);
            movieIdList.add(string);
        }



        Intent intent =this.getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            favMovieID = intent.getStringExtra(Intent.EXTRA_TEXT);

        }

            setContentView(R.layout.activity_detail);
        setTitle("MovieDetail");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, new DetailActivityFragment(),"detFrag")
                    .commit();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    public String getFavMovieID(){
        return favMovieID;
    }

    @Override
    public void onFavBtnClicked(String movieID) {
       String str = getFavMovieID();

        Log.d("test","movie id rechd :" + str);
        if (movieIdList.contains(str)) {
            Toast.makeText(DetailActivity.this, "This Movie is already your favourite !!! =)",
                    Toast.LENGTH_LONG).show();
        } else {
            movieIdList.add(str);
        }
        SharedPreferences sharedPreferences =
                DetailActivity.this.getSharedPreferences("Data", 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Status_Size",movieIdList.size());

        for (int i = 0; i < movieIdList.size(); i++) {
            editor.remove("Status " + i);
            editor.putString("Status_" + i, movieIdList.get(i));


        }
        editor.commit();
    }
}