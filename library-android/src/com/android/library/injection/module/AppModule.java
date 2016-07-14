package com.android.library.injection.module;

import android.content.Context;

import com.android.library.BaseApplication;
import com.android.library.httptask.RetroRequestQueue;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link BaseApplication} to create.
 */
@Module
public class AppModule {

    private final BaseApplication mApplication;

    public AppModule(BaseApplication application) {

        mApplication = application;
    }

    /**
     * Expose the application to the graph.
     */
    @Provides
    @Singleton
    BaseApplication provideApplication() {

        return mApplication;
    }

    @Provides
    @Singleton
    RetroRequestQueue provideRequestQueue() {

        return BaseApplication.getRequestQueue();
    }
}
