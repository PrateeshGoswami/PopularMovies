package com.example.home.popularmovies.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.home.popularmovies.Fragments.DetailActivityFragment;
import com.example.home.popularmovies.Fragments.MainActivityFragment;
import com.example.home.popularmovies.R;
import com.example.home.popularmovies.SaveMovieId;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback ,SaveMovieId {
    private static final String DETAILFRAGMENT_TAG = "DTAG";
    ArrayList<String> movieIdList = new ArrayList<String>();



    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.movie_detail_container) != null){
            mTwoPane = true;
            if(savedInstanceState == null ){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container,new DetailActivityFragment(),DETAILFRAGMENT_TAG)
                        .commit();
            }

        }else {
            mTwoPane = false;
        }

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String movieID) {


        if(mTwoPane){
            Bundle args = new Bundle();
            args.putString("movieid",movieID);
            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container,fragment).commit();


        }else {
            Intent otherIntent = new Intent(this, DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movieID);
            startActivity(otherIntent);

        }

    }

    @Override
    public void onFavBtnClicked(String movieID) {

            Log.v("Movieid", movieID);
            if (movieIdList.contains(movieID)) {
                Toast.makeText(MainActivity.this, "This Movie is already your favourite !!! =)",
                        Toast.LENGTH_LONG).show();
            } else {
                movieIdList.add(movieID);
            }
            SharedPreferences sharedPreferences =
                    MainActivity.this.getSharedPreferences("movieIdData", 0);

            SharedPreferences.Editor editor = sharedPreferences.edit();

            for (int i = 0; i < movieIdList.size(); i++) {

                editor.putString("Status_" + i, movieIdList.get(i));


            }
            editor.commit();

    }
}
