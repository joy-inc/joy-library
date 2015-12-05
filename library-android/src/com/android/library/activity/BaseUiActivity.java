package com.android.library.activity;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.library.R;
import com.android.library.utils.DeviceUtil;
import com.android.library.utils.DimenCons;
import com.android.library.utils.ToastUtil;
import com.android.library.utils.ViewUtil;

/**
 * 基本的UI框架
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseUiActivity extends AppCompatActivity implements DimenCons {

    private Toolbar mToolbar;
    private FrameLayout mFlRoot;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        setContentView(inflateLayout(layoutResID));
    }

    @Override
    public void setContentView(View contentView) {

        mFlRoot = new FrameLayout(this);
        wrapContentView(mFlRoot, contentView);
        super.setContentView(mFlRoot);

        initData();
        initTitleView();
        initContentView();
    }

    protected void wrapContentView(FrameLayout rootView, View contentView) {

        TypedArray a = obtainStyledAttributes(R.styleable.Toolbar);
        boolean overlay = a.getBoolean(R.styleable.Toolbar_overlay, false);
        int height = a.getDimensionPixelSize(R.styleable.Toolbar_android_minHeight, TITLE_BAR_HEIGHT);
        a.recycle();

        // content view
        LayoutParams contentLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        contentLp.topMargin = overlay ? -STATUS_BAR_HEIGHT : height;
        rootView.addView(contentView, contentLp);

        // toolbar
        mToolbar = (Toolbar) inflateLayout(R.layout.lib_view_toolbar);
        setSupportActionBar(mToolbar);
        LayoutParams toolbarLp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        toolbarLp.gravity = Gravity.TOP;
        rootView.addView(mToolbar, toolbarLp);
    }

    protected void initData() {
    }

    protected void initTitleView() {
    }

    protected void initContentView() {
    }

    protected FrameLayout getRootView() {

        return mFlRoot;
    }

    protected Toolbar getToolbar() {

        return mToolbar;
    }

    protected LayoutParams getToolbarLp() {

        return (LayoutParams) mToolbar.getLayoutParams();
    }

    protected void setStatusBarTranslucent(boolean translucent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            if (translucent) {

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
        }
    }

    protected void setStatusBarColorResId(@ColorRes int colorResId) {

        setStatusBarColor(getResources().getColor(colorResId));
    }

    protected void setStatusBarColor(@ColorInt int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(color);
    }

    protected void setTitleBgColorResId(@ColorRes int colorResId) {

        setTitleBgColor(getResources().getColor(colorResId));
    }

    protected void setTitleBgColor(@ColorInt int color) {

        mToolbar.setBackgroundColor(color);
    }

    protected void setTitleText(@StringRes int resId) {

        setTitleText(getString(resId));
    }

    protected void setTitleText(String text) {

        mToolbar.setTitle(text);
    }

    protected void setSubTitle(@StringRes int resId) {

        setSubtitle(getString(resId));
    }

    protected void setSubtitle(String text) {

        mToolbar.setSubtitle(text);
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

    protected void addTitleLeftView(@DrawableRes int resId, View.OnClickListener lisn) {

        mToolbar.setNavigationIcon(resId);
        mToolbar.setNavigationOnClickListener(lisn);
    }

    protected TextView addTitleMiddleView(@StringRes int resId) {

        return addTitleMiddleView(getResources().getString(resId));
    }

    protected TextView addTitleMiddleView(String str) {

        TextView tvTitle = new TextView(this);
        tvTitle.setTextAppearance(this, R.style.lib_style_toolbar_title);
        tvTitle.setSingleLine();
        tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvTitle.setText(str);
        return (TextView) addTitleMiddleView(tvTitle, null);
    }

    protected View addTitleMiddleView(View v, View.OnClickListener lisn) {

        v.setOnClickListener(lisn);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        mToolbar.addView(v, lp);
        return v;
    }

    protected ImageView addTitleRightView(@DrawableRes int resId, View.OnClickListener lisn) {

        ImageView iv = new ImageView(this);
        iv.setImageResource(resId);
        return (ImageView) addTitleRightView(iv, lisn);
    }

    protected View addTitleRightView(View v, View.OnClickListener lisn) {

        v.setOnClickListener(lisn);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT;
        lp.rightMargin = getToolbar().getContentInsetLeft();
        mToolbar.addView(v, lp);
        return v;
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

        return getLayoutInflater().inflate(layoutResId, root);
    }
}