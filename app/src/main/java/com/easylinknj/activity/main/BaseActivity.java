package com.easylinknj.activity.main;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.easylinknj.R;
import com.easylinknj.httptask.ObjectRequest;
import com.easylinknj.utils.DeviceUtil;
import com.easylinknj.utils.DimenCons;
import com.easylinknj.utils.ToastUtil;
import com.easylinknj.utils.ViewUtil;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public abstract class BaseActivity<T> extends Activity implements DimenCons {

    private ViewGroup mVgTitleBar;
    private TextView mTvTitle;
    private ImageView mIvTip;
    private ProgressBar mProgressbar;
    private SwipeRefreshLayout mSwipeRefreshWidget;
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
    public void setContentView(int layoutResId) {

        setContentView(inflateLayout(layoutResId));
    }

    @Override
    public void setContentView(View contentView) {

        super.setContentView(wrapContentView(contentView));

        initData();
        initTitleView();
        initContentView();
    }

    protected View wrapContentView(View contentView) {

        FrameLayout flFrame = new FrameLayout(this);

        // title bar
        addTitleView(flFrame);

        // content view
        addContentViewWithSwipeRefresh(contentView, flFrame);

        // tip view
        addTipView(flFrame);

        // loading view
        addLoadingView(flFrame);

        return flFrame;
    }

    private void addTitleView(ViewGroup frame) {

        mVgTitleBar = (ViewGroup) inflateLayout(R.layout.view_titlebar);
        mTvTitle = (TextView) mVgTitleBar.findViewById(R.id.tvTitle);
        for (int i = 0; i < mVgTitleBar.getChildCount(); i++)
            mVgTitleBar.getChildAt(i).setTranslationY(STATUS_BAR_HEIGHT / 2);// 纵向正偏移，使其纵向居中
        frame.addView(mVgTitleBar, new LayoutParams(LayoutParams.MATCH_PARENT, STATUS_BAR_HEIGHT + TITLE_BAR_HEIGHT));
    }

    private void addContentViewWithSwipeRefresh(View contentView, ViewGroup frame) {

        // swipe refresh widget
        mSwipeRefreshWidget = new SwipeRefreshLayout(this);
        mSwipeRefreshWidget.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeRefreshWidget.setOnRefreshListener(new OnRefreshListener() {// default listener

            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        stopSwipeRefresh();
                    }
                }, 5000);
            }
        });
        mSwipeRefreshWidget.addView(contentView);

        LayoutParams contentLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        contentLp.topMargin = STATUS_BAR_HEIGHT + TITLE_BAR_HEIGHT;
        frame.addView(mSwipeRefreshWidget, contentLp);
    }

    private void addTipView(ViewGroup frame) {

        mIvTip = new ImageView(this);
        hideImageView(mIvTip);
        mIvTip.setScaleType(ScaleType.CENTER_INSIDE);
        mIvTip.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                onFrameTipViewClick();
            }
        });
        frame.addView(mIvTip, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    private void addLoadingView(ViewGroup frame) {

        mProgressbar = new ProgressBar(this);
        mProgressbar.setIndeterminate(true);
        mProgressbar.setTranslationY(STATUS_BAR_HEIGHT);// 纵向正偏移，使其纵向居中
        hideView(mProgressbar);// 默认隐藏
        frame.addView(mProgressbar, new LayoutParams(DP_1_PX * 80, DP_1_PX * 80, Gravity.CENTER));
    }

    protected void onFrameTipViewClick() {

        if (mIvTip.getDrawable() == null || mTipResId == DISABLED_RES_ID)
            return;

        if (isNetworkDisable()) {

            showToast(R.string.toast_common_no_network);
            return;
        }

//        if (isFrameNeedCache()) {
//
//            executeFrameRefreshAndCache();
//        } else {
//
//            executeFrameRefresh();
//        }
    }

    protected void showFailed() {

        mTipResId = FAILED_RES_ID;
        showImageView(mIvTip, mTipResId);
    }

    protected void hideFailed() {

        hideImageView(mIvTip);
    }

    protected void showLoading() {

        showView(mProgressbar);
    }

    protected void hideLoading() {

        goneView(mProgressbar);
    }

    protected void setTitleBackgroundColor(int color) {

        mVgTitleBar.setBackgroundColor(color);
    }

    protected void setTitleBackgroundColorResId(int colorResId) {

        setTitleBackgroundColor(getResources().getColor(colorResId));
    }

    protected void setTitleText(String text) {

        mTvTitle.setText(text);
    }

    protected void setTitleText(int resId) {

        setTitleText(getString(resId));
    }

    protected void setColorSchemeResources(int... colorResIds) {

        mSwipeRefreshWidget.setColorSchemeResources(colorResIds);
    }

    protected void setOnRefreshListener(OnRefreshListener lisn) {

        mSwipeRefreshWidget.setOnRefreshListener(lisn);
    }

    protected boolean isSwipeRefreshing() {

        return mSwipeRefreshWidget.isRefreshing();
    }

    protected void startSwipeRefresh() {

        if (isSwipeRefreshing())
            return;

        mSwipeRefreshWidget.setRefreshing(true);
    }

    protected void stopSwipeRefresh() {

        if (!isSwipeRefreshing())
            return;

        mSwipeRefreshWidget.setRefreshing(false);
    }

    protected abstract void initData();

    protected abstract void initTitleView();

    protected abstract void initContentView();

    protected abstract void onObjResponse(T datas);

    protected abstract void onFailedResponse(String msg);

    protected void executeAPI(String url, Object tag, Class clazz) {

        showLoading();

        ObjectRequest<T> req = new ObjectRequest(url, getObjLis(), getErrorLis(), clazz);
        req.setTag(tag);
        mReqQueue.add(req);
    }

    private Listener<T> getObjLis() {

        return new Listener<T>() {

            @Override
            public void onResponse(T response) {

                if (isFinishing())
                    return;

                hideLoading();
                onObjResponse(response);
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
                showFailed();
                onFailedResponse(error.getMessage());
            }
        };
    }

    protected void showToast(String text) {

        ToastUtil.showToast(text);
    }

    protected void showToast(int resId) {

        showToast(getString(resId));
    }

    protected void showToast(int resId, Object... formatArgs) {

        showToast(getString(resId, formatArgs));
    }

    protected boolean isNetworkEnable() {

        return DeviceUtil.isNetworkEnable();
    }

    protected boolean isNetworkDisable() {

        return DeviceUtil.isNetworkDisable();
    }

    protected static boolean isLollipopOrUpper() {

        return DeviceUtil.isLollipopOrUpper();
    }

    protected static boolean isLollipopLower() {

        return DeviceUtil.isLollipopLower();
    }

    protected void showView(View v) {

        ViewUtil.showView(v);
    }

    protected void hideView(View v) {

        ViewUtil.hideView(v);
    }

    protected void goneView(View v) {

        ViewUtil.goneView(v);
    }

    protected void showImageView(ImageView v, int imageResId) {

        ViewUtil.showImageView(v, imageResId);
    }

    protected void showImageView(ImageView v, Drawable drawable) {

        ViewUtil.showImageView(v, drawable);
    }

    protected void hideImageView(ImageView v) {

        ViewUtil.hideImageView(v);
    }

    protected void goneImageView(ImageView v) {

        ViewUtil.goneImageView(v);
    }

    protected View inflateLayout(int resource) {

        return ViewUtil.inflateLayout(resource);
    }

    protected View inflateLayout(int resource, ViewGroup root) {

        return ViewUtil.inflateLayout(resource, root);
    }
}
