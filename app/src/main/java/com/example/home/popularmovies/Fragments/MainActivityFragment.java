package com.example.home.popularmovies.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.home.popularmovies.Adapters.MoviesAdapter;
import com.example.home.popularmovies.Models.Movie;
import com.example.home.popularmovies.R;
import com.example.home.popularmovies.fetchingData.FetchMoviesTask;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    @Bind(R.id.movies_grid)
    GridView gridView;


    public MoviesAdapter moviesAdapter;
    private ArrayList<Movie> moviesList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// if the activity pauses no need to make the network call
        if (savedInstanceState == null || !savedInstanceState.containsKey("movie_key")) {
            moviesList = new ArrayList();
        } else {
            moviesList = savedInstanceState.getParcelableArrayList("movie_key");
        }
        setHasOptionsMenu(true);
    }


    public MainActivityFragment() {
    }

    private void updateMovieList() {
        FetchMoviesTask fetchMovieTask = new FetchMoviesTask(this);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortingOrder = sharedPref.getString(getString(R.string.pref_movie_sorting_key),
                getString(R.string.pref_movie_sorting_default));
        switch (sortingOrder) {
            case "popularity.desc":
                fetchMovieTask.execute(sortingOrder);
                break;
            case "vote_average.desc":
                fetchMovieTask.execute(sortingOrder);
                break;
            case "favourite":
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Data", 0);
                int size = sharedPreferences.getInt("Status_Size", 0);
                for (int i = 0; i < size; i++) {
                    String string = sharedPreferences.getString("Status_" + i, null);

                }


        }


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
                        moviesList);

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);


        gridView.setAdapter(moviesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                String movieID = movie.getId();
                ((Callback) getActivity()).onItemSelected(movieID);

            }
        });

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movie_key", (ArrayList<? extends Parcelable>) moviesList);
        super.onSaveInstanceState(outState);
    }

    public interface Callback {
        public void onItemSelected(String movieID);
    }

}

