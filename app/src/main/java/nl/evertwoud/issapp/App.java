package nl.evertwoud.issapp;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Mapbox Access token initialization
        Mapbox.getInstance(getApplicationContext(), getString(R.string.mapbox_access_token));
    }
}