package com.easylinknj.httptask;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public abstract class ObjectRequest<T> extends Request<T> {

    public ObjectRequest(int method, String url, Response.ErrorListener listener) {

        super(method, url, listener);
    }

    @Override
    protected void deliverResponse(T response) {

    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        return null;
    }
}
