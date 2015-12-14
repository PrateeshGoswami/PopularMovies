package com.example.home.popularmovies.fetchingData;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.home.popularmovies.DetailActivityFragment;
import com.example.home.popularmovies.Models.MovieReview;
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
 * Created by home on 12/5/2015.
 */
public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<MovieReview>> {

    private DetailActivityFragment detailActivityFragment;
    private final String LOG_TAG = FetchDetailsTask.class.getSimpleName();

    public FetchReviewsTask(DetailActivityFragment detailActivityFragment) {
        this.detailActivityFragment = detailActivityFragment;
    }

    private ArrayList<MovieReview> getReviewDataFromJSon(String movieReviewJsonStr)
            throws JSONException, IOException {
//        extract reviewer and review and number of reviews

        final String RESULTS = "results";
        final String REVIEWER = "author";
        final String REVIEW = "content";

        JSONObject reviewJson = new JSONObject(movieReviewJsonStr);
        JSONArray reviewArray = reviewJson.getJSONArray(RESULTS);

        ArrayList<MovieReview> reviewList = new ArrayList<>();

        for (int i = 0; i < reviewArray.length(); i++) {
            JSONObject review = reviewArray.getJSONObject(i);
            String strReviewer = review.getString(REVIEWER);
            String strReview = review.getString(REVIEW);

            // only add the movie with a review
            if (review.getString(REVIEW) != null) {
    reviewList.add(new MovieReview(strReviewer,strReview));
            }
        }

        return reviewList;
    }

private String getTotalNoofReviews(String movieReviewJsonStr)
throws JSONException,IOException{
    final String REVIEWS = "reviews";
    final String TOTAL_RESULTS = "total_results";

    JSONObject root = new JSONObject(movieReviewJsonStr);
    JSONObject count = root.getJSONObject(REVIEWS);
    String total = count.getString(TOTAL_RESULTS);
    return total;
}


    @Override
    protected ArrayList<MovieReview> doInBackground(String... params) {
        // If there's no info, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
// These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // declaring a variable to contain the Json data as a String
        String movieReviewJsonStr = null;

        try {
            final String MOVIE_ID = params[0];
            final String MOVIEDB_BASE_URL =
                    "http://api.themoviedb.org/3/movie";
            final String API_KEY_PARAM = "api_key";
            final String REVIEW_TRAILOR_PARAM ="append_to_response";

            Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                    .appendPath(MOVIE_ID)
                    .appendQueryParameter(API_KEY_PARAM, detailActivityFragment.getString(R.string.api_key))
                    .appendQueryParameter(REVIEW_TRAILOR_PARAM,detailActivityFragment.getString(R.string.review_trailor))
                    .build();
//            build the url
            URL url = new URL(builtUri.toString());
//            Log.v(LOG_TAG, "BuiltURI Json string: " + url);
            // Create the request to the movie database, and open the connection
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

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieReviewJsonStr = buffer.toString();
                     Log.v(LOG_TAG, "REVIEWS Json string: "+ movieReviewJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }try {
            return getReviewDataFromJSon(movieReviewJsonStr);
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            ex.printStackTrace();
        }

        return null;
    }
//
    @Override
    protected void onPostExecute(ArrayList<MovieReview> results) {
        if(results != null){
            for (MovieReview movieReview : results){

//this is where I'm having problem I'm unable to inflate a linearlayout
//                inside which I want to add textviews dynamically,
//                the number of textviews will be same as the number of reviews
//                after I'm done with reviews I will to do same for the trailers
//
            }

            }
        }
    }

