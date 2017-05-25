package com.example.karthikeyanp.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karthikeyanp.popularmovies.MovieUtils;
import com.example.karthikeyanp.popularmovies.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by karthikeyanp on 5/12/2017.
 */

public class SynopsisFragment extends MoviesFragment {
    @BindView(R.id.details_release_date)
    TextView releaseDate;
    @BindView(R.id.details_vote_avg)
    TextView voteAverage;
    @BindView(R.id.details_synopsis)
    TextView synopsis;
    @BindView(R.id.details_poster_view)
    ImageView posterView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.synopsis_card_view, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMovieFromBundle();
        loadContent();
    }

    private void loadContent() {
        Picasso.with(getActivity())
                .load(MovieUtils.IMAGE_URL + movie.posterPath)
                .error(R.drawable.ic_error)
                .into(posterView);
        releaseDate.setText(movie.releaseDate);
        voteAverage.setText(String.valueOf(movie.voteAverage) + "/10");
        synopsis.setText(movie.overView);
    }

}
