package com.masthuggis.boki;

import android.app.Application;
import android.content.Context;

public class Boki extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        Boki.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Boki.context;
    }

}
