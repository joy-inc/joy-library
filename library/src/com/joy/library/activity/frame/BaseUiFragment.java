package com.joy.library.activity.frame;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.joy.library.utils.DeviceUtil;
import com.joy.library.utils.DimenCons;
import com.joy.library.utils.ToastUtil;
import com.joy.library.utils.ViewUtil;

/**
 * 基本的UI框架
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseUiFragment extends Fragment implements DimenCons {

    private FrameLayout mFrameView;
    private CharSequence mLableText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFrameView = new FrameLayout(getActivity());
        return mFrameView;
    }

    protected void setContentView(@LayoutRes int layoutResID) {

        setContentView(inflateLayout(layoutResID));
    }

    protected void setContentView(View contentView) {

        wrapContentView(mFrameView, contentView);
        initData();
        initContentView();
    }

    protected void wrapContentView(FrameLayout rootView, View contentView) {

        // content view
        rootView.addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    protected void initData() {
    }

    protected void initContentView() {
    }

    public BaseUiFragment setLableText(CharSequence lableText) {

        mLableText = lableText;
        return this;
    }

    public CharSequence getLableText() {

        return mLableText;
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

    protected void showToast(int resId) {

        showToast(getString(resId));
    }

    protected void showToast(int resId, Object... formatArgs) {

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

    protected View inflateLayout(@LayoutRes int layoutResID) {

        return inflateLayout(layoutResID, null);
    }

    protected View inflateLayout(@LayoutRes int layoutResID, @Nullable ViewGroup root) {

        return getActivity().getLayoutInflater().inflate(layoutResID, root);
    }
}