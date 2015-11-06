package com.joy.library.adapter.frame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class ExAdapter<T> extends BaseAdapter {

    private List<T> mData;
    private OnItemViewClickListener mOnItemViewClickLisn;
    private OnItemViewLongClickListener mOnItemViewLongClickLisn;

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

        if (mData == null) {
            return null;
        }

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

        ExViewHolder viewHolder = null;
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
        if (mData != null && item != null) {
            mData.add(position, item);
        }
    }

    public void add(T item) {

        if (mData != null && item != null) {
            mData.add(item);
        }
    }

    public void addAll(List<T> data) {
        if (data == null) {
            return;
        }

        if (mData == null) {
            mData = data;
        } else {
            mData.addAll(data);
        }
    }

    public void addAll(int position, List<T> item) {

        if (mData != null && item != null) {
            mData.addAll(position, item);
        }
    }

    public List<T> getData() {

        return mData;
    }

    public void remove(T item) {

        if (mData != null) {
            mData.remove(item);
        }
    }

    public void remove(int position) {

        if (mData != null) {
            mData.remove(position);
        }
    }

    public void removeAll() {

        if (mData != null) {
            mData.clear();
        }
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

    public void setOnItemViewClickListener(OnItemViewClickListener lisn) {

        mOnItemViewClickLisn = lisn;
    }

    public void setOnItemViewLongClickListener(OnItemViewLongClickListener lisn) {

        mOnItemViewLongClickLisn = lisn;
    }

    protected void callbackOnItemViewClickListener(int position, View view) {

        if (mOnItemViewClickLisn != null)
            mOnItemViewClickLisn.onItemViewClick(position, view);
    }

    protected void callbackOnItemViewLongClickListener(int position, View view) {

        if (mOnItemViewLongClickLisn != null)
            mOnItemViewLongClickLisn.onItemViewLongClick(position, view);
    }
}
