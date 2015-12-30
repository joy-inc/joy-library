package com.android.library.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout.LayoutParams;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.library.R;
import com.android.library.adapter.ExFragmentPagerAdapter;
import com.android.library.utils.DeviceUtil;
import com.android.library.utils.DimenCons;
import com.android.library.utils.ToastUtil;
import com.android.library.utils.ViewUtil;
import com.android.library.view.ExViewPager;

import java.util.List;

/**
 * 基本的UI框架
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseTabActivity extends AppCompatActivity implements DimenCons {

    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private FloatingActionButton mFloatActionBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_act_tab);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        setContentView(inflateLayout(layoutResID));
    }

    @Override
    public void setContentView(View contentView) {

        super.setContentView(contentView);

        initData();
        initTitleView();
        initContentView();
    }

    protected void initData() {
    }

    protected void initTitleView() {

        // toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(null);
    }

    protected void initContentView() {

        // view pager
        ExViewPager viewPager = (ExViewPager) findViewById(R.id.viewpager);
        ExFragmentPagerAdapter pagerAdapter = new ExFragmentPagerAdapter(getSupportFragmentManager());
        pagerAdapter.setFragmentItemDestoryEnable(isPagerItemDestoryEnable());
        pagerAdapter.setFragments(getFragments());
        viewPager.setAdapter(pagerAdapter);
        // tab layout
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mTabLayout.setupWithViewPager(viewPager);
        // float action bar
        mFloatActionBtn = (FloatingActionButton) findViewById(R.id.fab);
        setFloatActionBtnDisable();
    }

    protected boolean isPagerItemDestoryEnable() {

        return false;
    }

    protected abstract List<? extends BaseUiFragment> getFragments();

    protected Toolbar getToolbar() {

        return mToolbar;
    }

    protected LayoutParams getToolbarLp() {

        return (LayoutParams) mToolbar.getLayoutParams();
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

        addTitleLeftView(R.drawable.ic_back, new View.OnClickListener() {

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

        int size = getToolbarLp().height;
        ib.setMinimumWidth(size);
        ib.setMinimumHeight(size);
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

    protected ImageButton addTitleRightView(@DrawableRes int resId, View.OnClickListener lisn) {

        ImageButton ib = new ImageButton(this, null, R.attr.toolbarNavigationButtonStyle);
//        ib.setImageDrawable(mTintManager.getDrawable(resId));
        ib.setImageResource(resId);
        return (ImageButton) addTitleRightView(ib, lisn);
    }

    protected View addTitleRightView(View v, View.OnClickListener lisn) {

        int size = getToolbarLp().height;
        v.setMinimumWidth(size);
        v.setMinimumHeight(size);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            v.setBackgroundResource(R.drawable.control_background_52dp_material);

        v.setOnClickListener(lisn);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT;
        mToolbar.addView(v, lp);
        return v;
    }

    protected TabLayout getTabLayout() {

        return mTabLayout;
    }

    /**
     * Sets the tab indicator's color for the currently selected tab.
     *
     * @param color color to use for the indicator
     */
    protected void setTabIndicatorColor(@ColorInt int color) {

        mTabLayout.setSelectedTabIndicatorColor(color);
    }

    /**
     * Sets the tab indicator's height for the currently selected tab.
     *
     * @param height height to use for the indicator in pixels
     */
    protected void setTabIndicatorHeight(int height) {

        mTabLayout.setSelectedTabIndicatorHeight(height);
    }

    /**
     * Sets the text colors for the different states (normal, selected) used for the tabs.
     */
    protected void setTabTextColors(@ColorInt int normalColor, @ColorInt int selectedColor) {

        mTabLayout.setTabTextColors(normalColor, selectedColor);
    }

    protected FloatingActionButton getFloatActionBtn() {

        return mFloatActionBtn;
    }

    protected void setFloatActionBtnEnable(@DrawableRes int resId, View.OnClickListener lisn) {

        setFloatActionBtnEnable(getResources().getDrawable(resId), lisn);
    }

    protected void setFloatActionBtnEnable(Drawable drawable, View.OnClickListener lisn) {

        mFloatActionBtn.setEnabled(true);
        showImageView(mFloatActionBtn, drawable);
        mFloatActionBtn.setOnClickListener(lisn);
    }

    protected void setFloatActionBtnDisable() {

        mFloatActionBtn.setEnabled(false);
        hideImageView(mFloatActionBtn);
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