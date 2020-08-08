package com.shashankbhat.musicplayer.application;

import android.app.Application;

import leakcanary.AppWatcher;

/**
 * Created by SHASHANK BHAT on 08-Aug-20.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        AppWatcher.INSTANCE.getObjectWatcher();
    }
}
