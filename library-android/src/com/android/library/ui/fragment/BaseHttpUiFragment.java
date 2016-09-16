package com.android.library.ui.fragment;

import android.app.Activity;
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
import com.android.library.ui.activity.BaseTabActivity;
import com.android.library.utils.DeviceUtil;
import com.android.library.widget.JLoadingView;
import com.android.volley.RequestQueue;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.android.library.httptask.RequestMode.CACHE_AND_REFRESH;
import static com.android.library.httptask.RequestMode.CACHE_ONLY;
import static com.android.library.httptask.RequestMode.REFRESH_AND_CACHE;
import static com.android.library.httptask.RequestMode.REFRESH_ONLY;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public abstract class BaseHttpUiFragment<T> extends BaseUiFragment {

    private ImageView mIvTip;
    private JLoadingView mLoadingView;
    private int mTipResId;
    private final int FAILED_RES_ID = R.drawable.ic_tip_error;
    private final int DISABLED_RES_ID = R.drawable.ic_tip_empty;
    private ObjectRequest<T> mObjReq;
    private boolean mContentHasDisplayed;

    @Override
    protected void wrapContentView(FrameLayout contentParent, View contentView) {

        super.wrapContentView(contentParent, contentView);
        // tip view
        addTipView(contentParent);
        // loading view
        addLoadingView(contentParent);
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
        mIvTip.setOnClickListener(v -> onTipViewClick());
        hideImageView(mIvTip);
        frame.addView(mIvTip, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
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

        if (DeviceUtil.isNetworkDisable()) {

            showToast(R.string.toast_common_no_network);
        } else {

            onRetry();
        }
    }

    protected void onRetry() {

        execute(getRequestMode());
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
    protected void executeRefreshOnly() {

        execute(REFRESH_ONLY);
    }

    /**
     * fetch cache-->response.
     */
    protected void executeCacheOnly() {

        execute(CACHE_ONLY);
    }

    /**
     * cache expired: fetch net, update cache-->response.
     */
    protected void executeRefreshAndCache() {

        execute(REFRESH_AND_CACHE);
    }

    /**
     * cache update needed: fetch cache-->response, fetch net, update cache-->response.
     */
    protected void executeCacheAndRefresh() {

        execute(CACHE_AND_REFRESH);
    }

    final void execute(RequestMode mode) {

        cancelRequest();
        mObjReq = getObjectRequest();
        mObjReq.setRequestMode(mode);
        mObjReq.setResponseListener(getObjRespLis());
        addRequest(mObjReq, mode != REFRESH_ONLY);
    }

    private ObjectResponseListener<T> getObjRespLis() {

        if (!mContentHasDisplayed)
            hideContent();
        hideTipView();
        showLoading();

        return new ObjectResponse<T>() {

            @Override
            public void onSuccess(Object tag, T t) {

                if (isFinishing())
                    return;

                if (isFinalResponse())
                    hideLoading();

                if (invalidateContent(t)) {

                    hideTipView();
                    showContent();
                    mContentHasDisplayed = true;
                } else {

                    if (isFinalResponse()) {
                        hideContent();
                        showEmptyTip();
                    }
                }
            }

            @Override
            public void onError(Object tag, String msg) {

                if (isFinishing())
                    return;

                onHttpFailed(msg);
                onHttpFailed(tag, msg);

                hideLoading();
                hideContent();
                showErrorTip();
            }
        };
    }

    protected void showContent() {

        showView(getContentView());
    }

    protected void hideContent() {

        hideView(getContentView());
    }

    protected void showErrorTip() {

        mTipResId = FAILED_RES_ID;
        showImageView(mIvTip, mTipResId);
    }

    protected void hideTipView() {

        hideImageView(mIvTip);
    }

    protected void showEmptyTip() {

        mTipResId = DISABLED_RES_ID;
        showImageView(mIvTip, mTipResId);
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