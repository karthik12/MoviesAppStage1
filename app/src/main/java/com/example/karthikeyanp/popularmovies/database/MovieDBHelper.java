package com.example.karthikeyanp.popularmovies.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.karthikeyanp.popularmovies.database.MovieContract.MovieEntry.MOVIE_ADULT;
import static com.example.karthikeyanp.popularmovies.database.MovieContract.MovieEntry.MOVIE_BACKDROP_URI;
import static com.example.karthikeyanp.popularmovies.database.MovieContract.MovieEntry.MOVIE_ID;
import static com.example.karthikeyanp.popularmovies.database.MovieContract.MovieEntry.MOVIE_LANGUAGE;
import static com.example.karthikeyanp.popularmovies.database.MovieContract.MovieEntry.MOVIE_ORIGINAL_TITLE;
import static com.example.karthikeyanp.popularmovies.database.MovieContract.MovieEntry.MOVIE_POPULARITY;
import static com.example.karthikeyanp.popularmovies.database.MovieContract.MovieEntry.MOVIE_POSTER;
import static com.example.karthikeyanp.popularmovies.database.MovieContract.MovieEntry.MOVIE_RELEASE_DATE;
import static com.example.karthikeyanp.popularmovies.database.MovieContract.MovieEntry.MOVIE_SYNOPSIS;
import static com.example.karthikeyanp.popularmovies.database.MovieContract.MovieEntry.MOVIE_TITLE;
import static com.example.karthikeyanp.popularmovies.database.MovieContract.MovieEntry.MOVIE_VOTE_AVERAGE;
import static com.example.karthikeyanp.popularmovies.database.MovieContract.MovieEntry.MOVIE_VOTE_COUNT;
import static com.example.karthikeyanp.popularmovies.database.MovieContract.MovieEntry.TABLE_NAME;
import static com.example.karthikeyanp.popularmovies.database.MovieContract.MovieEntry._ID;

public class MovieDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movieCraze.db";
    public static final int DATABASE_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MOVIE_ID + " TEXT UNIQUE NOT NULL," +
                MOVIE_BACKDROP_URI + " TEXT NOT NULL," +
                MOVIE_TITLE + " TEXT NOT NULL," +
                MOVIE_POSTER + " TEXT NOT NULL," +
                MOVIE_SYNOPSIS + " TEXT NOT NULL," +
                MOVIE_VOTE_AVERAGE + " TEXT NOT NULL," +
                MOVIE_RELEASE_DATE + " TEXT NOT NULL," +
                MOVIE_ADULT + " TEXT NOT NULL," +
                MOVIE_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MOVIE_LANGUAGE + " TEXT NOT NULL," +
                MOVIE_POPULARITY + " TEXT NOT NULL," +
                MOVIE_VOTE_COUNT + " INTEGER," +

                "UNIQUE (" + MOVIE_ID + ") ON CONFLICT IGNORE" +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
