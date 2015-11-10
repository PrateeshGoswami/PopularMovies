package com.example.home.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by home on 11/10/2015.
 */
@Database(version = MoviesDatabase.VERSION)
public final class MoviesDatabase {

    private MoviesDatabase(){
    }

    public static final int VERSION = 1;

    @Table(MoviesColumns.class) public static final String MOVIES = "movies";

}