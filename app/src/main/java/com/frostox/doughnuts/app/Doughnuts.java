package com.frostox.doughnuts.app;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by roger on 11/1/2016.
 */
public class Doughnuts extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
