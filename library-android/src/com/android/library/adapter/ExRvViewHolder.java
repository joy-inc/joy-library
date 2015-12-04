package com.android.library.adapter;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.library.BaseApplication;
import com.android.library.utils.ViewUtil;

/**
 * Created by KEVIN.DAI on 15/11/10.
 */
public abstract class ExRvViewHolder<T> extends RecyclerView.ViewHolder {

    public ExRvViewHolder(View itemView) {

        super(itemView);
    }

    protected abstract void invalidateItemView(int position, T t);

    protected final void showView(View v) {

        ViewUtil.showView(v);
    }

    protected final void hideView(View v) {

        ViewUtil.hideView(v);
    }

    protected final void goneView(View v) {

        ViewUtil.goneView(v);
    }

    protected final String getString(@StringRes int resId) {

        return BaseApplication.getAppString(resId);
    }

    protected final String getString(@StringRes int resId, Object... formatArgs) {

        return BaseApplication.getAppString(resId, formatArgs);
    }
}