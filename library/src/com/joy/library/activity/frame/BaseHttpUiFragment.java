package com.joy.library.activity.frame;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.joy.library.BaseApplication;
import com.joy.library.R;
import com.joy.library.httptask.frame.CacheEntry;
import com.joy.library.httptask.frame.ObjectRequest;
import com.joy.library.httptask.frame.ObjectResponseListener;
import com.joy.library.utils.LogMgr;
import com.joy.library.widget.JLoadingView;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public abstract class BaseHttpUiFragment<T> extends BaseUiFragment {

    private ImageView mIvTip;
    private JLoadingView mLoadingView;
    private int mTipResId;
    private final int FAILED_RES_ID = R.mipmap.ic_net_error;
    private final int DISABLED_RES_ID = R.mipmap.ic_tip_null;
    private boolean mIsNeedCache;

    @Override
    protected void wrapContentView(FrameLayout rootView, View contentView) {

        super.wrapContentView(rootView, contentView);
        // tip view
        addTipView(rootView);
        // loading view
        addLoadingView(rootView);
    }

    private void addTipView(ViewGroup frame) {

        mIvTip = new ImageView(getActivity());
//        mIvTip.setTranslationY(STATUS_BAR_HEIGHT);// 纵向正偏移，使其纵向居中
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

        mLoadingView = JLoadingView.get(getActivity());
        mLoadingView.hide();// 默认隐藏
        frame.addView(mLoadingView, JLoadingView.getLp());
    }

    protected void onTipViewClick() {

        if (mIvTip.getDrawable() == null || mTipResId == DISABLED_RES_ID)
            return;

        if (isNetworkDisable()) {

            showToast(R.string.toast_common_no_network);
            return;
        }

        if (isNeedCache()) {

            executeRefreshAndCache();
        } else {

            executeRefresh();
        }
    }

    protected boolean isNeedCache() {

        return mIsNeedCache;
    }

    protected void executeRefresh() {

        ObjectRequest<T> req = getObjectRequest();
        addRequest2QueueNoCache(req, req.getIdentifier());
    }

    protected void executeCache() {

        ObjectRequest<T> req = getObjectRequest();
        addRequest2QueueHasCache(req, req.getIdentifier());
    }

    protected void executeRefreshAndCache() {

        mIsNeedCache = true;

        ObjectRequest<T> req = getObjectRequest();
        addRequest2QueueHasCache(req, req.getIdentifier());
    }

    protected void executeCacheAndRefresh() {

        ObjectRequest<T> req = getObjectRequest();
        Cache.Entry entry = new CacheEntry();
        req.setCacheEntry(entry);

        if (LogMgr.isDebug())
            LogMgr.e("daisw", "~~" + entry.refreshNeeded() + " " + entry.isExpired());

        addRequest2QueueHasCache(req, req.getIdentifier());
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

        return ((BaseApplication) getActivity().getApplication()).getRequestQueue();
    }

    protected abstract ObjectRequest<T> getObjectRequest();

    protected void addRequest2QueueNoCache(ObjectRequest<T> req, Object tag) {

        addRequest2Queue(req, tag, false);
    }

    protected void addRequest2QueueHasCache(ObjectRequest<T> req, Object tag) {

        addRequest2Queue(req, tag, true);
    }

    protected void addRequest2Queue(ObjectRequest<T> req, Object tag, boolean shouldCache) {

        showLoading();
        hideTipView();

        req.setResponseListener(getObjRespLis());
        req.setShouldCache(shouldCache);
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    private ObjectResponseListener<T> getObjRespLis() {

        return new ObjectResponseListener<T>() {

            @Override
            public void onSuccess(Object tag, T t) {

                if (isFinishing())
                    return;

                hideLoading();
                boolean contentUsable = invalidateContent(t);
                if (!contentUsable)
                    showNoContentTip();
            }

            @Override
            public void onError(Object tag, VolleyError error) {

                if (isFinishing())
                    return;

                if (LogMgr.isDebug())
                    LogMgr.e("BaseHttpUiActivity", "~~onError tag: " + tag + " msg: " + error);

                hideLoading();
                showFailedTip();
                onHttpFailed(tag, error == null ? "" : error.getMessage());
            }
        };
    }

    protected void removeRequestFromQueue(Object tag) {

        getRequestQueue().cancelAll(tag);
    }

    protected void removeAllRequestFromQueue() {

        getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {

            @Override
            public boolean apply(Request<?> request) {

                return true;
            }
        });
    }
}