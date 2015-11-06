package com.easy.utils;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.easy.EasyApplication;

public class ViewUtil {

    public static void showView(View v) {

        if (v == null)
            return;

        if (v.getVisibility() != View.VISIBLE)
            v.setVisibility(View.VISIBLE);
    }

    public static void hideView(View v) {

        if (v == null)
            return;

        if (v.getVisibility() != View.INVISIBLE)
            v.setVisibility(View.INVISIBLE);
    }

    public static void goneView(View v) {

        if (v == null)
            return;

        if (v.getVisibility() != View.GONE)
            v.setVisibility(View.GONE);
    }

    public static void showImageView(ImageView v, int imageResId) {

        if (v == null)
            return;

        if (imageResId > 0) {

            v.setImageResource(imageResId);
        } else {

            v.setImageDrawable(null);
        }

        if (v.getVisibility() != View.VISIBLE)
            v.setVisibility(View.VISIBLE);
    }

    public static void showImageView(ImageView v, Drawable drawable) {

        if (v == null)
            return;

        v.setImageDrawable(drawable);
        if (v.getVisibility() != View.VISIBLE)
            v.setVisibility(View.VISIBLE);
    }

    public static void hideImageView(ImageView v) {

        if (v == null)
            return;

        if (v.getVisibility() != View.INVISIBLE)
            v.setVisibility(View.INVISIBLE);
        v.setImageDrawable(null);
    }

    public static void goneImageView(ImageView v) {

        if (v == null)
            return;

        if (v.getVisibility() != View.GONE)
            v.setVisibility(View.GONE);
        v.setImageDrawable(null);
    }

    public static View inflateLayout(int resource) {

        return inflateLayout(resource, null);
    }

    public static View inflateLayout(int resource, ViewGroup root) {

        return LayoutInflater.from(EasyApplication.getContext()).inflate(resource, root);
    }
}
