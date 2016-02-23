package com.example.home.popularmovies.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Prateesh Goswami
 * @version 1.0
 * @date 2/22/2016
 */
public class FavMovie  {

    @SerializedName("posterURL")
    @Expose
    private String posterURL;

    @SerializedName("id")
    @Expose
    private String id;

    public FavMovie() {
    }

    public FavMovie(String id, String posterURL) {
        this.id = id;

        this.posterURL = posterURL;
           }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public String getPosterURL() {
        return posterURL;
    }




}


