package com.example.home.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by home on 11/10/2015.
 */
public interface MoviesColumns {


    @DataType(DataType.Type.INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID =
            "_id";


    // Movie id used to identify movie and fetch trailers and reviews
    @DataType(DataType.Type.TEXT) @NotNull
    String COLUMN_MOVIE_ID = "movie_id";
    // Column containing a movie title
    @DataType(DataType.Type.TEXT) @NotNull
    String COLUMN_TITLE = "title";
    // Column containing a plot for the movie
    @DataType(DataType.Type.TEXT) @NotNull
    String COLUMN_PLOT = "plot";
    @DataType(DataType.Type.TEXT) @NotNull
    // Column containing a user rating for the movie
            String COLUMN_USER_RATING = "user_rating";
    @DataType(DataType.Type.TEXT) @NotNull
    // Column containing release date for the movie
            String COLUMN_R_DATE = "release_date";
    // Column containing path for the movie poster
    @DataType(DataType.Type.TEXT) @NotNull
    String COLUMN_POSTER_PATH = "poster_path";
}
