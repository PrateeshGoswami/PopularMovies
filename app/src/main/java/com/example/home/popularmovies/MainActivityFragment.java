package com.example.home.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.home.popularmovies.fetchingData.FetchMoviesTask;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MoviesAdapter moviesAdapter;


    public MainActivityFragment() {
    }

    private void updateMovieList() {
        FetchMoviesTask fetchMovieInfo = new FetchMoviesTask(this);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortingOrder = sharedPref.getString(getString(R.string.pref_movie_sorting_key),
                getString(R.string.pref_movie_sorting_default));

        fetchMovieInfo.execute(sortingOrder);
    }

    //on start movie list will be updated
    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.

        moviesAdapter =
                new MoviesAdapter(
                        getActivity(),
                        new ArrayList<Movie>());

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) view.findViewById(R.id.movies_grid);
        gridView.setAdapter(moviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                String movieID = movie.getId();
                Intent otherIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movieID);

                startActivity(otherIntent);
            }
        });

        return view;
    }
    //on the background thread fetch the data from the moviedb.org

}

