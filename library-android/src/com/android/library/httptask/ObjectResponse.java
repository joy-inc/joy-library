package com.android.library.httptask;

import com.android.volley.VolleyError;

/**
 * Created by KEVIN.DAI on 15/11/25.
 */
@Deprecated
public abstract class ObjectResponse<T> implements ObjectResponseListener<T> {

    @Override
    public void onError(Object tag, VolleyError error) {

        onError(tag, ErrorHelper.getErrorType(error));
    }

    public void onError(Object tag, String msg) {

    }
}
