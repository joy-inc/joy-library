package com.android.library.adapter;

import android.view.View;

/**
 * Created by KEVIN.DAI on 15/7/16.
 *
 * @deprecated see {@link ExRvViewHolder}
 */
public interface ExViewHolder {

    int getConvertViewRid();

    void initConvertView(View convertView);

    void invalidateConvertView(int position);
}
