package com.android.library.ui.activity;

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
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.library.R;
import com.android.library.adapter.ExFragmentPagerAdapter;
import com.android.library.ui.activity.interfaces.BaseView;
import com.android.library.ui.fragment.BaseUiFragment;
import com.android.library.utils.CollectionUtil;
import com.android.library.utils.DimenCons;
import com.android.library.utils.ToastUtil;
import com.android.library.utils.ViewUtil;
import com.android.library.view.ExViewPager;
import com.android.library.view.MagicToolbar;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

/**
 * 基本的UI框架
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseTabActivity extends RxAppCompatActivity implements BaseView, DimenCons {

    private MagicToolbar mToolbar;
    private TabLayout mTabLayout;
    private FloatingActionButton mFloatActionBtn;
    private List<? extends BaseUiFragment> mFragments;
    private int mCurrentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_act_tab);
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (CollectionUtil.isNotEmpty(mFragments))
            mFragments.get(mCurrentPosition).onVisible();
    }

    @Override
    protected void onPause() {

        super.onPause();
        if (isFinishing()) {

            if (mFragments != null) {

                mFragments.clear();
                mFragments = null;
            }
        }
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
        mToolbar = (MagicToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(null);
    }

    protected void initContentView() {

        mFragments = getFragments();

        // view pager
        ExFragmentPagerAdapter pagerAdapter = new ExFragmentPagerAdapter(getSupportFragmentManager());
        pagerAdapter.setFragmentItemDestoryEnable(isPagerItemDestoryEnable());
        pagerAdapter.setFragments(mFragments);
        ExViewPager viewPager = (ExViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(getPageChangeListener());
        viewPager.setAdapter(pagerAdapter);
        // tab layout
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        mTabLayout.setupWithViewPager(viewPager);
        // float action bar
        mFloatActionBtn = (FloatingActionButton) findViewById(R.id.fab);
        setFloatActionBtnDisable();
    }

    protected BaseUiFragment getFragment(int location) {

        return CollectionUtil.isEmpty(mFragments) ? null : mFragments.get(location);
    }

    private OnPageChangeListener getPageChangeListener() {

        return new OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                mCurrentPosition = position;
                mFragments.get(position).onVisible();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
    }

    protected boolean isPagerItemDestoryEnable() {

        return false;
    }

    protected abstract List<? extends BaseUiFragment> getFragments();

    protected MagicToolbar getToolbar() {

        return mToolbar;
    }

    public LayoutParams getToolbarLp() {

        return (LayoutParams) mToolbar.getLayoutParams();
    }

    protected void setStatusBarColorResId(@ColorRes int colorResId) {

        setStatusBarColor(getResources().getColor(colorResId));
    }

    protected void setStatusBarColor(@ColorInt int color) {

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

    protected ImageButton setTitleLogo(@DrawableRes int resId) {

        return mToolbar.setTitleLogo(resId);
    }

    protected ImageButton addTitleLeftBackView() {

        return addTitleLeftBackView(R.drawable.ic_back);
    }

    protected ImageButton addTitleLeftBackView(@DrawableRes int resId) {

        return addTitleLeftView(resId, v -> finish());
    }

    protected ImageButton addTitleLeftView(@DrawableRes int resId, View.OnClickListener lisn) {

        return mToolbar.addTitleLeftView(resId, lisn);
    }

    protected TextView addTitleMiddleView(@StringRes int resId) {

        return addTitleMiddleView(getResources().getString(resId));
    }

    protected TextView addTitleMiddleView(String str) {

        return mToolbar.addTitleMiddleView(str);
    }

    protected View addTitleMiddleView(View v, View.OnClickListener lisn) {

        return mToolbar.addTitleMiddleView(v, lisn);
    }

    protected ImageButton addTitleRightView(@DrawableRes int resId) {

        return mToolbar.addTitleRightView(resId);
    }

    protected ImageButton addTitleRightView(@DrawableRes int resId, View.OnClickListener lisn) {

        return mToolbar.addTitleRightView(resId, lisn);
    }

    protected View addTitleRightView(View v, View.OnClickListener lisn) {

        return mToolbar.addTitleRightView(v, lisn);
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

        return getLayoutInflater().inflate(layoutResId, root);
    }
}
