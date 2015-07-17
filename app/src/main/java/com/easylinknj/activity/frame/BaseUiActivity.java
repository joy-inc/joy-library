package com.easylinknj.activity.frame;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.easylinknj.R;
import com.easylinknj.utils.DeviceUtil;
import com.easylinknj.utils.DimenCons;
import com.easylinknj.utils.ToastUtil;
import com.easylinknj.utils.ViewUtil;

/**
 * 基本的UI框架
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseUiActivity extends Activity implements DimenCons {

    private ViewGroup mVgTitleBar;
    private TextView mTvTitle;

    @Override
    public void setContentView(int layoutResId) {

        setContentView(inflateLayout(layoutResId));
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

    protected void wrapContentView(ViewGroup rootView, View contentView) {

        // title bar
        addTitleView(rootView);

        // content view
        addContentView(rootView, contentView);
    }

    private void addTitleView(ViewGroup rootView) {

        mVgTitleBar = (ViewGroup) inflateLayout(R.layout.view_titlebar);
        mTvTitle = (TextView) mVgTitleBar.findViewById(R.id.tvTitle);
        for (int i = 0; i < mVgTitleBar.getChildCount(); i++)
            mVgTitleBar.getChildAt(i).setTranslationY(STATUS_BAR_HEIGHT / 2);// 纵向正偏移，使其纵向居中
        rootView.addView(mVgTitleBar, new LayoutParams(LayoutParams.MATCH_PARENT, STATUS_BAR_HEIGHT + TITLE_BAR_HEIGHT));
    }

    private void addContentView(ViewGroup rootView, View contentView) {

        LayoutParams contentLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        contentLp.topMargin = STATUS_BAR_HEIGHT + TITLE_BAR_HEIGHT;
        rootView.addView(contentView, contentLp);
    }

    protected abstract void initData();

    protected abstract void initTitleView();

    protected abstract void initContentView();

    protected void setTitleBackgroundColor(int color) {

        mVgTitleBar.setBackgroundColor(color);
    }

    protected void setTitleBackgroundColorResId(int colorResId) {

        setTitleBackgroundColor(getResources().getColor(colorResId));
    }

    protected void setTitleText(String text) {

        mTvTitle.setText(text);
    }

    protected void setTitleText(int resId) {

        setTitleText(getString(resId));
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

    protected View inflateLayout(int resource) {

        return ViewUtil.inflateLayout(resource);
    }

    protected View inflateLayout(int resource, ViewGroup root) {

        return ViewUtil.inflateLayout(resource, root);
    }
}
