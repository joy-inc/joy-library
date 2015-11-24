package com.android.library.adapter;

import android.view.View;

public interface OnItemViewLongClickListener<T> {

    void onItemViewLongClick(int position, View clickView, T t);
}
