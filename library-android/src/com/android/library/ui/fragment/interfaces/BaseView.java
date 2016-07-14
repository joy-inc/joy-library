package com.android.library.ui.fragment.interfaces;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;

import com.trello.rxlifecycle.FragmentLifecycleProvider;

/**
 * Created by Daisw on 16/6/24.
 */
public interface BaseView extends FragmentLifecycleProvider {

    void showView(View v);
    void hideView(View v);
    void goneView(View v);

    void showImageView(ImageView iv, @DrawableRes int resId);
    void showImageView(ImageView iv, Drawable drawable);
    void hideImageView(ImageView iv);
    void goneImageView(ImageView iv);

    void showToast(String text);
    void showToast(@StringRes int resId);
    void showToast(@StringRes int resId, Object... formatArgs);
}
