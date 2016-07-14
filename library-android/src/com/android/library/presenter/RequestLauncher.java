package com.android.library.presenter;

import com.android.library.httptask.ObjectRequest;
import com.android.library.httptask.RequestMode;

import rx.Observable;

/**
 * Created by KEVIN.DAI on 16/1/18.
 */
public interface RequestLauncher<T> {

    Observable<T> launch(ObjectRequest<T> request, RequestMode mode);

    void cancel();
}
