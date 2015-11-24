package com.android.library.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

    protected void addTitleRightView(View v, View.OnClickListener lisn) {

        v.setOnClickListener(lisn);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.RIGHT;
        lp.rightMargin = getToolbar().getContentInsetLeft();
        mToolbar.addView(v, lp);
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

    protected View inflateLayout(@LayoutRes int layoutResID) {

        return inflateLayout(layoutResID, null);
    }

    protected View inflateLayout(@LayoutRes int layoutResID, @Nullable ViewGroup root) {

        return getLayoutInflater().inflate(layoutResID, root);
    }
}