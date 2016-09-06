package com.android.library.adapter;

/**
 * Created by KEVIN.DAI on 15/7/16.
 *
 * @deprecated see {@link ExRvViewHolder}
 */
public abstract class ExViewHolderBase implements ExViewHolder {

    protected int mPosition;

    @Override
    public void invalidateConvertView(int position) {

        mPosition = position;
        invalidateConvertView();
    }

    public abstract void invalidateConvertView();
}