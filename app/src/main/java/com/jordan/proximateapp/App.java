package com.jordan.proximateapp;

import android.app.Application;
import android.content.Context;

/**
 * Created by jordan on 07/02/2018.
 */

public class App extends Application {
    private static App m_singleton;

    public static App getInstance() {
        return m_singleton;
    }

    public static Context getContext() {
        return m_singleton.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
