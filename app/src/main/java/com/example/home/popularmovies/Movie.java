package com.example.home.popularmovies;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by home on 10/25/2015.
 */
public class Movie implements Parcelable{

    private String originalTitle;
    private String synopsis;
    private String posterURL;
    private String rating;
    private String releaseDate;
    private String id;
    private String duration;

    private Bitmap posterImage;

    public Movie() {}
    public Movie(String id, String originalTitle, String posterURL, String synopsis,
                 String rating, String releaseDate){
        this.id = id;
        this.originalTitle = originalTitle;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.posterURL = posterURL;
    }
    protected Movie(Parcel parcel ){
        id = parcel.readString();
        originalTitle = parcel.readString();
        synopsis = parcel.readString();
        rating = parcel.readString();
        releaseDate = parcel.readString();
        posterURL = parcel.readString();
    }

    public String getDuration() { return duration; }

    public void setDuration(String duration) { this.duration = duration; }

    public Bitmap getPosterImage() { return posterImage; }

    public void setPosterImage(Bitmap posterImage) { this.posterImage = posterImage; }

    public String getId() {  return id; }

    public void setId(String id) { this.id = id;}

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPosterURL() { return posterURL; }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(originalTitle);
        dest.writeString(synopsis);
        dest.writeString(rating);
        dest.writeString(releaseDate);
        dest.writeString(posterURL);

    }
    static final Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

