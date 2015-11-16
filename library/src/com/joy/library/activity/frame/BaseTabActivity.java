package com.joy.library.activity.frame;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.joy.library.R;
import com.joy.library.adapter.frame.ExFragmentPagerAdapter;
import com.joy.library.utils.DeviceUtil;
import com.joy.library.utils.DimenCons;
import com.joy.library.utils.ToastUtil;
import com.joy.library.utils.ViewUtil;
import com.joy.library.view.ExViewPager;

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
        hideView(mFloatActionBtn);
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

    protected TabLayout getTabLayout() {

        return mTabLayout;
    }

    /**
     * Sets the tab indicator's height for the currently selected tab.
     *
     * @param height height to use for the indicator in pixels
     */
    protected void setTabIndicatorHeight(int height) {

        mTabLayout.setSelectedTabIndicatorHeight(height);
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