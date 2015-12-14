package com.example.home.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by home on 12/7/2015.
 */
public class Trailer implements Parcelable {
    protected Trailer(Parcel in) {
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
