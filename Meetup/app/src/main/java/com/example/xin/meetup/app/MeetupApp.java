package com.example.xin.meetup.app;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class MeetupApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
