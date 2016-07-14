package com.android.library.ui.activity;

import android.annotation.TargetApi;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
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
import com.android.library.ui.activity.interfaces.BaseView;
import com.android.library.utils.DimenCons;
import com.android.library.utils.ToastUtil;
import com.android.library.utils.ViewUtil;
import com.android.library.view.MagicToolbar;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * 基本的UI框架
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseUiActivity extends RxAppCompatActivity implements BaseView, DimenCons {

    private FrameLayout mFlRoot;
    private View mContentView;
    private MagicToolbar mToolbar;
    private int mTbHeight;
    private boolean isNoTitle, isOverlay;
    private boolean isSystemBarTrans;
//    private TintManager mTintManager;

    @Override
    public final void setContentView(@LayoutRes int layoutResID) {

        setContentView(inflateLayout(layoutResID));
    }

    @Override
    public final void setContentView(View contentView) {

        mContentView = contentView;

        resolveThemeAttribute();

        mFlRoot = new FrameLayout(this);
        wrapContentView(mFlRoot, contentView);
        super.setContentView(mFlRoot);

        initData();
        initTitleView();
        initContentView();
    }

    @SuppressWarnings("ResourceType")
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
            mToolbar = (MagicToolbar) inflateLayout(R.layout.lib_view_toolbar);
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

    protected final FrameLayout getRootView() {

        return mFlRoot;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected final void setBackground(Drawable background) {

        mFlRoot.setBackground(background);
    }

    protected final void setBackgroundResource(@DrawableRes int resId) {

        mFlRoot.setBackgroundResource(resId);
    }

    protected final void setBackgroundColor(@ColorInt int color) {

        mFlRoot.setBackgroundColor(color);
    }

    protected final View getContentView() {

        return mContentView;
    }

    protected final LayoutParams getContentViewLp() {

        return (LayoutParams) mContentView.getLayoutParams();
    }

    protected final MagicToolbar getToolbar() {

        return mToolbar;
    }

    protected final LayoutParams getToolbarLp() {

        return (LayoutParams) mToolbar.getLayoutParams();
    }

    protected final int getToolbarHeight() {

        return mTbHeight;
    }

    boolean isNoTitle() {

        return isNoTitle;
    }

    boolean isOverlay() {

        return isOverlay;
    }

    boolean isSystemBarTrans() {

        return isSystemBarTrans;
    }

    protected final void setStatusBarColorResId(@ColorRes int colorResId) {

        setStatusBarColor(getResources().getColor(colorResId));
    }

    protected final void setStatusBarColor(@ColorInt int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(color);
    }

    protected final void setNavigationBarColorResId(@ColorRes int colorResId) {

        setNavigationBarColor(getResources().getColor(colorResId));
    }

    protected final void setNavigationBarColor(@ColorInt int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(color);
    }

    protected final void setTitleBgColorResId(@ColorRes int colorResId) {

        setTitleBgColor(getResources().getColor(colorResId));
    }

    protected final void setTitleBgColor(@ColorInt int color) {

        mToolbar.setBackgroundColor(color);
    }

    protected final void setTitleBarAlpha(int alpha) {

        mToolbar.getBackground().setAlpha(alpha);
    }

    protected final void setTitleText(@StringRes int resId) {

        setTitleText(getString(resId));
    }

    protected final void setTitleText(String text) {

        mToolbar.setTitle(text);
    }

    protected final void setSubTitle(@StringRes int resId) {

        setSubtitle(getString(resId));
    }

    protected final void setSubtitle(String text) {

        mToolbar.setSubtitle(text);
    }

    protected final void setTitleTextColor(@ColorInt int color) {

        mToolbar.setTitleTextColor(color);
    }

    protected final void setSubtitleTextColor(@ColorInt int color) {

        mToolbar.setSubtitleTextColor(color);
    }

    protected final ImageButton setTitleLogo(@DrawableRes int resId) {

        return mToolbar.setTitleLogo(resId);
    }

    protected final ImageButton addTitleLeftBackView() {

        return addTitleLeftBackView(R.drawable.ic_back);
    }

    protected final ImageButton addTitleLeftBackView(@DrawableRes int resId) {

        return addTitleLeftView(resId, v -> finish());
    }

    protected final ImageButton addTitleLeftView(@DrawableRes int resId, View.OnClickListener lisn) {

        return mToolbar.addTitleLeftView(resId, lisn);
    }

    protected final TextView addTitleMiddleView(@StringRes int resId) {

        return addTitleMiddleView(getString(resId));
    }

    protected final TextView addTitleMiddleView(String str) {

        return mToolbar.addTitleMiddleView(str);
    }

    protected final View addTitleMiddleView(View v, View.OnClickListener lisn) {

        return mToolbar.addTitleMiddleView(v, lisn);
    }

    protected final View addTitleMiddleViewMatchParent(View v) {

        return mToolbar.addTitleMiddleViewWithLp(v, new Toolbar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    protected final ImageButton addTitleRightView(@DrawableRes int resId) {

        return mToolbar.addTitleRightView(resId);
    }

    protected final ImageButton addTitleRightView(@DrawableRes int resId, View.OnClickListener lisn) {

        return mToolbar.addTitleRightView(resId, lisn);
    }

    protected final View addTitleRightView(View v, View.OnClickListener lisn) {

        return mToolbar.addTitleRightView(v, lisn);
    }

    /**
     * fragment activity part
     */
    protected final void addFragment(Fragment f, String tag) {

        if (f == null)
            return;

        getSupportFragmentManager().beginTransaction().add(f, tag).commitAllowingStateLoss();
    }

    protected final void addFragment(int frameId, Fragment f) {

        if (f == null)
            return;

        getSupportFragmentManager().beginTransaction().add(frameId, f).commitAllowingStateLoss();
    }

    protected final void addFragment(int frameId, Fragment f, String tag) {

        if (f == null)
            return;

        getSupportFragmentManager().beginTransaction().add(frameId, f, tag).commitAllowingStateLoss();
    }

    protected final void replaceFragment(int frameId, Fragment f) {

        if (f == null)
            return;

        getSupportFragmentManager().beginTransaction().replace(frameId, f).commitAllowingStateLoss();
    }

    protected final void replaceFragment(int frameId, Fragment f, String tag) {

        if (f == null)
            return;

        getSupportFragmentManager().beginTransaction().replace(frameId, f, tag).commitAllowingStateLoss();
    }

    protected final void removeFragment(Fragment f) {

        if (f == null)
            return;

        getSupportFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
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

    protected View inflateLayout(@LayoutRes int layoutResId) {

        return inflateLayout(layoutResId, null);
    }

    protected View inflateLayout(@LayoutRes int layoutResId, @Nullable ViewGroup root) {

        return getLayoutInflater().inflate(layoutResId, root);
    }
}
