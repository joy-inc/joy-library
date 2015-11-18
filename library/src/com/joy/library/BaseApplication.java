package com.joy.library;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.RequestQueue.RequestFinishedListener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.joy.library.utils.LogMgr;

/**
 * Created by KEVIN.DAI on 15/7/8.
 */
public class BaseApplication extends Application {

    private static Context mContext = null;

    @Override
    public void onCreate() {

        super.onCreate();
        mContext = getApplicationContext();
        VolleyLog.DEBUG = true;
    }

    public static Context getContext() {

        return mContext;
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

//    private static void releaseContext() {
//
//        mContext = null;
//    }

    /**
     * Global request queue for Volley
     */
    private static RequestQueue mReqQueue;

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public static RequestQueue getRequestQueue() {

        // lazy initialize the request queue, the queue instance will be created when it is accessed for the first time
        if (mReqQueue == null) {

            mReqQueue = Volley.newRequestQueue(mContext);
            mReqQueue.addRequestFinishedListener(mReqFinishLis);
        }
        return mReqQueue;
    }

    private static RequestFinishedListener mReqFinishLis = new RequestFinishedListener() {

        @Override
        public void onRequestFinished(Request request) {

            if (LogMgr.isDebug())
                LogMgr.d("BaseApplication", "~~request finished tag: " + request.getTag() + ", sequence number: " + request.getSequence());
        }
    };

    private static void releaseVolley() {

        if (mReqQueue != null) {

            mReqQueue.removeRequestFinishedListener(mReqFinishLis);
            mReqQueue.cancelAll(new RequestFilter() {

                @Override
                public boolean apply(Request<?> request) {

                    return true;
                }
            });
            mReqQueue = null;
        }
    }

    protected static void release() {

        releaseVolley();
//        releaseContext();
    }
}