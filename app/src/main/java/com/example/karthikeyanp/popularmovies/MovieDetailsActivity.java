package com.example.karthikeyanp.popularmovies;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.karthikeyanp.popularmovies.fragments.ReviewFragment;
import com.example.karthikeyanp.popularmovies.fragments.SynopsisFragment;
import com.example.karthikeyanp.popularmovies.fragments.TrailerFragment;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by karthikeyanp on 2/21/2017.
 */

public class MovieDetailsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final String EXTRA_MOVIE = "movie";
    private static final String FRAGMENT_ID = "FRAGMENT_ID";
    public static final int FRAGMENT_SYNOPSIS = 1;
    public static final int FRAGMENT_TRAILER = 2;
    public static final int FRAGMENT_REVIEW = 3;
    private static final String TAG = "MovieDetailsActivity";
    private static int sCurrentFragment;
    @BindView(R.id.backdrop)
    ImageView backDropView;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.toolbar_details_activity)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.fab_fav)
    FloatingActionButton favourite;


    Movie movie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI(savedInstanceState);
        loadUI();
    }

    private void loadUI() {
        if (movie != null) {
            loadBackDrop();
            initFav();
        }
    }

    private void initFav() {
        if (MovieUtils.isFavorite(this, movie)) {
            favourite.setImageResource(R.drawable.ic_favorite);
        } else {
            favourite.setImageResource(R.drawable.ic_favorite_border);
        }
    }


    private void initUI(Bundle savedInstanceState) {
        setContentView(R.layout.movie_details_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        collapsingToolbar.setTitle(movie.title);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        favourite.setOnClickListener(this);
        loadFragment(savedInstanceState);
    }

    private void loadFragment(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment;
        if (savedInstanceState == null) {
            fragment = new SynopsisFragment();
            Bundle args = new Bundle();
            args.putParcelable(EXTRA_MOVIE, movie);
            fragment.setArguments(args);
            fragmentTransaction.add(R.id.fragment_container, fragment);
            sCurrentFragment = FRAGMENT_SYNOPSIS;
            fragmentTransaction.commit();
        } else if (savedInstanceState.containsKey(FRAGMENT_ID)) {
            int fragmentId = savedInstanceState.getInt(FRAGMENT_ID);
            replaceFragment(getFragment(fragmentId));
        }


    }

    private Fragment getFragment(int fragmentId) {
        switch (fragmentId) {
            case FRAGMENT_SYNOPSIS:
                sCurrentFragment = FRAGMENT_SYNOPSIS;
                return new SynopsisFragment();
            case FRAGMENT_REVIEW:
                sCurrentFragment = FRAGMENT_REVIEW;
                return new ReviewFragment();
            case FRAGMENT_TRAILER:
                sCurrentFragment = FRAGMENT_TRAILER;
                return new TrailerFragment();
            default:
                return new SynopsisFragment();
        }
    }

    private void loadBackDrop() {
        Picasso.with(getApplicationContext())
                .load(MovieUtils.IMAGE_URL + movie.backdropPath)
                .into(backDropView);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_synopsis:
                sCurrentFragment = FRAGMENT_SYNOPSIS;
                replaceFragment(new SynopsisFragment());
                break;
            case R.id.action_trailers:
                sCurrentFragment = FRAGMENT_TRAILER;
                replaceFragment(new TrailerFragment());
                break;
            case R.id.action_review:
                sCurrentFragment = FRAGMENT_REVIEW;
                replaceFragment(new ReviewFragment());
                break;
        }
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        // Create new fragment and transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_MOVIE, movie);
        fragment.setArguments(args);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outPersistentState.putInt(FRAGMENT_ID, sCurrentFragment);
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(FRAGMENT_ID, sCurrentFragment);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onClick(View v) {
        setFavorite();
    }

    private void setFavorite() {
        if (MovieUtils.isFavorite(this, movie)) {
            MovieUtils.removeFromFavorites(this, movie.id);
            favourite.setImageResource(R.drawable.ic_favorite_border);
        } else {
            MovieUtils.addToFavorites(this, movie);
            favourite.setImageResource(R.drawable.ic_favorite);
        }
    }
}
