package com.joy.library;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by KEVIN.DAI on 15/7/8.
 */
public class BaseApplication extends Application {

    private static Context mContext = null;

    @Override
    public void onCreate() {

        super.onCreate();
        mContext = getApplicationContext();
        Fresco.initialize(this);
    }

    public static Context getContext() {

        return mContext;
    }
}
