package com.android.library.ui.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.android.library.BaseApplication;
import com.android.library.R;
import com.android.library.ui.fragment.interfaces.BaseView;
import com.android.library.utils.DimenCons;
import com.android.library.utils.SnackbarUtil;
import com.android.library.utils.ToastUtil;
import com.android.library.utils.ViewUtil;
import com.trello.rxlifecycle.components.support.RxFragment;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * 基本的UI框架
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseUiFragment extends RxFragment implements BaseView, DimenCons {

    private CharSequence mLableText;
    private FrameLayout mContentParent;
    private View mContentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContentParent = new FrameLayout(getActivity());
        return mContentParent;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        initData();
        initContentView();
    }

    protected final void setContentView(@LayoutRes int layoutResId) {

        setContentView(inflateLayout(layoutResId, mContentParent, true));
    }

    protected final void setContentView(View contentView) {

        mContentView = contentView;
        wrapContentView(mContentParent, mContentView);
    }

    protected void wrapContentView(FrameLayout contentParent, View contentView) {

        // add transition animation
//        LayoutTransition lt = new LayoutTransition();
//        lt.setDuration(100);
//        mContentParent.setLayoutTransition(lt);

        // content view
        if (contentParent != contentView)
            contentParent.addView(contentView, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    protected void initData() {
    }

    protected void initContentView() {
    }

    protected final FrameLayout getContentParent() {

        return mContentParent;
    }

    protected final View getContentView() {

        return mContentView;
    }

    public final BaseUiFragment setLableText(CharSequence lableText) {

        mLableText = lableText;
        return this;
    }

    public final BaseUiFragment setLableText(@StringRes int resId) {

        mLableText = getActivity() == null ? BaseApplication.getAppString(resId) : getString(resId);
        return this;
    }

    public final CharSequence getLableText() {

        return mLableText;
    }

    public void onVisible() {

    }

    protected final boolean isFinishing() {

        return getActivity() == null || getActivity().isFinishing();
    }

    @Override
    public final void showToast(String text) {

        ToastUtil.showToast(text);
    }

    @Override
    public final void showToast(@StringRes int resId) {

        showToast(getString(resId));
    }

    @Override
    public final void showToast(@StringRes int resId, Object... formatArgs) {

        showToast(getString(resId, formatArgs));
    }

    @Override
    @SuppressWarnings("ResourceType")
    public final void showSnackbar(@NonNull CharSequence text) {

        showSnackbar(text, LENGTH_SHORT);
    }

    @Override
    public final void showSnackbar(@NonNull CharSequence text, @Snackbar.Duration int duration) {

        showSnackbar(text, duration, -1);
    }

    @Override
    public final void showSnackbar(@NonNull CharSequence text, @Snackbar.Duration int duration, @ColorInt int textColor) {

        showSnackbar(text, duration, -1, textColor);
    }

    @Override
    public final void showSnackbar(@NonNull CharSequence text, @Snackbar.Duration int duration, @ColorInt int bgColor, @ColorInt int textColor) {

        if (textColor == -1)
            textColor = getResources().getColor(R.color.color_text_primary);
        SnackbarUtil.showSnackbar(getContentView(), text, duration, bgColor, textColor);
    }

    @Override
    public final void showView(View v) {

        ViewUtil.showView(v);
    }

    @Override
    public final void hideView(View v) {

        ViewUtil.hideView(v);
    }

    @Override
    public final void goneView(View v) {

        ViewUtil.goneView(v);
    }

    @Override
    public final void showImageView(ImageView v, @DrawableRes int resId) {

        ViewUtil.showImageView(v, resId);
    }

    @Override
    public final void showImageView(ImageView v, Drawable drawable) {

        ViewUtil.showImageView(v, drawable);
    }

    @Override
    public final void hideImageView(ImageView v) {

        ViewUtil.hideImageView(v);
    }

    @Override
    public final void goneImageView(ImageView v) {

        ViewUtil.goneImageView(v);
    }

    protected final View inflateLayout(@LayoutRes int layoutResId) {

        return inflateLayout(layoutResId, null);
    }

    protected final View inflateLayout(@LayoutRes int layoutResId, @Nullable ViewGroup root) {

        return getActivity().getLayoutInflater().inflate(layoutResId, root);
    }

    protected final View inflateLayout(@LayoutRes int layoutResId, @Nullable ViewGroup root, boolean attachToRoot) {

        return getActivity().getLayoutInflater().inflate(layoutResId, root, attachToRoot);
    }
}
