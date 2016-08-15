package com.android.library.listener;

import android.view.View;

public interface OnItemLongClickListener<T> {

    void onItemLongClick(int position, View v, T t);
}
