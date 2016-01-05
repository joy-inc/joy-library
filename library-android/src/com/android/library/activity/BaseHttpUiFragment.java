package com.android.library.activity;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.android.library.BaseApplication;
import com.android.library.R;
import com.android.library.httptask.CacheMode;
import com.android.library.httptask.ObjectRequest;
import com.android.library.httptask.ObjectResponseListener;
import com.android.library.utils.LogMgr;
import com.android.library.widget.JLoadingView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public abstract class BaseHttpUiFragment<T> extends BaseUiFragment {

    private ImageView mIvTip;
    private JLoadingView mLoadingView;
    private int mTipResId;
    private final int FAILED_RES_ID = R.drawable.ic_net_error;
    private final int DISABLED_RES_ID = R.drawable.ic_tip_null;
    private ObjectRequest<T> mObjReq;

    @Override
    protected void wrapContentView(FrameLayout rootView, View contentView) {

        super.wrapContentView(rootView, contentView);
        // tip view
        addTipView(rootView);
        // loading view
        addLoadingView(rootView);
    }

    @Override
    public void onPause() {

        super.onPause();
        if (isFinishing()) {

            cancelRequest();
        }
    }

    private void addTipView(ViewGroup frame) {

        mIvTip = new ImageView(getActivity());
        mIvTip.setScaleType(ScaleType.CENTER_INSIDE);
        mIvTip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                onTipViewClick();
            }
        });
        hideImageView(mIvTip);
        frame.addView(mIvTip, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        makeCenterIfNecessary(mIvTip);
    }

    private void addLoadingView(ViewGroup frame) {

        mLoadingView = JLoadingView.get(getActivity());
        mLoadingView.hide();// 默认隐藏
        frame.addView(mLoadingView, JLoadingView.getLp());
        makeCenterIfNecessary(mLoadingView);
    }

    private void makeCenterIfNecessary(View v) {

        Activity act = getActivity();
        if (act instanceof BaseTabActivity)
            v.setTranslationY(-((BaseTabActivity) act).getToolbarLp().height / 2);
    }

    private void onTipViewClick() {

        if (mIvTip.getDrawable() == null || mTipResId == DISABLED_RES_ID)
            return;

        if (isNetworkDisable()) {

            showToast(R.string.toast_common_no_network);
        } else {

            onRetry();
        }
    }

    protected void onRetry() {

        execute(getReqCacheMode());
    }

    protected CacheMode getReqCacheMode() {

        if (mObjReq == null)
            return CacheMode.NONE;

        return mObjReq.getCacheMode();
    }

    protected boolean isReqHasCache() {

        if (mObjReq == null)
            return false;

        return mObjReq.hasCache();
    }

    boolean isRespIntermediate() {

        if (mObjReq == null)
            return false;

        return mObjReq.isRespIntermediate();
    }

    protected void cancelRequest() {

        if (mObjReq != null) {

            mObjReq.cancel();
            mObjReq = null;
        }
    }

    /**
     * fetch net-->response.
     */
    protected void executeRefreshOnly() {

        execute(CacheMode.NONE);
    }

    /**
     * fetch cache-->response.
     */
    protected void executeCacheOnly() {

        execute(CacheMode.CACHE_ONLY);
    }

    /**
     * cache expired: fetch net, update cache-->response.
     */
    protected void executeRefreshAndCache() {

        execute(CacheMode.REFRESH_AND_CACHE);
    }

    /**
     * cache update needed: fetch cache-->response, fetch net, update cache-->response.
     */
    protected void executeCacheAndRefresh() {

        execute(CacheMode.CACHE_AND_REFRESH);
    }

    final void execute(CacheMode cacheMode) {

        cancelRequest();
        mObjReq = getObjectRequest();
        mObjReq.setCacheMode(cacheMode);
        mObjReq.setResponseListener(getObjRespLis());
        addRequest(mObjReq, cacheMode != CacheMode.NONE);
    }

    private ObjectResponseListener<T> getObjRespLis() {

        return new ObjectResponseListener<T>() {

            @Override
            public void onPre() {

//                hideContentView();
                hideTipView();
                showLoading();
            }

            @Override
            public void onSuccess(Object tag, T t) {

                if (isFinishing())
                    return;

                if (invalidateContent(t)) {

//                    hideTipView();
                    showContentView();
                } else {

                    hideContentView();
                    if (!isRespIntermediate())
                        showNoContentTip();
                }
                if (!isRespIntermediate())
                    hideLoading();
            }

            @Override
            public void onError(Object tag, String msg) {

                if (isFinishing())
                    return;

                onHttpFailed(msg);
                onHttpFailed(tag, msg);

                hideLoading();
                hideContentView();
                showFailedTip();
            }
        };
    }

    protected void showContentView() {

        showView(getContentView());
    }

    protected void hideContentView() {

        hideView(getContentView());
    }

    protected void showFailedTip() {

        mTipResId = FAILED_RES_ID;
        showImageView(mIvTip, mTipResId);
    }

    protected void hideTipView() {

        hideImageView(mIvTip);
    }

    protected void showNoContentTip() {

        mTipResId = DISABLED_RES_ID;
        showImageView(mIvTip, mTipResId);
    }

    protected void addCustomView(View v) {

        hideContentView();
        hideTipView();
        hideLoading();
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        getRootView().addView(v, lp);
        makeCenterIfNecessary(v);
    }

    protected void removeCustomView(View v) {

        getRootView().removeView(v);
    }

    protected void showLoading() {

        mLoadingView.show();
    }

    protected void hideLoading() {

        mLoadingView.hide();
    }

    protected abstract boolean invalidateContent(T t);

    /**
     * 子类可以继承此方法得到失败时的错误信息，用于Toast提示
     */
    protected void onHttpFailed(Object tag, String msg) {
    }

    void onHttpFailed(String msg) {

        if (LogMgr.isDebug())
            showToast(getClass().getSimpleName() + ": " + msg);
    }

    protected RequestQueue getRequestQueue() {

        return BaseApplication.getRequestQueue();
    }

    protected abstract ObjectRequest<T> getObjectRequest();

    protected void addRequestNoCache(ObjectRequest<?> req, Object tag) {

        addRequest(req, tag, false);
    }

    protected void addRequestHasCache(ObjectRequest<?> req, Object tag) {

        addRequest(req, tag, true);
    }

    protected void addRequest(ObjectRequest<?> req, Object tag, boolean shouldCache) {

        req.setTag(tag);
        req.setShouldCache(shouldCache);
        getRequestQueue().add(req);
    }

    protected void addRequestNoCache(ObjectRequest<?> req) {

        addRequest(req, false);
    }

    protected void addRequestHasCache(ObjectRequest<?> req) {

        addRequest(req, true);
    }

    protected void addRequest(ObjectRequest<?> req, boolean shouldCache) {

        addRequest(req, req.getIdentifier(), shouldCache);
    }

    protected void removeRequest(Object tag) {

        getRequestQueue().cancelAll(tag);
    }

    protected void removeAllRequest() {

        getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {

            @Override
            public boolean apply(Request<?> request) {

                return true;
            }
        });
    }
}