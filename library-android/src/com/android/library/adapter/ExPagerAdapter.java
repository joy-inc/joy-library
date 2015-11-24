package com.android.library.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class ExPagerAdapter<T> extends PagerAdapter {

    private List<T> mData;
    private OnItemViewClickListener mOnItemViewClickLisn;

    public ExPagerAdapter() {
    }

    public ExPagerAdapter(List<T> data) {

        mData = data;
    }

    public boolean isEmpty() {

        return getCount() == 0;
    }

    @Override
    public int getCount() {

        return mData == null ? 0 : mData.size();
    }

    public T getItem(int position) {

        if (mData == null)
            return null;

        T item = null;
        try {
            item = mData.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return item;
    }

    public void setData(List<T> data) {

        this.mData = data;
    }

    public List<T> getData() {

        return mData;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = getItem(container, position);
        container.addView(view);
        return view;
    }

    protected abstract View getItem(ViewGroup container, int position);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    public void setOnItemViewClickListener(OnItemViewClickListener lisn) {

        mOnItemViewClickLisn = lisn;
    }

    protected void callbackItemViewClick(int position, View view) {

        if (mOnItemViewClickLisn != null)
            mOnItemViewClickLisn.onItemViewClick(position, view, getItem(position));
    }
}