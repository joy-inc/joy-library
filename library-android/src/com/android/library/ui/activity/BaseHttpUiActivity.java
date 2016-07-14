package com.android.library.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.android.library.BaseApplication;
import com.android.library.BuildConfig;
import com.android.library.R;
import com.android.library.httptask.ObjectRequest;
import com.android.library.httptask.ObjectResponse;
import com.android.library.httptask.ObjectResponseListener;
import com.android.library.httptask.RequestMode;
import com.android.library.utils.DeviceUtil;
import com.android.library.widget.JLoadingView;
import com.android.volley.RequestQueue;

import static com.android.library.httptask.RequestMode.CACHE_AND_REFRESH;
import static com.android.library.httptask.RequestMode.CACHE_ONLY;
import static com.android.library.httptask.RequestMode.REFRESH_AND_CACHE;
import static com.android.library.httptask.RequestMode.REFRESH_ONLY;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public abstract class BaseHttpUiActivity<T> extends BaseUiActivity {

    private ImageView mIvTip;
    private JLoadingView mLoadingView;
    private int mTipResId;
    private final int FAILED_RES_ID = R.drawable.ic_tip_error;
    private final int DISABLED_RES_ID = R.drawable.ic_tip_empty;
    private ObjectRequest<T> mObjReq;
    private boolean mContentHasDisplayed;

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
        mIvTip.setScaleType(ScaleType.CENTER_INSIDE);
        mIvTip.setOnClickListener(v -> onTipViewClick());
        hideImageView(mIvTip);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (!isNoTitle() && !isOverlay())
            lp.topMargin = isSystemBarTrans() ? STATUS_BAR_HEIGHT + getToolbarHeight() : getToolbarHeight();
        frame.addView(mIvTip, lp);
    }

    private void addLoadingView(ViewGroup frame) {

        mLoadingView = JLoadingView.get(this);
        mLoadingView.hide();// 默认隐藏
        LayoutParams lp = JLoadingView.getLp();
        if (!isNoTitle() && !isOverlay())
            lp.topMargin = isSystemBarTrans() ? (STATUS_BAR_HEIGHT + getToolbarHeight()) / 2 : getToolbarHeight() / 2;
        frame.addView(mLoadingView, lp);
    }

    private void onTipViewClick() {

        if (mIvTip.getDrawable() == null || mTipResId == DISABLED_RES_ID)
            return;

        if (DeviceUtil.isNetworkDisable()) {

            showToast(R.string.toast_common_no_network);
        } else {

            onRetry();
        }
    }

    protected void onRetry() {

        switch (getRequestMode()) {

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

    protected final RequestMode getRequestMode() {

        return mObjReq != null ? mObjReq.getRequestMode() : REFRESH_ONLY;
    }

    protected final boolean isReqHasCache() {

        return mObjReq != null && mObjReq.hasCache();
    }

    final boolean isFinalResponse() {

        return mObjReq != null && mObjReq.isFinalResponse();
    }

    protected final void cancelRequest() {

        if (mObjReq != null) {

            mObjReq.cancel();
            mObjReq = null;
        }
    }

    /**
     * fetch net-->response.
     */
    protected final void executeRefreshOnly() {

        cancelRequest();
        mObjReq = getObjectRequest();
        mObjReq.setRequestMode(REFRESH_ONLY);
        mObjReq.setResponseListener(getObjRespLis());
        addRequestNoCache(mObjReq);
    }

    /**
     * fetch cache-->response.
     */
    protected final void executeCacheOnly() {

        cancelRequest();
        mObjReq = getObjectRequest();
        mObjReq.setRequestMode(CACHE_ONLY);
        mObjReq.setResponseListener(getObjRespLis());
        addRequestHasCache(mObjReq);
    }

    /**
     * cache expired: fetch net, update cache-->response.
     */
    protected final void executeRefreshAndCache() {

        cancelRequest();
        mObjReq = getObjectRequest();
        mObjReq.setRequestMode(REFRESH_AND_CACHE);
        mObjReq.setResponseListener(getObjRespLis());
        addRequestHasCache(mObjReq);
    }

    /**
     * cache update needed: fetch cache-->response, fetch net, update cache-->response.
     */
    protected final void executeCacheAndRefresh() {

        cancelRequest();
        mObjReq = getObjectRequest();
        mObjReq.setRequestMode(CACHE_AND_REFRESH);
        mObjReq.setResponseListener(getObjRespLis());
        addRequestHasCache(mObjReq);
    }

    private ObjectResponseListener<T> getObjRespLis() {

        if (!mContentHasDisplayed)
            hideContentView();
        hideTipView();
        showLoading();

        return new ObjectResponse<T>() {

            @Override
            public void onSuccess(Object tag, T t) {

                if (isFinishing())
                    return;

                if (invalidateContent(t)) {

                    showContentView();
                    mContentHasDisplayed = true;
                } else
                    showNoContentTip();
                if (isFinalResponse())
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

        if (!BuildConfig.RELEASE)
            showToast(getClass().getSimpleName() + ": " + msg);
    }

    protected final RequestQueue getRequestQueue() {

        return BaseApplication.getRequestQueue();
    }

    protected abstract ObjectRequest<T> getObjectRequest();

    protected final void addRequestNoCache(ObjectRequest<?> req, Object tag) {

        addRequest(req, tag, false);
    }

    protected final void addRequestHasCache(ObjectRequest<?> req, Object tag) {

        addRequest(req, tag, true);
    }

    protected final void addRequest(ObjectRequest<?> req, Object tag, boolean shouldCache) {

        req.setTag(tag);
        req.setShouldCache(shouldCache);
        getRequestQueue().add(req);
    }

    protected final void addRequestNoCache(ObjectRequest<?> req) {

        addRequest(req, false);
    }

    protected final void addRequestHasCache(ObjectRequest<?> req) {

        addRequest(req, true);
    }

    protected final void addRequest(ObjectRequest<?> req, boolean shouldCache) {

        addRequest(req, req.getIdentifier(), shouldCache);
    }

    protected final void removeRequest(Object tag) {

        getRequestQueue().cancelAll(tag);
    }

    protected final void removeAllRequest() {

        getRequestQueue().cancelAll(request -> true);
    }
}