package com.example.home.popularmovies.fetchingData;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.home.popularmovies.Fragments.DetailActivityFragment;
import com.example.home.popularmovies.Models.Trailer;
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
 * Created by home on 12/7/2015.
 */
public class FetchTrailersTask extends AsyncTask<String,Void,ArrayList<Trailer>>{
    private DetailActivityFragment detailActivityFragment;
    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

    public FetchTrailersTask(DetailActivityFragment detailActivityFragment) {
        this.detailActivityFragment = detailActivityFragment;
    }

//    getting the source for the trailers
    private ArrayList<Trailer> getTrailerFromJson(String movieTrailerJsonStr)
    throws JSONException
    {
        final String MDB_TRAILERS = "trailers";
        final String MDB_YOUTUBE = "youtube";
        final String MDB_NAME = "name";
        final String MDB_SOURCE = "source";

        JSONObject trailerJson = new JSONObject(movieTrailerJsonStr);
        JSONObject tr = trailerJson.getJSONObject(MDB_TRAILERS);
        JSONArray trailerAry = tr.getJSONArray(MDB_YOUTUBE);

        ArrayList<Trailer> trailerList = new ArrayList<>();

        for (int i = 0;i < trailerAry.length();i++){
            JSONObject jsonObject = trailerAry.getJSONObject(i);
            String trName = jsonObject.getString(MDB_NAME);
            String trSource = jsonObject.getString(MDB_SOURCE);
            if(jsonObject.getString(MDB_NAME) != null){
                Trailer trailer = new Trailer(trName,trSource);
                trailer.setTrailerName(trName);
                trailer.setTrailerSource(trSource);
//                Log.v(LOG_TAG, "Trailer name :" + trSource);
                trailerList.add(trailer);
            }
        }
        return trailerList;


    }
    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {
        // If there's no info, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
// These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // declaring a variable to contain the Json data as a String
        String movieTrailerJsonStr = null;
        try {
            final String MOVIE_ID = params[0];
            final String MOVIEDB_BASE_URL =
                    "http://api.themoviedb.org/3/movie";
            final String API_KEY_PARAM = "api_key";
            final String REVIEW_TRAILOR_PARAM = "append_to_response";

            Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                    .appendPath(MOVIE_ID)
                    .appendQueryParameter(API_KEY_PARAM, detailActivityFragment.getString(R.string.api_key))
                    .appendQueryParameter(REVIEW_TRAILOR_PARAM, detailActivityFragment.getString(R.string.review_trailor))
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
            movieTrailerJsonStr = buffer.toString();
//            Log.v(LOG_TAG, "Trailer Json string: " + movieTrailerJsonStr);

        }catch (IOException e) {
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
        }try {
            return getTrailerFromJson(movieTrailerJsonStr);
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> results) {
        detailActivityFragment.tLinearLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) detailActivityFragment.
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (results != null && !results.isEmpty()) {

            for (final Trailer trailers : results) {
                View view = inflater.inflate(R.layout.list_item_movie_trailer,null);
                TextView textView = (TextView) view.findViewById(R.id.list_item_trailer_name);
                textView.setText(trailers.getTrailerName());
//                Log.v(LOG_TAG,"trailer name :" + trailers.getTrailerSource());
//        to play the trailer
                ImageView play = (ImageView)view.findViewById(R.id.imgPlay);
                play.setOnClickListener(new View.OnClickListener() {
                    String source = trailers.getTrailerSource();
                    @Override
                    public void onClick(View v) {
                        detailActivityFragment.startActivity(new Intent(Intent.
                                ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+source)));
                    }
                });

                detailActivityFragment.tLinearLayout.addView(view);

            }

        }else {
            View view = inflater.inflate(R.layout.list_item_movie_trailer,null);
            TextView textView = (TextView)view.findViewById(R.id.list_item_trailer_name);
            textView.setText("Sorry no trailers for this movie  :( ");
            ImageView imageView = (ImageView)view.findViewById(R.id.imgPlay);
            imageView.setVisibility(view.GONE);
            detailActivityFragment.tLinearLayout.addView(view);
        }


    }
}
