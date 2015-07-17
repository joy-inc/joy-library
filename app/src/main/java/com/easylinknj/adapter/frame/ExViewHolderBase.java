package com.easylinknj.adapter.frame;

import com.easylinknj.adapter.frame.ExViewHolder;

public abstract class ExViewHolderBase implements ExViewHolder {

    protected int mPosition;

    @Override
    public void invalidateConvertView(int position) {

        mPosition = position;
        invalidateConvertView();
    }

    public abstract void invalidateConvertView();
}