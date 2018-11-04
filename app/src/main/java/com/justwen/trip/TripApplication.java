package com.justwen.trip;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Created by Justwen on 2018/10/21.
 */
public class TripApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static Context getContext() {
        return sContext;
    }
}
