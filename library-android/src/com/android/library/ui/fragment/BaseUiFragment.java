package com.android.library.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.android.library.BaseApplication;
import com.android.library.ui.fragment.interfaces.BaseView;
import com.android.library.utils.DimenCons;
import com.android.library.utils.ToastUtil;
import com.android.library.utils.ViewUtil;
import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * 基本的UI框架
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseUiFragment extends RxFragment implements BaseView, DimenCons {

    private CharSequence mLableText;
    private FrameLayout mFlRoot;
    private View mContentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFlRoot = new FrameLayout(getActivity());
        return mFlRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        initData();
        initContentView();
    }

    protected void setContentView(@LayoutRes int layoutResID) {

        setContentView(inflateLayout(layoutResID));
    }

    protected void setContentView(View contentView) {

        mContentView = contentView;
        wrapContentView(mFlRoot, mContentView);
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

    @Override
    public void showToast(String text) {

        ToastUtil.showToast(text);
    }

    @Override
    public void showToast(@StringRes int resId) {

        showToast(getString(resId));
    }

    @Override
    public void showToast(@StringRes int resId, Object... formatArgs) {

        showToast(getString(resId, formatArgs));
    }

    @Override
    public void showView(View v) {

        ViewUtil.showView(v);
    }

    @Override
    public void hideView(View v) {

        ViewUtil.hideView(v);
    }

    @Override
    public void goneView(View v) {

        ViewUtil.goneView(v);
    }

    @Override
    public void showImageView(ImageView v, @DrawableRes int resId) {

        ViewUtil.showImageView(v, resId);
    }

    @Override
    public void showImageView(ImageView v, Drawable drawable) {

        ViewUtil.showImageView(v, drawable);
    }

    @Override
    public void hideImageView(ImageView v) {

        ViewUtil.hideImageView(v);
    }

    @Override
    public void goneImageView(ImageView v) {

        ViewUtil.goneImageView(v);
    }

    protected View inflateLayout(@LayoutRes int layoutResId) {

        return inflateLayout(layoutResId, null);
    }

    protected View inflateLayout(@LayoutRes int layoutResId, @Nullable ViewGroup root) {

        return getActivity().getLayoutInflater().inflate(layoutResId, root);
    }
}
