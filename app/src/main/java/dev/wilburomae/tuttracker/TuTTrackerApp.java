package dev.wilburomae.tuttracker;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class TuTTrackerApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
