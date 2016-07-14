package com.android.library.httptask;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Daisw on 16/6/27.
 */
@Singleton
public class RetroRequestQueue extends RequestQueue {

    public RetroRequestQueue(Cache cache, Network network) {

        super(cache, network);
    }

    public <T> Observable<T> addRequest(Request<T> request) {

        return ((ObjectRequest<T>) super.add(request)).observable();
    }
}
