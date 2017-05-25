package com.example.karthikeyanp.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.karthikeyanp.popularmovies.MovieUtils;
import com.example.karthikeyanp.popularmovies.OkHttpHelper;
import com.example.karthikeyanp.popularmovies.R;
import com.example.karthikeyanp.popularmovies.Reviews;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by karthikeyanp on 5/12/2017.
 */

public class ReviewFragment extends MoviesFragment {

    private static final String TAG = "ReviewFragment";
    private static final String REVIEWS = "reviews";
    @BindView(R.id.review_view)
    RecyclerView reviewView;
    List<Reviews> reviewsList = new ArrayList<>();
    private ReviewsAdapter mAdapter;
    @BindView(R.id.review_offline_view)
    LinearLayout offlineView;
    @BindView(R.id.review_empty_result)
    LinearLayout emptyView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.review_layout, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        reviewView.setLayoutManager(mLayoutManager);
        mAdapter = new ReviewsAdapter(reviewsList);
        reviewView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (MovieUtils.isOnline()) {
            onlineViewChanges();
        } else {
            offlineViewChanges();
        }

    }

    private void loadContent() {
        getMovieFromBundle();
        OkHttpClient client = OkHttpHelper.getClient();

        final Request request = new Request.Builder()
                .url(MovieUtils.buildUrl(movie.id, REVIEWS))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(TAG, "onFailure: " + request, e);
            }

            @Override
            public void onResponse(Response response) {
                if (!response.isSuccessful()) {
                    // TODO: 5/18/2017 Show nice false UI for bad responses
                    Log.e(TAG, "onResponse:Unexpected code " + response);
                }
                InputStream stream;
                try {
                    stream = response.body().byteStream();
                    if (stream != null) {
                        String resultString = IOUtils.toString(stream, "UTF-8");
                        reviewsList = MovieUtils.getReviewList(resultString);
                        updateUI();
                    } else {
                        Log.e(TAG, "onResponse: No response received.");
                    }
                } catch (IOException e) {
                    Log.e(TAG, "onResponse: ", e);
                }
            }
        });
    }

    private void updateUI() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (reviewsList != null && !reviewsList.isEmpty()) {
                        mAdapter.updateReviews(reviewsList);
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }

    @Override
    protected void offlineViewChanges() {
        reviewView.setVisibility(View.GONE);
        offlineView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onlineViewChanges() {
        offlineView.setVisibility(View.GONE);
        reviewView.setVisibility(View.VISIBLE);
        loadContent();
    }
}
