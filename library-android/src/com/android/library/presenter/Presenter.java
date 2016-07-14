package com.android.library.presenter;

/**
 * Created by KEVIN.DAI on 16/1/18.
 */
public interface Presenter<V> {

    void attachView(V v);

    void detachView();
}
