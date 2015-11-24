package com.android.library.adapter;

import android.view.View;

public interface OnItemViewClickListener<T> {

    void onItemViewClick(int position, View clickView, T t);
}
