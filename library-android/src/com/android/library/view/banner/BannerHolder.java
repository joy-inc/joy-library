package com.android.library.view.banner;

import android.content.Context;
import android.view.View;

/**
 * Created by KEVIN.DAI on 15/12/17.
 */
public interface BannerHolder<T> {

    View createView(Context context);

    void invalidate(int position, T t);
}