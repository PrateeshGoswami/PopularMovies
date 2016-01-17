package com.example.home.popularmovies.fetchingData;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.home.popularmovies.Fragments.MainActivityFragment;
import com.example.home.popularmovies.Models.Movie;
import com.example.home.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by home on 11/15/2015.
 */
public class FetchMoviesTask extends AsyncTask<String, Integer, ArrayList<Movie>> {
//        private int contentLength = -1;

//        @Override
//        protected void onPreExecute() {
//            downloadMoviesProgress.setVisibility(View.VISIBLE);
//        }

    private MainActivityFragment mainActivityFragment;
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    public FetchMoviesTask(MainActivityFragment mainActivityFragment) {
        this.mainActivityFragment = mainActivityFragment;
    }


    private ArrayList<Movie> getMovieDataFromJson(String moviesJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String RESULTS = "results";
        final String POSTER_PATH = "poster_path";
        final String ID = "id";
        final String ORIGINAL_TITLE = "original_title";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String VOTE_AVERAGE = "vote_average";

        JSONObject root = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = root.getJSONArray(RESULTS);

        ArrayList<Movie> moviesList = new ArrayList<>();

        for (int i = 0; i < moviesArray.length(); i++) {
            // Get the JSON object
            JSONObject movie = moviesArray.getJSONObject(i);

            String poster_path = Uri.parse("http://image.tmdb.org/t/p/w185").buildUpon()
                    .appendEncodedPath(movie.getString(POSTER_PATH))
                    .build().toString();

            String id = movie.getString(ID);
            String original_title = movie.getString(ORIGINAL_TITLE);
            String overview = movie.getString(OVERVIEW);
            String release_date = movie.getString(RELEASE_DATE);
            String vote_average = movie.getString(VOTE_AVERAGE);

            // Only add the movies that have a valid poster to display
            if (movie.getString(POSTER_PATH) != "null") {
                moviesList.add(new Movie(id, original_title, poster_path, overview,
                        vote_average, release_date));
            }
        }

        return moviesList;
    }


    //need to override doInBackground function

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        // If there's no info, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // declaring a variable to contain the Json data as a String
        String moviesJsonStr = null;

        try {
            final String MOVIEDB_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, params[0])
                    .appendQueryParameter(API_KEY_PARAM, mainActivityFragment.getString(R.string.api_key))
                    .build();

            //build the URL
            URL url = new URL(builtUri.toString());
//            Log.v(LOG_TAG, "BuiltURI Json string: " + url);


            // Create the request to the movie database, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            //            get the length of the content for progress bar
//                            contentLength = urlConnection.getContentLength();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            moviesJsonStr = buffer.toString();
//                  Log.v(LOG_TAG, "BuiltURI Json string: "+ moviesJsonStr);
            //publish progress
//            publishProgress(moviesJsonStr.length());


        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMovieDataFromJson(moviesJsonStr);
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            ex.printStackTrace();
        }
        return null;
    }

//            @Override
//            protected void onProgressUpdate(Integer... values) {
//
//        calculatedProgress=(int)(((double)values[0]/contentLength)*100);
//                downloadMoviesProgress.setProgress(calculatedProgress);
//            }
    @Override
    protected void onPostExecute(ArrayList<Movie> results) {
        if (results != null) {
            mainActivityFragment.moviesAdapter.clear();
            for (Movie movie : results) {
                mainActivityFragment.moviesAdapter.add(movie);
            }
        }
        //           bye bye progress bar thank you!
//                    downloadMoviesProgress.setVisibility(View.GONE);
    }
}
