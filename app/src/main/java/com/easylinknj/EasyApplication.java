package com.easylinknj;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by KEVIN.DAI on 15/7/8.
 */
public class EasyApplication extends Application{

    @Override
    public void onCreate() {

        super.onCreate();

        Fresco.initialize(this);
    }
}
