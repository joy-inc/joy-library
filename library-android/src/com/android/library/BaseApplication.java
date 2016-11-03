package com.android.library;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;

import com.android.library.httptask.RetroRequestQueue;
import com.android.library.httptask.RetroVolley;
import com.android.library.injection.component.AppComponent;
import com.android.library.injection.component.DaggerAppComponent;
import com.android.library.injection.module.AppModule;
import com.android.library.utils.LogMgr;
import com.android.library.utils.ToastUtil;
import com.android.volley.Cache;
import com.android.volley.RequestQueue.RequestFinishedListener;
import com.android.volley.VolleyLog;
import com.facebook.drawee.backends.pipeline.Fresco;

import butterknife.ButterKnife;

/**
 * Created by KEVIN.DAI on 15/7/8.
 */
public class BaseApplication extends Application {

    private static Context mContext;
    /**
     * Global request queue for Volley.
     */
    private static RetroRequestQueue mReqQueue;
    private AppComponent mComponent;

    @Override
    public void onCreate() {

        super.onCreate();
        initAppComponent();
        initContext();
        initVolley();
        initFresco();
        ButterKnife.setDebug(!BuildConfig.RELEASE);
    }

    private void initAppComponent() {

        mComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent component() {

        return mComponent;
    }

    private void initContext() {

        mContext = getApplicationContext();
    }

    public static Context getContext() {

        return mContext;
    }

    private static void releaseContext() {

        mContext = null;
    }

    public static Resources getAppResources() {

        return mContext.getResources();
    }

    public static String getAppString(@StringRes int resId) {

        return getAppResources().getString(resId);
    }

    public static String getAppString(@StringRes int resId, Object... formatArgs) {

        return getAppResources().getString(resId, formatArgs);
    }

    public static String[] getAppStringArray(@ArrayRes int resId) {

        return getAppResources().getStringArray(resId);
    }

    /**
     * the queue will be created if it is null.
     */
    private void initVolley() {

        if (mReqQueue == null) {

            mReqQueue = RetroVolley.newRequestQueue(mContext);
            mReqQueue.addRequestFinishedListener(mReqFinishLis);
        }
        VolleyLog.DEBUG = !BuildConfig.RELEASE;
    }

    private void initFresco() {

        Fresco.initialize(this);
    }

    /**
     * @return The Volley Request queue.
     */
    public static RetroRequestQueue getRequestQueue() {

        return mReqQueue;
    }

    public static Cache getVolleyCache() {

        return mReqQueue == null ? null : mReqQueue.getCache();
    }

    private static RequestFinishedListener mReqFinishLis = request -> {

        if (!BuildConfig.RELEASE)
            LogMgr.d("BaseApplication", "~~request finished. tag: " + request.getTag() + ", sequence number: " + request.getSequence());
    };

    private static void releaseVolley() {

        if (mReqQueue != null) {

            mReqQueue.removeRequestFinishedListener(mReqFinishLis);
            mReqQueue.cancelAll(request -> true);
//            mReqQueue.stop();
//            mReqQueue = null;
        }
    }

    private static void releaseFresco() {

        Fresco.shutDown();
    }

    protected static void release() {

//        releaseFresco();
        releaseVolley();
//        releaseContext();
        ToastUtil.release();
    }
}