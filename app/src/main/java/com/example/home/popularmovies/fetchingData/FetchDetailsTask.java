package com.example.home.popularmovies.fetchingData;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.home.popularmovies.Fragments.DetailActivityFragment;
import com.example.home.popularmovies.Models.Movie;
import com.example.home.popularmovies.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by home on 11/15/2015.
 */
public class FetchDetailsTask extends AsyncTask<String, Void, Movie> {
    private DetailActivityFragment detailActivityFragment;
    private final String LOG_TAG = FetchDetailsTask.class.getSimpleName();

    public FetchDetailsTask(DetailActivityFragment detailActivityFragment) {
        this.detailActivityFragment = detailActivityFragment;
    }


    private Movie getMovieDataFromJson(String movieInfoJsonStr)
            throws JSONException, IOException {

        // These are the names of the JSON objects that need to be extracted.
        final String ORIGINAL_TITLE = "original_title";
        final String OVERVIEW = "overview";
        final String POSTER_PATH = "poster_path";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String RUNTIME = "runtime";

        JSONObject root = new JSONObject(movieInfoJsonStr);
        Movie movie = new Movie();

        movie.setRating(root.getString(VOTE_AVERAGE));
        movie.setSynopsis(root.getString(OVERVIEW));
        movie.setOriginalTitle(root.getString(ORIGINAL_TITLE));
        movie.setReleaseDate(root.getString(RELEASE_DATE));
        movie.setDuration(root.getString(RUNTIME));


        String poster_path = Uri.parse("http://image.tmdb.org/t/p/w185").buildUpon()
                .appendEncodedPath(root.getString(POSTER_PATH))
                .build().toString();

        movie.setPosterURL(poster_path);
        movie.setPosterImage(Picasso.with(detailActivityFragment.getContext())
                .load(movie.getPosterURL()).get());

        return movie;
    }


    @Override
    protected Movie doInBackground(String... params) {

        // If there's no info, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String moviesJsonStr = null;

        try {

            final String MOVIEDB_BASE_URL =
                    "http://api.themoviedb.org/3/movie/";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(API_KEY_PARAM, detailActivityFragment.getString(R.string.api_key))
                    .build();

            URL url = new URL(builtUri.toString());
//            Log.v(LOG_TAG, "BuiltURI Json string: " + url);

            // Create the request to themoviedb, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
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
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            moviesJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attempting
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
    @Override
    public void onPostExecute(Movie result) {
        if (result != null) {
            String releaseDate = result.getReleaseDate();
            int separatorIndex = releaseDate.indexOf('-');
            String releaseYear = releaseDate.substring(0, separatorIndex);

            detailActivityFragment.mTitleTextView.setText(result.getOriginalTitle());
            detailActivityFragment.mReleaseYearTextView.setText(releaseYear);
            detailActivityFragment.mDurationTextView.setText(result.getDuration() + "min");
            detailActivityFragment.mRatingsTextView.setText(result.getRating() + "/10.0");
            detailActivityFragment.mOverviewTextView.setText(result.getSynopsis());
            detailActivityFragment.mPosterImageTextView.setImageBitmap(result.getPosterImage());
            detailActivityFragment.mOverviewTextView.setText(result.getSynopsis());
        }
    }

}
