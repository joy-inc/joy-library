package com.android.library.adapter;

public abstract class ExViewHolderBase implements ExViewHolder {

    protected int mPosition;

    @Override
    public void invalidateConvertView(int position) {

        mPosition = position;
        invalidateConvertView();
    }

    public abstract void invalidateConvertView();
}