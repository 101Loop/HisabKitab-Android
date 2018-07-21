package com.vitartha.hisabkitab.Adapters;


import android.app.Application;
import android.content.Context;

public class HisabKitab extends Application {
    private static HisabKitab volleysingleton;

    @Override
    public void onCreate() {
        super.onCreate();
        volleysingleton = this;
    }

    public static HisabKitab getInstance() {
        return volleysingleton;
    }

    public static Context getAppContext() {
        return volleysingleton.getApplicationContext();

    }
}