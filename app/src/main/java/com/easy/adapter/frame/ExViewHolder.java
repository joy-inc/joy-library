package com.easy.adapter.frame;

import android.view.View;

public interface ExViewHolder {

    int getConvertViewRid();

    void initConvertView(View convertView);

    void invalidateConvertView(int position);
}
