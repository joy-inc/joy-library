package com.android.library.listener;

import android.view.View;

public interface OnItemClickListener<T> {

    void onItemClick(int position, View v, T t);
}
