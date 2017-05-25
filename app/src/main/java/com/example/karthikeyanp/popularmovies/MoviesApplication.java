package com.example.karthikeyanp.popularmovies;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by karthikeyanp on 2/17/2017.
 */

public class MoviesApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        sContext = getApplicationContext();
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    public static Context getAppContext() {
        return sContext;
    }
}
