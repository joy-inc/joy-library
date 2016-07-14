package com.android.library.presenter.activity;

import com.android.library.BaseApplication;
import com.android.library.httptask.ObjectRequest;
import com.android.library.httptask.RequestMode;
import com.android.library.ui.activity.interfaces.BaseViewNet;
import com.android.library.presenter.PresenterImpl;
import com.android.library.presenter.RequestLauncher;

import rx.Observable;

import static com.android.library.httptask.RequestMode.CACHE_AND_REFRESH;
import static com.android.library.httptask.RequestMode.CACHE_ONLY;
import static com.android.library.httptask.RequestMode.REFRESH_AND_CACHE;
import static com.android.library.httptask.RequestMode.REFRESH_ONLY;

/**
 * Created by KEVIN.DAI on 16/1/18.
 */
public class RequestLauncherImpl<T, V extends BaseViewNet> extends PresenterImpl<V> implements RequestLauncher<T> {

    private ObjectRequest<T> mObjReq;
    private String[] mParams;

    @Override
    public Observable<T> launch(ObjectRequest<T> request, RequestMode mode) {

        if (request == null)
            throw new NullPointerException("You need override the getObjectRequest() method.");

        cancel();
        mObjReq = request;
        mObjReq.setRequestMode(mode);

        Observable<T> observable = addRequest(mObjReq, mode != REFRESH_ONLY);
//                .onErrorResumeNext(this::onErrorResume)
//                .share();

//        observable
//                .doOnSubscribe(this::doOnFirst)
//                .filter(this::filterNull)
//                .subscribe(
//                        this::onNext,
//                        this::onError);

        return observable;
    }

    public Observable<T> onErrorResume(Throwable e) {

        onError(e);
        return Observable.<T>empty();
    }

    public void doOnFirst() {

        getBaseView().hideContent();
        getBaseView().hideTipView();
        getBaseView().showLoading();
    }

    public boolean filterNull(T t) {

        if (t == null)
            onEmpty();
        return t != null;
    }

    public void onNext(T t) {

        if (isFinalResponse())
            getBaseView().hideLoading();
        getBaseView().showContent();
    }

    public void onError(Throwable e) {

        getBaseView().hideLoading();
        getBaseView().hideContent();
        getBaseView().showErrorTip();
    }

    public void onEmpty() {

        getBaseView().hideContent();
        if (isFinalResponse()) {
            getBaseView().hideLoading();
            getBaseView().showEmptyTip();
        }
    }

    @Override
    public final void cancel() {

        if (mObjReq != null) {

            mObjReq.cancel();
            mObjReq = null;
        }
    }

    /**
     * fetch net-->response.
     */
    public Observable<T> launchRefreshOnly(String... params) {

        setParams(params);
        return launch(getObjectRequest(params), REFRESH_ONLY);
    }

    /**
     * fetch cache-->response.
     */
    public Observable<T> launchCacheOnly(String... params) {

        setParams(params);
        return launch(getObjectRequest(params), CACHE_ONLY);
    }

    /**
     * cache expired: fetch net, update cache-->response.
     */
    public Observable<T> launchRefreshAndCache(String... params) {

        setParams(params);
        return launch(getObjectRequest(params), REFRESH_AND_CACHE);
    }

    /**
     * cache update needed: fetch cache-->response, fetch net, update cache-->response.
     */
    public Observable<T> launchCacheAndRefresh(String... params) {

        setParams(params);
        return launch(getObjectRequest(params), CACHE_AND_REFRESH);
    }

    protected ObjectRequest<T> getObjectRequest(String... params) {

        return null;
    }

    public final void setParams(String... params) {

        mParams = params;
    }

    public final String[] getParams() {

        return mParams;
    }

    public final RequestMode getRequestMode() {

        return mObjReq != null ? mObjReq.getRequestMode() : REFRESH_ONLY;
    }

    public final boolean isReqHasCache() {

        return mObjReq != null && mObjReq.hasCache();
    }

    public final boolean isFinalResponse() {

        return mObjReq != null && mObjReq.isFinalResponse();
    }

    public final Observable<T> addRequestNoCache(ObjectRequest<T> req, Object tag) {

        return addRequest(req, tag, false);
    }

    public final Observable<T> addRequestHasCache(ObjectRequest<T> req, Object tag) {

        return addRequest(req, tag, true);
    }

    public final Observable<T> addRequest(ObjectRequest<T> req, Object tag, boolean shouldCache) {

        req.setTag(tag);
        req.setShouldCache(shouldCache);
        return BaseApplication.getRequestQueue().addRequest(req);
    }

    public final Observable<T> addRequestNoCache(ObjectRequest<T> req) {

        return addRequest(req, false);
    }

    public final Observable<T> addRequestHasCache(ObjectRequest<T> req) {

        return addRequest(req, true);
    }

    public final Observable<T> addRequest(ObjectRequest<T> req, boolean shouldCache) {

        return addRequest(req, req.getIdentifier(), shouldCache);
    }

    public final void removeRequest(Object tag) {

        BaseApplication.getRequestQueue().cancelAll(tag);
    }

    public final void removeAllRequest() {

        BaseApplication.getRequestQueue().cancelAll(request -> true);
    }
}
