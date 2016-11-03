package com.android.library.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout.LayoutParams;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.library.R;
import com.android.library.adapter.ExFragmentPagerAdapter;
import com.android.library.ui.activity.interfaces.BaseView;
import com.android.library.ui.fragment.BaseUiFragment;
import com.android.library.utils.CollectionUtil;
import com.android.library.utils.DimenCons;
import com.android.library.utils.SnackbarUtil;
import com.android.library.utils.ToastUtil;
import com.android.library.utils.ViewUtil;
import com.android.library.view.ExViewPager;
import com.android.library.view.MagicToolbar;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;

/**
 * 基本的UI框架
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseTabActivity extends RxAppCompatActivity implements BaseView, DimenCons {

    private View mContentView;
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
    public final void setContentView(@LayoutRes int layoutResId) {

        setContentView(inflateLayout(layoutResId));
    }

    @Override
    public final void setContentView(View contentView) {

        mContentView = contentView;

        super.setContentView(contentView);

        initData();
        initTitleView();
        initContentView();
    }

    protected final View getContentView() {

        return mContentView;
    }

    protected final FrameLayout.LayoutParams getContentViewLp() {

        return (FrameLayout.LayoutParams) mContentView.getLayoutParams();
    }

    protected void initData() {
    }

    protected void initTitleView() {

        // toolbar
        mToolbar = (MagicToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        mTabLayout.setSelectedTabIndicatorHeight(DP_1_PX * 3);
        mTabLayout.setupWithViewPager(viewPager);
        // float action bar
        mFloatActionBtn = (FloatingActionButton) findViewById(R.id.fab);
        setFloatActionBtnDisable();
    }

    protected final BaseUiFragment getFragment(int location) {

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

    protected final MagicToolbar getToolbar() {

        return mToolbar;
    }

    public final LayoutParams getToolbarLp() {

        return (LayoutParams) mToolbar.getLayoutParams();
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

        return addTitleLeftBackView(R.drawable.ic_arrow_back_white_24dp);
    }

    protected final ImageButton addTitleLeftBackView(@DrawableRes int resId) {

        return addTitleLeftView(resId, v -> finish());
    }

    protected final ImageButton addTitleLeftView(@DrawableRes int resId, View.OnClickListener lisn) {

        return mToolbar.addTitleLeftView(resId, lisn);
    }

    protected final TextView addTitleMiddleView(@StringRes int resId) {

        return addTitleMiddleView(getResources().getString(resId));
    }

    protected final TextView addTitleMiddleView(String str) {

        return mToolbar.addTitleMiddleView(str);
    }

    protected final View addTitleMiddleView(View v, View.OnClickListener lisn) {

        return mToolbar.addTitleMiddleView(v, lisn);
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

    protected final TabLayout getTabLayout() {

        return mTabLayout;
    }

    /**
     * Sets the tab indicator's color for the currently selected tab.
     *
     * @param color color to use for the indicator
     */
    protected final void setTabIndicatorColor(@ColorInt int color) {

        mTabLayout.setSelectedTabIndicatorColor(color);
    }

    /**
     * Sets the tab indicator's height for the currently selected tab.
     *
     * @param height height to use for the indicator in pixels
     */
    protected final void setTabIndicatorHeight(int height) {

        mTabLayout.setSelectedTabIndicatorHeight(height);
    }

    /**
     * Sets the text colors for the different states (normal, selected) used for the tabs.
     */
    protected final void setTabTextColors(@ColorInt int normalColor, @ColorInt int selectedColor) {

        mTabLayout.setTabTextColors(normalColor, selectedColor);
    }

    protected final FloatingActionButton getFloatActionBtn() {

        return mFloatActionBtn;
    }

    protected final void setFloatActionBtnEnable(@DrawableRes int resId, View.OnClickListener lisn) {

        setFloatActionBtnEnable(getResources().getDrawable(resId), lisn);
    }

    protected final void setFloatActionBtnEnable(Drawable drawable, View.OnClickListener lisn) {

        mFloatActionBtn.setEnabled(true);
        showImageView(mFloatActionBtn, drawable);
        mFloatActionBtn.setOnClickListener(lisn);
    }

    protected final void setFloatActionBtnDisable() {

        mFloatActionBtn.setEnabled(false);
        hideImageView(mFloatActionBtn);
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
    public void showSnackbar(@NonNull CharSequence text) {

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

        return getLayoutInflater().inflate(layoutResId, root);
    }
}
