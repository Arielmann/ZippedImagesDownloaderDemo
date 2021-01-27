package com.example.zippedimagesdownloaderdemo.app;

import android.app.Application;
import android.content.Context;

public class ZippedImagesDownloadApp extends Application {

    public static Context APPLICATION_CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        APPLICATION_CONTEXT = getApplicationContext();
        setupHilt();
    }

    private void setupHilt() {
    }

}
