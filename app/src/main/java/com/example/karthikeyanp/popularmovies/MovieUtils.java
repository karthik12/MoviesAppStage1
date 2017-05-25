package com.example.karthikeyanp.popularmovies;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.karthikeyanp.popularmovies.database.MovieContract;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by karthikeyanp on 2/17/2017.
 */

public class MovieUtils {

    private static final String TAG = "MovieUtils";

    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/w500/";

    public static String buildUrl(String value) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(value)
                .appendQueryParameter("api_key", getApiKey());
        return builder.build().toString();
    }

    public static List<Movie> getMovieList(String resultString) {
        if (TextUtils.isEmpty(resultString)) {
            return null;
        } else {
            Gson gson = new Gson();
            JsonElement jelem = gson.fromJson(resultString, JsonElement.class);
            JsonObject jsonObject = jelem.getAsJsonObject();
            JsonArray results = jsonObject.getAsJsonArray("results");
            Type listType = new TypeToken<List<Movie>>() {
            }.getType();
            return gson.fromJson(results, listType);
        }
    }

    public static List<Reviews> getReviewList(String resultString) {
        if (TextUtils.isEmpty(resultString)) {
            return null;
        } else {
            Gson gson = new Gson();
            JsonElement jelem = gson.fromJson(resultString, JsonElement.class);
            JsonObject jsonObject = jelem.getAsJsonObject();
            JsonArray results = jsonObject.getAsJsonArray("results");
            Type listType = new TypeToken<List<Reviews>>() {
            }.getType();
            return gson.fromJson(results, listType);
        }
    }

    public static List<Videos> getVideoList(String resultString) {
        if (TextUtils.isEmpty(resultString)) {
            return null;
        } else {
            Gson gson = new Gson();
            JsonElement jelem = gson.fromJson(resultString, JsonElement.class);
            JsonObject jsonObject = jelem.getAsJsonObject();
            JsonArray results = jsonObject.getAsJsonArray("results");
            Type listType = new TypeToken<List<Videos>>() {
            }.getType();
            return gson.fromJson(results, listType);
        }
    }

    /**
     * build url to get review or trailer info based on movieId
     *
     * @param movieId
     * @param type
     * @return
     */
    public static String buildUrl(int movieId, String type) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(Integer.toString(movieId))
                .appendPath(type)
                .appendQueryParameter("api_key", getApiKey());
        return builder.build().toString();
    }

    public static String getApiKey() {
        try {
            Context appContext = MoviesApplication.getAppContext();
            ApplicationInfo ai = appContext.getPackageManager().getApplicationInfo(appContext.getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString("key");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        return null;
    }

    public static String getJsonString(List<Movie> movies) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Movie>>() {
        }.getType();
        return gson.toJson(movies, listType);
    }

    public static List<Movie> getMovieListFromJson(String json) {
        Gson gson = new Gson();
        JsonElement jsonElem = gson.fromJson(json, JsonElement.class);
        JsonArray jsonObject = jsonElem.getAsJsonArray();
        Type listType = new TypeToken<List<Movie>>() {
        }.getType();
        return gson.fromJson(jsonObject, listType);
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) MoviesApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static boolean isFavorite(Context context, Movie movie) {
        Uri uri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, movie.id);
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = cr.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst())
                return true;
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }

    public static void addToFavorites(Context context, Movie movie) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.MOVIE_ID, movie.id);
        values.put(MovieContract.MovieEntry.MOVIE_BACKDROP_URI, movie.backdropPath);
        values.put(MovieContract.MovieEntry.MOVIE_TITLE, movie.title);
        values.put(MovieContract.MovieEntry.MOVIE_POSTER, movie.posterPath);
        values.put(MovieContract.MovieEntry.MOVIE_SYNOPSIS, movie.overView);
        values.put(MovieContract.MovieEntry.MOVIE_VOTE_AVERAGE, movie.voteCount);
        values.put(MovieContract.MovieEntry.MOVIE_RELEASE_DATE, movie.releaseDate);
        values.put(MovieContract.MovieEntry.MOVIE_ADULT, movie.adult);
        values.put(MovieContract.MovieEntry.MOVIE_ORIGINAL_TITLE, movie.originalTitle);
        values.put(MovieContract.MovieEntry.MOVIE_LANGUAGE, movie.originalLanguage);
        values.put(MovieContract.MovieEntry.MOVIE_POPULARITY, movie.popularity);
        values.put(MovieContract.MovieEntry.MOVIE_VOTE_AVERAGE, movie.voteAverage);
        resolver.insert(uri, values);
    }

    public static void removeFromFavorites(Context context, int movieID) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();

        resolver.delete(uri, MovieContract.MovieEntry.MOVIE_ID + " = ? ",
                new String[]{movieID + ""});
    }

    public static boolean hasFavourite(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = cr.query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
            if (cursor != null && cursor.moveToFirst())
                return true;
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }
}
