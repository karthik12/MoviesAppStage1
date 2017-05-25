package com.example.karthikeyanp.popularmovies;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Implementation of AsyncTask designed to fetch data from the network.
 */
public class DownloadTask extends AsyncTask<String, Void, Result> {

    private static final String TAG = "DownloadTask";
    private DownloadCallback<String> mCallback;

    DownloadTask(DownloadCallback<String> callback) {
        setCallback(callback);
    }

    private void setCallback(DownloadCallback<String> callback) {
        mCallback = callback;
    }

    /**
     * Cancel background network operation if we do not have network connectivity.
     */
    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute() called");
        if (mCallback != null) {
            NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected() ||
                    (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                // If no connectivity, cancel task and update Callback with null data.
                mCallback.updateFromDownload(null);
                cancel(true);
            }
        }
    }

    /**
     * Defines work to perform on the background thread.
     */
    @Override
    protected Result doInBackground(String... urls) {
        Log.d(TAG, "doInBackground() called with: urls = [" + urls + "]");
        Result result = null;
        InputStream stream = null;
        if (!isCancelled() && urls != null && urls.length > 0) {
            String urlString = urls[0];
            try {
                URL url = new URL(urlString);
                stream = downloadUrl(url);
                if (stream != null) {
                    String resultString = IOUtils.toString(stream, "UTF-8");
                    List<Movie> movies = MovieUtils.getMovieList(resultString);
                    result = new Result(movies);
                } else {
                    throw new IOException("No response received.");
                }

            } catch (Exception e) {
                result = new Result(e);
            } finally {
                IOUtils.closeQuietly(stream);
            }
        }
        return result;
    }

    /**
     * Updates the DownloadCallback with the result.
     */
    @Override
    protected void onPostExecute(Result result) {
        Log.d(TAG, "onPostExecute() called with: result = [" + result + "]");
        if (result != null && mCallback != null) {
            if (result.mException != null) {
                mCallback.updateFromDownload(result);
            } else {
                mCallback.updateFromDownload(result);
            }
            mCallback.finishDownloading();
        }
    }

    /**
     * Override to add special behavior for cancelled AsyncTask.
     */
    @Override
    protected void onCancelled(Result result) {
    }

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    private InputStream downloadUrl(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}
