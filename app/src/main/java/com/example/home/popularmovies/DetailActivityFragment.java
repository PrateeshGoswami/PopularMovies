package com.example.home.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import me.grantland.widget.AutofitTextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    public AutofitTextView mTitleTextView;
    public TextView mReleaseYearTextView;
    public TextView mDurationTextView;
    public TextView mRatingsTextView;
    public TextView mOverviewTextView;
    public ImageView mPosterImageTextView;

    public DetailActivityFragment() {
    }

    private void getMovieInfo(String movieID) {
        MovieInfoFetchTask movieInfoTask = new MovieInfoFetchTask();
        movieInfoTask.execute(movieID);
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = getActivity().getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String movieID = intent.getStringExtra(Intent.EXTRA_TEXT);
            getMovieInfo(movieID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mTitleTextView = (AutofitTextView) rootView.findViewById(R.id.titleTextView);
        mReleaseYearTextView = (TextView) rootView.findViewById(R.id.yearOfReleaseTextView);
        mDurationTextView = (TextView) rootView.findViewById(R.id.durationTextView);
        mRatingsTextView = (TextView) rootView.findViewById(R.id.ratingsTextView);
        mPosterImageTextView = (ImageView) rootView.findViewById(R.id.posterImageView);
        mOverviewTextView = (TextView) rootView.findViewById(R.id.overviewTextView);

        return rootView;
    }

    public class MovieInfoFetchTask extends AsyncTask<String, Void, Movie> {
        private final String LOG_TAG = MovieInfoFetchTask.class.getSimpleName();


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
            movie.setPosterImage(Picasso.with(getContext())
                    .load(movie.getPosterURL()).get());

            return movie;
        }

        @Override
        protected void onPostExecute(Movie result) {
            if (result != null) {
                String releaseDate = result.getReleaseDate();
                int separatorIndex = releaseDate.indexOf('-');
                String releaseYear = releaseDate.substring(0, separatorIndex);

                mTitleTextView.setText(result.getOriginalTitle());
                mReleaseYearTextView.setText(releaseYear);
                mDurationTextView.setText(result.getDuration() + "min");
                mRatingsTextView.setText(result.getRating() + "/10.0");
                mOverviewTextView.setText(result.getSynopsis());
                mPosterImageTextView.setImageBitmap(result.getPosterImage());
                mOverviewTextView.setText(result.getSynopsis());
            }
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
                        .appendQueryParameter(API_KEY_PARAM, getString(R.string.api_key))
                        .build();

                URL url = new URL(builtUri.toString());

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
    }

}

