package com.example.karthikeyanp.popularmovies.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.karthikeyanp.popularmovies.MovieUtils;
import com.example.karthikeyanp.popularmovies.OkHttpHelper;
import com.example.karthikeyanp.popularmovies.R;
import com.example.karthikeyanp.popularmovies.Videos;
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

public class TrailerFragment extends MoviesFragment {
    private static final String TAG = "Trailer";
    private static final String VIDEOS = "videos";
    @BindView(R.id.video_view)
    RecyclerView videoView;
    @BindView(R.id.video_offline_view)
    LinearLayout offlineView;
    @BindView(R.id.video_empty_result)
    LinearLayout emptyView;
    List<Videos> videosList = new ArrayList<>();
    private VideosAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_layout, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        videoView.setLayoutManager(mLayoutManager);
        mAdapter = new VideosAdapter(getActivity(), videosList, new MyOnVideoViewItemClickListener());
        videoView.setAdapter(mAdapter);
        return view;
    }

    private void openVideo(int position) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videosList.get(position).key));
        intent.putExtra("VIDEO_ID", videosList.get(position).key);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.app_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadContent();
    }

    private void loadContent() {
        getMovieFromBundle();
        OkHttpClient client = OkHttpHelper.getClient();

        final Request request = new Request.Builder()
                .url(MovieUtils.buildUrl(movie.id, VIDEOS))
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
                        videosList = MovieUtils.getVideoList(resultString);
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
                    if (videosList != null && !videosList.isEmpty()) {
                        mAdapter.updateVideos(videosList);
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }

    @Override
    protected void offlineViewChanges() {
        videoView.setVisibility(View.GONE);
        offlineView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onlineViewChanges() {
        offlineView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        loadContent();
    }

    private class MyOnVideoViewItemClickListener implements VideosAdapter.OnVideoViewItemClickListener {
        @Override
        public void videoClicked(int position) {
            openVideo(position);
        }
    }
}

