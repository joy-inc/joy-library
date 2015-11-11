package com.joy.library.adapter.frame;

import android.view.View;

public interface OnItemViewLongClickListener<T> {

    void onItemViewLongClick(int position, View clickView, T t);
}
