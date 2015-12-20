package com.example.home.popularmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by home on 12/7/2015.
 */
public class Trailer implements Parcelable {
    public String trailerName;
    public String trailerSource;

    public Trailer(String trailerName, String trailerSource) {
        trailerName = this.trailerName;
        trailerSource = this.trailerSource;
    }

    protected Trailer(Parcel parcel) {
        trailerName = parcel.readString();
        trailerSource = parcel.readString();

    }

    public String getTrailerName() {
        return trailerName;
    }

    public String getTrailerSource() {
        return trailerSource;
    }

    public void setTrailerName(String trName) {
        this.trailerName = trName;

    }

    public void setTrailerSource(String trSource) {
        this.trailerSource = trSource;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailerName);
        dest.writeString(trailerSource);
    }

    static final Parcelable.Creator<Trailer> CREATOR =
            new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };


}
