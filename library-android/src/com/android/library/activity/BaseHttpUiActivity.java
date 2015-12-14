package com.android.library.activity;

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
import com.android.library.widget.JLoadingView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public abstract class BaseHttpUiActivity<T> extends BaseUiActivity {

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
    protected void onPause() {

        super.onPause();
        if (isFinishing()) {

            cancelRequest();
        }
    }

    private void addTipView(ViewGroup frame) {

        mIvTip = new ImageView(this);
        mIvTip.setTranslationY(STATUS_BAR_HEIGHT);// 纵向正偏移，使其纵向居中
        mIvTip.setScaleType(ScaleType.CENTER_INSIDE);
        mIvTip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                onTipViewClick();
            }
        });
        hideImageView(mIvTip);
        frame.addView(mIvTip, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void addLoadingView(ViewGroup frame) {

        mLoadingView = JLoadingView.get(this);
        mLoadingView.setTranslationY(STATUS_BAR_HEIGHT);// 纵向正偏移，使其纵向居中
        mLoadingView.hide();// 默认隐藏
        frame.addView(mLoadingView, JLoadingView.getLp());
    }

    protected void onTipViewClick() {

        if (mIvTip.getDrawable() == null || mTipResId == DISABLED_RES_ID)
            return;

        if (isNetworkDisable()) {

            showToast(R.string.toast_common_no_network);
        } else {

            onRetryCallback();
        }
    }

    protected void onRetryCallback() {

        switch (getReqCacheMode()) {

            case CACHE_ONLY:

                executeCacheOnly();
                break;
            case REFRESH_AND_CACHE:

                executeRefreshAndCache();
                break;
            case CACHE_AND_REFRESH:

                executeCacheAndRefresh();
                break;
            default:

                executeRefreshOnly();
                break;
        }
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

    protected boolean isRespIntermediate() {

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

        cancelRequest();
        mObjReq = getObjectRequest();
        mObjReq.setCacheMode(CacheMode.NONE);
        mObjReq.setResponseListener(getObjRespLis());
        addRequestNoCache(mObjReq);
    }

    /**
     * fetch cache-->response.
     */
    protected void executeCacheOnly() {

        cancelRequest();
        mObjReq = getObjectRequest();
        mObjReq.setCacheMode(CacheMode.CACHE_ONLY);
        mObjReq.setResponseListener(getObjRespLis());
        addRequestHasCache(mObjReq);
    }

    /**
     * cache expired: fetch net, update cache-->response.
     */
    protected void executeRefreshAndCache() {

        cancelRequest();
        mObjReq = getObjectRequest();
        mObjReq.setCacheMode(CacheMode.REFRESH_AND_CACHE);
        mObjReq.setResponseListener(getObjRespLis());
        addRequestHasCache(mObjReq);
    }

    /**
     * cache update needed: fetch cache-->response, fetch net, update cache-->response.
     */
    protected void executeCacheAndRefresh() {

        cancelRequest();
        mObjReq = getObjectRequest();
        mObjReq.setCacheMode(CacheMode.CACHE_AND_REFRESH);
        mObjReq.setResponseListener(getObjRespLis());
        addRequestHasCache(mObjReq);
    }

    private ObjectResponseListener<T> getObjRespLis() {

        return new ObjectResponseListener<T>() {

            @Override
            public void onPre() {

                hideContentView();
                hideTipView();
                showLoading();
            }

            @Override
            public void onSuccess(Object tag, T t) {

                if (isFinishing())
                    return;

                if (invalidateContent(t))
                    showContentView();
                else
                    showNoContentTip();
                if (!isRespIntermediate())
                    hideLoading();
            }

            @Override
            public void onError(Object tag, String msg) {

                if (isFinishing())
                    return;

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