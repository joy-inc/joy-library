package com.android.library.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.library.listener.OnItemClickListener;
import com.android.library.listener.OnItemLongClickListener;

import java.util.List;

/**
 * Created by KEVIN.DAI on 15/7/16.
 *
 * @param <T>
 * @deprecated see {@link ExRvAdapter}
 */
public abstract class ExAdapter<T> extends BaseAdapter {

    private List<T> mData;
    private OnItemClickListener<T> mOnItemClickListener;
    private OnItemLongClickListener<T> mOnItemLongClickListener;

    protected ExAdapter() {

    }

    protected ExAdapter(List<T> data) {

        mData = data;
    }

    @Override
    public int getCount() {

        return mData == null ? 0 : mData.size();
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ExViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = getViewHolder(position);
            convertView = LayoutInflater.from(parent.getContext()).inflate(viewHolder.getConvertViewRid(), null);
            viewHolder.initConvertView(convertView);
            convertView.setTag(viewHolder);
        } else {

            viewHolder = (ExViewHolder) convertView.getTag();
        }

        viewHolder.invalidateConvertView(position);
        return convertView;
    }

    protected abstract ExViewHolder getViewHolder(int position);

    public boolean isEmpty() {

        return getCount() == 0;
    }

    public void setData(List<T> data) {

        mData = data;
    }

    public void add(int position, T item) {

        if (mData != null && item != null)
            mData.add(position, item);
    }

    public void add(T item) {

        if (mData != null && item != null)
            mData.add(item);
    }

    public void addAll(List<T> data) {

        if (data == null)
            return;

        if (mData == null) {

            mData = data;
        } else {

            mData.addAll(data);
        }
    }

    public void addAll(int position, List<T> item) {

        if (mData != null && item != null)
            mData.addAll(position, item);
    }

    public List<T> getData() {

        return mData;
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

        return position >= 0 && position < getCount();
    }

	/*
     * click listener part
	 */

    public void setOnItemClickListener(OnItemClickListener<T> lisn) {

        mOnItemClickListener = lisn;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> lisn) {

        mOnItemLongClickListener = lisn;
    }

    protected void callbackOnItemClickListener(int position, View view) {

        if (mOnItemClickListener != null)
            mOnItemClickListener.onItemClick(position, view, getItem(position));
    }

    protected void callbackOnItemLongClickListener(int position, View view) {

        if (mOnItemLongClickListener != null)
            mOnItemLongClickListener.onItemLongClick(position, view, getItem(position));
    }
}