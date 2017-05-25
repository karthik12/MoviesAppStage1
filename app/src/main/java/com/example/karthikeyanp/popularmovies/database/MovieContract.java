package com.example.karthikeyanp.popularmovies.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by karthikeyanp on 5/23/2017.
 */

public class MovieContract {

    /* Add content provider constants to the Contract
     Clients need to know how to access the task data, and it's your job to provide
     these content URI's for the path to that data:
        1) Content authority,
        2) Base content URI,
        3) Path(s) to the tasks directory
        4) Content URI for data in the TaskEntry class
      */

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.karthikeyanp.popularmovies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_MOVIES = "movies";


    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String MOVIE_ID = "id";
        public static final String MOVIE_BACKDROP_URI = "movie_backdrop_path";
        public static final String MOVIE_ORIGINAL_TITLE = "original_title";
        public static final String MOVIE_LANGUAGE = "original_language";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_POSTER = "poster_path";
        public static final String MOVIE_ADULT = "adult";
        public static final String MOVIE_SYNOPSIS = "overview";
        public static final String MOVIE_VOTE_AVERAGE = "vote_average";
        public static final String MOVIE_POPULARITY = "popularity";
        public static final String MOVIE_VOTE_COUNT = "vote_count";
        public static final String MOVIE_RELEASE_DATE = "release_date";


    }
}
