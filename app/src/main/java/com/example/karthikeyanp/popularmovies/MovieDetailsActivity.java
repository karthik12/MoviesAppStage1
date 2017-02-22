package com.example.karthikeyanp.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by karthikeyanp on 2/21/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_MOVIE = "movie";

    private TextView releaseDate, voteAverage, synopsis;

    ImageView posterView, backDropView;

    Movie movie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        loadUI();
    }

    private void loadUI() {
        if (movie != null) {
            loadBackDrop();
            loadContent();
        }
    }

    private void loadContent() {
        Picasso.with(getApplicationContext())
                .load(MovieUtils.IMAGE_URL + movie.posterPath)
                .error(R.drawable.ic_error)
                .into(posterView);
        releaseDate.setText(movie.releaseDate);
        voteAverage.setText(String.valueOf(movie.voteAverage) + "/10");
        synopsis.setText(movie.overView);
    }

    private void initUI() {
        setContentView(R.layout.movie_details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_details_activity);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        posterView = (ImageView) findViewById(R.id.details_poster_view);
        releaseDate = (TextView) findViewById(R.id.details_release_date);
        voteAverage = (TextView) findViewById(R.id.details_vote_avg);
        synopsis = (TextView) findViewById(R.id.details_synopsis);
        backDropView = (ImageView) findViewById(R.id.backdrop);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        collapsingToolbar.setTitle(movie.title);
    }

    private void loadBackDrop() {
        Picasso.with(getApplicationContext())
                .load(MovieUtils.IMAGE_URL + movie.backdropPath)
                .into(backDropView);
    }
}
