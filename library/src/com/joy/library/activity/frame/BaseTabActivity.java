package com.joy.library.activity.frame;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.joy.library.R;
import com.joy.library.utils.DeviceUtil;
import com.joy.library.utils.DimenCons;
import com.joy.library.utils.ToastUtil;
import com.joy.library.utils.ViewUtil;

/**
 * 基本的UI框架
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseTabActivity extends AppCompatActivity implements DimenCons {

    private Toolbar mToolbar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        setContentView(inflateLayout(layoutResID));
    }

    @Override
    public void setContentView(View contentView) {

        FrameLayout flRoot = new FrameLayout(this);
        wrapContentView(flRoot, contentView);
        super.setContentView(flRoot);

        initData();
        initTitleView();
        initContentView();
    }

    protected void wrapContentView(FrameLayout rootView, View contentView) {

        // toolbar
        mToolbar = (Toolbar) inflateLayout(R.layout.lib_view_toolbar);
        setSupportActionBar(mToolbar);
        LayoutParams toolbarLp = new LayoutParams(LayoutParams.MATCH_PARENT, TITLE_BAR_HEIGHT);
        toolbarLp.gravity = Gravity.TOP;
        rootView.addView(mToolbar, toolbarLp);

        // content view
        LayoutParams contentLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        contentLp.topMargin = TITLE_BAR_HEIGHT;
        rootView.addView(contentView, contentLp);
    }

    protected void initData() {
    }

    protected void initTitleView() {
    }

    protected void initContentView() {
    }

    protected Toolbar getToolbar() {

        return mToolbar;
    }

    protected void setTitleBgColor(@ColorInt int color) {

        mToolbar.setBackgroundColor(color);
    }

    protected void setTitleBgColorResId(@ColorRes int colorResId) {

        setTitleBgColor(getResources().getColor(colorResId));
    }

    protected void setTitleText(String text) {

        mToolbar.setTitle(text);
    }

    protected void setTitleText(@StringRes int resId) {

        setTitleText(getString(resId));
    }

    protected void setSubtitle(String text) {

        mToolbar.setSubtitle(text);
    }

    protected void setSubTitle(@StringRes int resId) {

        setSubtitle(getString(resId));
    }

    protected void setTitleTextColor(@ColorInt int color) {

        mToolbar.setTitleTextColor(color);
    }

    protected void setSubtitleTextColor(@ColorInt int color) {

        mToolbar.setSubtitleTextColor(color);
    }

    protected void setTitleLogo(@DrawableRes int resId) {

        mToolbar.setLogo(resId);
    }

    protected void addTitleLeftBackView() {

        addTitleLeftView(R.drawable.abc_ic_ab_back_mtrl_am_alpha, new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    protected void addTitleLeftView(@DrawableRes int resId, View.OnClickListener listener) {

        mToolbar.setNavigationIcon(resId);
        mToolbar.setNavigationOnClickListener(listener);
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

        return getLayoutInflater().inflate(layoutResID, root);
    }
}