package com.think42lab.arangam;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.think42lab.arangam.util.FontsOverride;

/**
 * Created by think42lab on 18/12/16.
 */

public class ArangamApplication extends Application {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "normal", "Roboto-Regular.ttf");
    }
}
