package com.example.karthikeyanp.popularmovies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import static com.example.karthikeyanp.popularmovies.MovieUtils.buildUrl;
import static com.example.karthikeyanp.popularmovies.MovieUtils.getApiKey;
import static com.example.karthikeyanp.popularmovies.MovieUtils.getMovieListFromJson;
import static com.example.karthikeyanp.popularmovies.MovieUtils.isOnline;
import static com.example.karthikeyanp.popularmovies.NetworkFragment.getInstance;

public class MainActivity extends AppCompatActivity implements DownloadCallback<Result>, NetworkFragment.ActivityCallback {

    private static final String TAG = "MoviesActivity";
    private static final String MOVIE_LIST_JSON = "movie_list_json";
    private static final String EXTRA_MOVIE = "movie";
    private static final String SORT_ORDER = "sort_order";
    private RecyclerView mRecyclerView;
    private LinearLayout networkView;
    private MoviesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Movie> movieList;
    NetworkFragment mNetworkFragment;
    private boolean mDownloading = false;
    private SortOrder sortOrder = SortOrder.TOP_RATED;
    private boolean activityRecreation = false;
    private boolean isClickEventAllowed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        if (getApiKey() == null) {
            Toast.makeText(this, R.string.key_not_found, Toast.LENGTH_SHORT).show();
            return;
        }
        mNetworkFragment = getInstance(getSupportFragmentManager());
        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_LIST_JSON)) {
            activityRecreation = true;
            updateAdapter(getMovieListFromJson(savedInstanceState.getString(MOVIE_LIST_JSON)));
            sortOrder = (SortOrder) savedInstanceState.getSerializable(SORT_ORDER);
        }

    }

    private BroadcastReceiver connectivityChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isOnline()) {
                onlineViewChanges();
            } else if (movieList == null || movieList.isEmpty()) {
                offlineViewChanges();
            }
        }
    };

    private void initUI() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.movies_grid_view);
        networkView = (LinearLayout) findViewById(R.id.network_view);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        MoviesAdapter.OnRecycleViewItemClickListener clickListener = new MoviesAdapter.OnRecycleViewItemClickListener() {

            @Override
            public void recyclerViewListClicked(int position) {
                if (isClickEventAllowed) {
                    Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                    intent.putExtra(EXTRA_MOVIE, movieList.get(position));
                    startActivity(intent);
                }
            }
        };
        mAdapter = new MoviesAdapter(getApplicationContext(), clickListener, movieList);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                startDownload(sortOrder);
                return true;
            case R.id.menu_highest_rated:
                sortOrder = SortOrder.TOP_RATED;
                startDownload(sortOrder);
                return true;
            case R.id.menu_most_popular:
                sortOrder = SortOrder.POPULAR;
                startDownload(sortOrder);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movieList != null && !movieList.isEmpty()) {
            outState.putString(MOVIE_LIST_JSON, MovieUtils.getJsonString(movieList));
            outState.putSerializable(SORT_ORDER, sortOrder);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (sortOrder.getValue().equalsIgnoreCase(SortOrder.POPULAR.getValue())) {
            menu.findItem(R.id.menu_most_popular).setVisible(false);
        } else {
            menu.findItem(R.id.menu_most_popular).setVisible(true);
        }
        if (sortOrder.getValue().equalsIgnoreCase(SortOrder.TOP_RATED.getValue())) {
            menu.findItem(R.id.menu_highest_rated).setVisible(false);
        } else {
            menu.findItem(R.id.menu_highest_rated).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void startDownload(SortOrder order) {
        Log.d(TAG, "startDownload() called");
        if (!isOnline()) {
            offlineViewChanges();
            return;
        }
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            Log.d(TAG, "startDownload: starting..");
            mNetworkFragment.startDownload(buildUrl(order.getValue()));
            mDownloading = true;
        }
    }

    private void offlineViewChanges() {
        networkView.setVisibility(View.VISIBLE);
        mRecyclerView.setAlpha(0.4f);
        mRecyclerView.setClickable(false);
        isClickEventAllowed = false;
        mRecyclerView.setEnabled(false);

    }

    private void onlineViewChanges() {
        networkView.setVisibility(View.GONE);
        mRecyclerView.setAlpha(1f);
        isClickEventAllowed = true;
        mRecyclerView.setClickable(true);
        mRecyclerView.setEnabled(true);
        if (!activityRecreation) {
            startDownload(sortOrder);
        }
    }

    @Override
    public void updateFromDownload(Result result) {
        if (result == null) {
            Toast.makeText(this, "No Result..", Toast.LENGTH_SHORT).show();
        } else if (result.mException != null) {
            Log.d(TAG, "Exception in download" + result.mException);
        } else if (result.mResultValue != null) {
            updateAdapter(result.mResultValue);
        }
    }

    private void updateAdapter(List<Movie> mResultValue) {
        movieList = mResultValue;
        mAdapter.updateMovies(movieList);
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch (progressCode) {
            case Progress.ERROR:
                break;
            case Progress.CONNECT_SUCCESS:
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                break;
        }
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }

    @Override
    public void onActivityCreated() {
        if (!activityRecreation) {
            startDownload(sortOrder);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(connectivityChangeReceiver);
        } catch (Exception e) {
            Log.i(TAG, "Exception occurred during un-registering network receiver");
        }
    }
}
