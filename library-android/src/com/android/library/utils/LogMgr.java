package com.android.library.utils;

import android.util.Log;

import com.android.library.BuildConfig;

public class LogMgr {

    private static final String TAG = "LogMgr";

    public static void v(String log) {

        v(TAG, log);
    }

    public static void v(String tag, String log) {

        if (!BuildConfig.RELEASE)
            Log.v(tag, log);
    }

    public static void d(String log) {

        d(TAG, log);
    }

    public static void d(String tag, String log) {

        if (!BuildConfig.RELEASE)
            Log.d(tag, log);
    }

    public static void i(String log) {

        i(TAG, log);
    }

    public static void i(String tag, String log) {

        if (!BuildConfig.RELEASE)
            Log.i(tag, log);
    }

    public static void w(String log) {

        w(TAG, log);
    }

    public static void w(String tag, String log) {

        if (!BuildConfig.RELEASE)
            Log.w(tag, log);
    }

    public static void e(String log) {

        e(TAG, log);
    }

    public static void e(String tag, String log) {

        if (!BuildConfig.RELEASE)
            Log.e(tag, log);
    }
}
