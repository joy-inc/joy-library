package com.android.library.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.android.library.BaseApplication;
import com.android.library.utils.DeviceUtil;
import com.android.library.utils.DimenCons;
import com.android.library.utils.ToastUtil;
import com.android.library.utils.ViewUtil;

/**
 * 基本的UI框架
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseUiFragment extends Fragment implements DimenCons {

    private CharSequence mLableText;
    private FrameLayout mFlRoot;
    private View mContentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFlRoot = new FrameLayout(getActivity());
        return mFlRoot;
    }

    protected void setContentView(@LayoutRes int layoutResID) {

        setContentView(inflateLayout(layoutResID));
    }

    protected void setContentView(View contentView) {

        mContentView = contentView;
        wrapContentView(mFlRoot, mContentView);
        initData();
        initContentView();
    }

    protected void wrapContentView(FrameLayout rootView, View contentView) {

        // add transition animation
//        LayoutTransition lt = new LayoutTransition();
//        lt.setDuration(100);
//        mFlRoot.setLayoutTransition(lt);

        // content view
        rootView.addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    protected void initData() {
    }

    protected void initContentView() {
    }

    protected FrameLayout getRootView() {

        return mFlRoot;
    }

    protected View getContentView() {

        return mContentView;
    }

    public BaseUiFragment setLableText(CharSequence lableText) {

        mLableText = lableText;
        return this;
    }

    public BaseUiFragment setLableText(@StringRes int resId) {

        mLableText = getActivity() == null ? BaseApplication.getAppString(resId) : getString(resId);
        return this;
    }

    public CharSequence getLableText() {

        return mLableText;
    }

    public void onVisible() {

    }

    protected boolean isFinishing() {

        return getActivity() == null || getActivity().isFinishing();
    }

    protected boolean isNetworkEnable() {

        return DeviceUtil.isNetworkEnable();
    }

    protected boolean isNetworkDisable() {

        return DeviceUtil.isNetworkDisable();
    }

    protected void showToast(String text) {

        ToastUtil.showToast(text);
    }

    protected void showToast(@StringRes int resId) {

        showToast(getString(resId));
    }

    protected void showToast(@StringRes int resId, Object... formatArgs) {

        showToast(getString(resId, formatArgs));
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

    protected void showImageView(ImageView v, @DrawableRes int resId) {

        ViewUtil.showImageView(v, resId);
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

    protected View inflateLayout(@LayoutRes int layoutResId) {

        return inflateLayout(layoutResId, null);
    }

    protected View inflateLayout(@LayoutRes int layoutResId, @Nullable ViewGroup root) {

        return getActivity().getLayoutInflater().inflate(layoutResId, root);
    }
}