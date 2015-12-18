package com.android.library.view.banner;

import android.view.View;
import android.view.ViewGroup;

import com.android.library.adapter.ExPagerAdapter;
import com.android.library.view.banner.indicator.IndicatorAdapter;

/**
 * Created by KEVIN.DAI on 15/12/17.
 */
public class BannerAdapter<T> extends ExPagerAdapter<T> implements IndicatorAdapter {

    private BannerHolder<T> mHolder;

    public BannerAdapter(BannerHolder<T> holder) {

        mHolder = holder;
    }

    @Override
    public int getCount() {

        return getIndicatorCount() > 1 ? Integer.MAX_VALUE : getIndicatorCount();
    }

    @Override
    protected View getItem(ViewGroup container, int position) {

        final int realPosition = position % getIndicatorCount();

        View v = mHolder.createView(container.getContext());
        mHolder.invalidate(realPosition, getItem(realPosition));

        return v;
    }

    @Override
    public int getIndicatorCount() {

        return super.getCount();
    }
}