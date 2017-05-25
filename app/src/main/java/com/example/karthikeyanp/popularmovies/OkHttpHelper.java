package com.example.karthikeyanp.popularmovies;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by karthikeyanp on 5/18/2017.
 */

public class OkHttpHelper {

    private static OkHttpClient sClient;

    public static synchronized OkHttpClient getClient() {
        if (sClient == null) {
            sClient = new OkHttpClient();
        }
        return sClient;
    }


}
