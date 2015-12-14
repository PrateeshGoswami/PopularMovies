package com.example.home.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by home on 12/5/2015.
 */
public class MovieReview implements Parcelable {
    public String strReviewer;
   public String strReview;

    public MovieReview() {
    }

    public MovieReview(String strReviewer, String strReview) {
        strReviewer = this.strReviewer;
        strReview = this.strReview;
    }

    protected MovieReview(Parcel parcel) {
        strReviewer = parcel.readString();
        strReview = parcel.readString();
    }

    public String getStrReviewer() {
        return strReviewer;
    }

    public String getStrReview() {
        return strReview;

    }

    public void setStrReviewer(String strReviewer) {
        this.strReviewer = strReviewer;
    }

    public void setStrReview(String strReview) {
        this.strReview = strReview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(strReviewer);
        dest.writeString(strReview);

    }

    static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel parcel) {
            return new MovieReview(parcel);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }


    };

}
