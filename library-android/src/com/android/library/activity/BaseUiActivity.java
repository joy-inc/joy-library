package com.android.library.activity;

import android.annotation.TargetApi;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
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

    private FrameLayout mFlRoot;
    private View mContentView;
    private Toolbar mToolbar;
    private int mTbHeight;
    private boolean isNoTitle, isOverlay;
    private boolean isSystemBarTrans;
//    private TintManager mTintManager;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        setContentView(inflateLayout(layoutResID));
    }

    @Override
    public void setContentView(View contentView) {

        mContentView = contentView;

        resolveThemeAttribute();

        mFlRoot = new FrameLayout(this);
        wrapContentView(mFlRoot, contentView);
        super.setContentView(mFlRoot);

        initData();
        initTitleView();
        initContentView();
    }

    private void resolveThemeAttribute() {

//        TintTypedArray a = TintTypedArray.obtainStyledAttributes(this, null, R.styleable.Toolbar);
        TypedArray a = obtainStyledAttributes(R.styleable.Toolbar);
        isNoTitle = a.getBoolean(R.styleable.Toolbar_noTitle, false);
        isOverlay = a.getBoolean(R.styleable.Toolbar_overlay, false);
        mTbHeight = a.getDimensionPixelSize(R.styleable.Toolbar_android_minHeight, TITLE_BAR_HEIGHT);
        a.recycle();

        // Keep the TintManager in case we need it later
//        mTintManager = a.getTintManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(android.R.style.Theme, typedValue, true);
            int[] attrs = new int[]{android.R.attr.windowTranslucentStatus, android.R.attr.windowTranslucentNavigation};
            TypedArray typedArray = obtainStyledAttributes(typedValue.resourceId, attrs);
            boolean isStatusTrans = typedArray.getBoolean(0, false);
            boolean isNavigationTrans = typedArray.getBoolean(1, false);
            isSystemBarTrans = isStatusTrans || isNavigationTrans;
            typedArray.recycle();
        }
    }

    protected void wrapContentView(FrameLayout rootView, View contentView) {

        // add transition animation
//        LayoutTransition lt = new LayoutTransition();
//        lt.setDuration(100);
//        rootView.setLayoutTransition(lt);

        // content view
        LayoutParams contentLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rootView.addView(contentView, contentLp);

        if (isNoTitle) {

            contentLp.topMargin = isSystemBarTrans ? -STATUS_BAR_HEIGHT : 0;
        } else {

            contentLp.topMargin = isOverlay ? isSystemBarTrans ? -STATUS_BAR_HEIGHT : 0 : isSystemBarTrans ? STATUS_BAR_HEIGHT + mTbHeight : mTbHeight;

            // toolbar
            mToolbar = (Toolbar) inflateLayout(R.layout.lib_view_toolbar);
            setSupportActionBar(mToolbar);
            LayoutParams toolbarLp = new LayoutParams(LayoutParams.MATCH_PARENT, mTbHeight);
            toolbarLp.topMargin = isSystemBarTrans ? STATUS_BAR_HEIGHT : 0;
            toolbarLp.gravity = Gravity.TOP;
            rootView.addView(mToolbar, toolbarLp);
            setTitle(null);
        }
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void setBackground(Drawable background) {

        mFlRoot.setBackground(background);
    }

    protected void setBackgroundResource(@DrawableRes int resId) {

        mFlRoot.setBackgroundResource(resId);
    }

    protected void setBackgroundColor(@ColorInt int color) {

        mFlRoot.setBackgroundColor(color);
    }

    protected View getContentView() {

        return mContentView;
    }

    protected LayoutParams getContentViewLp() {

        return (LayoutParams) mContentView.getLayoutParams();
    }

    protected Toolbar getToolbar() {

        return mToolbar;
    }

    protected LayoutParams getToolbarLp() {

        return (LayoutParams) mToolbar.getLayoutParams();
    }

    protected int getToolbarHeight() {

        return mTbHeight;
    }

    boolean isNoTitle() {

        return isNoTitle;
    }

    protected void setStatusBarColorResId(@ColorRes int colorResId) {

        setStatusBarColor(getResources().getColor(colorResId));
    }

    protected void setStatusBarColor(@ColorInt int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(color);
    }

    protected void setNavigationBarColorResId(@ColorRes int colorResId) {

        setNavigationBarColor(getResources().getColor(colorResId));
    }

    protected final void setNavigationBarColor(@ColorInt int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(color);
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

//        mToolbar.setLogo(resId);
        addTitleLeftView(resId, null);
    }

    protected void addTitleLeftBackView() {

        addTitleLeftBackView(R.drawable.ic_back);
    }

    protected void addTitleLeftBackView(@DrawableRes int resId) {

        addTitleLeftView(resId, new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    protected void addTitleLeftView(@DrawableRes int resId, View.OnClickListener lisn) {

//        mToolbar.setNavigationIcon(resId);
//        mToolbar.setNavigationOnClickListener(lisn);

        ImageButton ib = new ImageButton(this, null, R.attr.toolbarNavigationButtonStyle);
//        ib.setImageDrawable(mTintManager.getDrawable(resId));
        ib.setImageResource(resId);

        ib.setMinimumWidth(mTbHeight);
        ib.setMinimumHeight(mTbHeight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            ib.setBackgroundResource(R.drawable.control_background_52dp_material);

        if (lisn != null)
            ib.setOnClickListener(lisn);
        mToolbar.addView(ib);
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
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        mToolbar.addView(v, lp);
        return v;
    }

    protected View addTitleMiddleView(View v) {

        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        mToolbar.addView(v, lp);
        return v;
    }

    protected ImageButton addTitleRightView(@DrawableRes int resId, View.OnClickListener lisn) {

        ImageButton ib = new ImageButton(this, null, R.attr.toolbarNavigationButtonStyle);
//        ib.setImageDrawable(mTintManager.getDrawable(resId));
        ib.setImageResource(resId);
        return (ImageButton) addTitleRightView(ib, lisn);
    }

    protected View addTitleRightView(View v, View.OnClickListener lisn) {

        v.setMinimumWidth(mTbHeight);
        v.setMinimumHeight(mTbHeight);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            v.setBackgroundResource(R.drawable.control_background_52dp_material);

        v.setOnClickListener(lisn);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT;
        mToolbar.addView(v, lp);
        return v;
    }

    /**
     * fragment activity part
     */
    protected void addFragment(Fragment f, String tag) {

        if (f == null)
            return;

        getSupportFragmentManager().beginTransaction().add(f, tag).commitAllowingStateLoss();
    }

    protected void addFragment(int frameId, Fragment f) {

        if (f == null)
            return;

        getSupportFragmentManager().beginTransaction().add(frameId, f).commitAllowingStateLoss();
    }

    protected void addFragment(int frameId, Fragment f, String tag) {

        if (f == null)
            return;

        getSupportFragmentManager().beginTransaction().add(frameId, f, tag).commitAllowingStateLoss();
    }

    protected void replaceFragment(int frameId, Fragment f) {

        if (f == null)
            return;

        getSupportFragmentManager().beginTransaction().replace(frameId, f).commitAllowingStateLoss();
    }

    protected void replaceFragment(int frameId, Fragment f, String tag) {

        if (f == null)
            return;

        getSupportFragmentManager().beginTransaction().replace(frameId, f, tag).commitAllowingStateLoss();
    }

    protected void removeFragment(Fragment f) {

        if (f == null)
            return;

        getSupportFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
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

    protected String[] getStringArray(@ArrayRes int resId) {

        return getResources().getStringArray(resId);
    }
}