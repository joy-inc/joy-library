package com.android.library.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by KEVIN.DAI on 15/11/10.
 */
public abstract class ExRvAdapter<K extends ExRvViewHolder, T> extends RecyclerView.Adapter<K> {

    private List<T> mData;
    private OnItemViewClickListener mOnItemViewClickLisn;
    private OnItemViewLongClickListener mOnItemViewLongClickLisn;

    protected ExRvAdapter() {

        this(null);
    }

    protected ExRvAdapter(List<T> data) {

        mData = data;
    }

    @Override
    public int getItemCount() {

        return mData == null ? 0 : mData.size();
    }

    public T getItem(int position) {

        if (mData == null)
            return null;

        T t = null;
        try {

            t = mData.get(position);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return t;
    }

    public View inflate(@NonNull ViewGroup parent, @LayoutRes int layoutResID) {

        return LayoutInflater.from(parent.getContext()).inflate(layoutResID, parent, false);
    }

    public boolean isEmpty() {

        return getItemCount() == 0;
    }

    public void setData(List<T> data) {

        mData = data;
    }

    public List<T> getData() {

        return mData;
    }

    public void add(int position, T t) {

        if (mData != null && t != null)
            mData.add(position, t);
    }

    public void add(T t) {

        if (mData != null && t != null)
            mData.add(t);
    }

    public void addAll(List<T> ts) {

        if (ts == null)
            return;

        if (mData == null) {

            mData = ts;
        } else {

            mData.addAll(ts);
        }
    }

    public void addAll(int position, List<T> ts) {

        if (mData != null && ts != null)
            mData.addAll(position, ts);
    }

    public void remove(T item) {

        if (mData != null)
            mData.remove(item);
    }

    public void remove(int position) {

        if (mData != null)
            mData.remove(position);
    }

    public void removeAll() {

        if (mData != null)
            mData.clear();
    }

    public void clear() {

        if (mData != null)
            mData.clear();
    }

    public boolean checkPosition(int position) {

        return position >= 0 && position < getItemCount();
    }

    /*
     * click listener part
	 */
    public void setOnItemViewClickListener(OnItemViewClickListener<T> lisn) {

        mOnItemViewClickLisn = lisn;
    }

    public void setOnItemViewLongClickListener(OnItemViewLongClickListener<T> lisn) {

        mOnItemViewLongClickLisn = lisn;
    }

    protected void callbackOnItemViewClickListener(int position, View view) {

        if (mOnItemViewClickLisn != null)
            mOnItemViewClickLisn.onItemViewClick(position, view, getItem(position));
    }

    protected void callbackOnItemViewLongClickListener(int position, View view) {

        if (mOnItemViewLongClickLisn != null)
            mOnItemViewLongClickLisn.onItemViewLongClick(position, view, getItem(position));
    }
}