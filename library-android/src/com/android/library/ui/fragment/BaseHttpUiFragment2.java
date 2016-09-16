package com.android.library.ui.fragment;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.android.library.R;
import com.android.library.ui.activity.BaseTabActivity;
import com.android.library.ui.fragment.interfaces.BaseViewNet;
import com.android.library.utils.DeviceUtil;
import com.android.library.widget.JLoadingView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ImageView.ScaleType.CENTER_INSIDE;

/**
 * Created by KEVIN.DAI on 16/1/18.
 */
public abstract class BaseHttpUiFragment2 extends BaseUiFragment implements BaseViewNet {

    private ImageView mIvTip;
    private JLoadingView mLoadingView;
    private int mTipResId;
    private final int ERROR_RES_ID = R.drawable.ic_tip_error;
    private final int EMPTY_RES_ID = R.drawable.ic_tip_empty;

    @Override
    protected void wrapContentView(FrameLayout contentParent, View contentView) {

        super.wrapContentView(contentParent, contentView);
        // tip view
        addTipView(contentParent);
        // loading view
        addLoadingView(contentParent);
    }

    private void addTipView(ViewGroup frame) {

        mIvTip = new ImageView(getActivity());
        mIvTip.setScaleType(CENTER_INSIDE);
        mIvTip.setOnClickListener(v -> onTipViewClick());
        hideImageView(mIvTip);
        frame.addView(mIvTip, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        makeCenterIfNecessary(mIvTip);
    }

    private void addLoadingView(ViewGroup frame) {

        mLoadingView = JLoadingView.get(getActivity());
        mLoadingView.hide();
        frame.addView(mLoadingView, JLoadingView.getLp());
        makeCenterIfNecessary(mLoadingView);
    }

    private void makeCenterIfNecessary(View v) {

        Activity act = getActivity();
        if (act instanceof BaseTabActivity)
            v.setTranslationY(-((BaseTabActivity) act).getToolbarLp().height / 2);
    }

    private void onTipViewClick() {

        if (getTipType() == TipType.ERROR) {

            if (DeviceUtil.isNetworkDisable()) {

                showToast(R.string.toast_common_no_network);
            } else {

                doOnRetry();
            }
        }
    }

    protected abstract void doOnRetry();

    @Override
    public void showLoading() {

        mLoadingView.show();
    }

    @Override
    public void hideLoading() {

        mLoadingView.hide();
    }

    @Override
    public void showContent() {

        showView(getContentView());
    }

    @Override
    public void hideContent() {

        hideView(getContentView());
    }

    @Override
    public void showErrorTip() {

        mTipResId = ERROR_RES_ID;
        showImageView(mIvTip, mTipResId);
    }

    @Override
    public void showEmptyTip() {

        mTipResId = EMPTY_RES_ID;
        showImageView(mIvTip, mTipResId);
    }

    @Override
    public void hideTipView() {

        hideImageView(mIvTip);
    }

    @NonNull
    @Override
    public ImageView getTipView() {

        return mIvTip;
    }

    @Override
    public TipType getTipType() {

        if (mIvTip.getDrawable() != null) {

            if (mTipResId == EMPTY_RES_ID)
                return TipType.EMPTY;
            else if (mTipResId == ERROR_RES_ID)
                return TipType.ERROR;
        }
        return TipType.NULL;
    }
}
