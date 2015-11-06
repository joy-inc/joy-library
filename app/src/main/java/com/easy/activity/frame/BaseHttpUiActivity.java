package com.easy.activity.frame;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.easy.R;
import com.easy.httptask.frame.ObjectRequest;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public abstract class BaseHttpUiActivity<T> extends BaseUiActivity {

    private ImageView mIvTip;
    private ProgressBar mProgressbar;
    private int mTipResId;
    private final int FAILED_RES_ID = R.mipmap.ic_net_error;
    private final int DISABLED_RES_ID = R.mipmap.ic_tip_null;

    protected RequestQueue mReqQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mReqQueue = Volley.newRequestQueue(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mReqQueue = null;
    }

    @Override
    protected void wrapContentView(ViewGroup rootView, View contentView) {

        super.wrapContentView(rootView, contentView);

        // tip view
        addTipView(rootView);

        // loading view
        addLoadingView(rootView);
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

        mProgressbar = new ProgressBar(this);
        mProgressbar.setTranslationY(STATUS_BAR_HEIGHT);// 纵向正偏移，使其纵向居中
        mProgressbar.setIndeterminate(true);
        hideView(mProgressbar);// 默认隐藏
        frame.addView(mProgressbar, new LayoutParams(DP_1_PX * 80, DP_1_PX * 80, Gravity.CENTER));
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

    private boolean isNeedCache() {

        return false;// TODO anyting
    }

    protected void executeRefreshAndCache() {

    }

    protected void executeRefresh() {

    }

    protected void showFailedTip() {

        mTipResId = FAILED_RES_ID;
        showImageView(mIvTip, mTipResId);
    }

    protected void hideTip() {

        hideImageView(mIvTip);
    }

    protected void showNoContentTip() {

        mTipResId = DISABLED_RES_ID;
        showImageView(mIvTip, mTipResId);
    }

    protected void showLoading() {

        showView(mProgressbar);
    }

    protected void hideLoading() {

        goneView(mProgressbar);
    }

    protected abstract void invalidateContent(T datas);

    /**
     * 子类可以继承此方法得到失败时的错误信息，用于Toast提示
     */
    protected void onHttpFailed(String msg) {
    }

    protected void addRequest2QueueNoCache(String url, Object tag, Class clazz) {

        showLoading();
        addRequest2Queue(url, false, tag, clazz);
    }

    protected void addRequest2QueueHasCache(String url, Object tag, Class clazz) {

        showLoading();
        addRequest2Queue(url, true, tag, clazz);
    }

    private void addRequest2Queue(String url, boolean shouldCache, Object tag, Class clazz) {

        ObjectRequest<T> req = new ObjectRequest(url, getObjLis(), getErrorLis(), clazz);
        req.setShouldCache(false);
        req.setTag(tag);
        mReqQueue.add(req);
    }

    protected void removeRequestFromQueue(Object tag) {

        if (mReqQueue != null)
            mReqQueue.cancelAll(tag);
    }

    private Listener<T> getObjLis() {

        return new Listener<T>() {

            @Override
            public void onResponse(T response) {

                if (isFinishing())
                    return;

                hideLoading();
                invalidateContent(response);
            }
        };
    }

    private ErrorListener getErrorLis() {

        return new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (isFinishing())
                    return;

                hideLoading();
                showFailedTip();
                onHttpFailed(error.getMessage());
            }
        };
    }
}
