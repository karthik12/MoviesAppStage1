package com.example.karthikeyanp.popularmovies;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) MoviesApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
