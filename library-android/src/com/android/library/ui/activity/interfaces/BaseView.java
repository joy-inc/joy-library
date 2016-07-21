package com.android.library.ui.activity.interfaces;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;

import com.trello.rxlifecycle.ActivityLifecycleProvider;
import com.trello.rxlifecycle.FragmentLifecycleProvider;

/**
 * Created by Daisw on 16/6/24.
 */
public interface BaseView extends ActivityLifecycleProvider {

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

    void showSnackbar(@NonNull CharSequence text);
    void showSnackbar(@NonNull CharSequence text, @Snackbar.Duration int duration);
    void showSnackbar(@NonNull CharSequence text, @Snackbar.Duration int duration, @ColorInt int textColor);
    void showSnackbar(@NonNull CharSequence text, @Snackbar.Duration int duration, @ColorInt int bgColor, @ColorInt int textColor);
}
