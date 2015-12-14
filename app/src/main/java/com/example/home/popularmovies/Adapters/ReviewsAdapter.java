package com.example.home.popularmovies.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.home.popularmovies.Models.MovieReview;
import com.example.home.popularmovies.R;

import java.util.List;

/**
 * Created by home on 12/6/2015.
 */
public class ReviewsAdapter extends ArrayAdapter<MovieReview> {
    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context The current context. Used to inflate the layout file.
     * @param reviews A List of MovieReview objects to display in a list
     */
    public ReviewsAdapter(Activity context, List<MovieReview> reviews) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, reviews);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        MovieReview movieReview = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(
                    getContext()).inflate(R.layout.list_item_movie_review, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.list_item_reviewer_text);
        textView.setText(movieReview.strReviewer);

        TextView textView1 = (TextView) convertView.findViewById(R.id.list_item_review_text);
        textView1.setText(movieReview.strReview);
        return convertView;
    }
}
