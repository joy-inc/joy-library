package com.android.library.utils;

import android.content.Context;
import android.support.annotation.LayoutRes;

/**
 * Created by Daisw on 16/8/4.
 */

public class LayoutInflater {

    public static <T> T inflate(Context context, @LayoutRes int layoutResId) {

        return (T) android.view.LayoutInflater.from(context).inflate(layoutResId, null);
    }
}
