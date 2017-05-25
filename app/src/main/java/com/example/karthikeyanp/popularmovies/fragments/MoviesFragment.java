package com.example.karthikeyanp.popularmovies.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.karthikeyanp.popularmovies.Movie;

import static com.example.karthikeyanp.popularmovies.MovieDetailsActivity.EXTRA_MOVIE;
import static com.example.karthikeyanp.popularmovies.MovieUtils.isOnline;

/**
 * Created by karthikeyanp on 5/18/2017.
 */

public abstract class MoviesFragment extends Fragment {
    private static final String TAG = "MoviesFragment";
    Movie movie;

    public void getMovieFromBundle() {
        movie = getArguments().getParcelable(EXTRA_MOVIE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private BroadcastReceiver connectivityChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isOnline()) {
                onlineViewChanges();
            } else {
                offlineViewChanges();
            }
        }
    };

    protected void offlineViewChanges() {
    }


    protected void onlineViewChanges() {
    }
    

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            getActivity().unregisterReceiver(connectivityChangeReceiver);
        } catch (Exception e) {
            Log.i(TAG, "Exception occurred during un-registering network receiver");
        }
    }
}
