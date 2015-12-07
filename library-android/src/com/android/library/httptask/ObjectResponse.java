package com.android.library.httptask;

import com.android.library.utils.LogMgr;

/**
 * Created by KEVIN.DAI on 15/11/25.
 */
public abstract class ObjectResponse<T> implements ObjectResponseListener<T> {

    @Override
    public void onPre() {

    }

    @Override
    public abstract void onSuccess(Object tag, T t);

    @Override
    public void onError(Object tag, String msg) {

        if (LogMgr.isDebug())
            LogMgr.e("~~http request error:" + msg);
    }
}