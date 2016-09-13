package com.android.library.ui.share;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.library.R;
import com.android.library.adapter.ExRvAdapter;
import com.android.library.adapter.ExRvViewHolder;

/**
 * Created by Daisw on 16/9/7.
 */

public class ShareAdapter extends ExRvAdapter<ShareAdapter.ViewHolder, ShareBean> {

    class ViewHolder extends ExRvViewHolder<ShareBean> {

        ImageView mIvIcon;
        TextView mTvName;

        public ViewHolder(View v) {

            super(v);
            mIvIcon = (ImageView) v.findViewById(R.id.ivIcon);
            mTvName = (TextView) v.findViewById(R.id.tvName);
            v.setOnClickListener((v1) -> callbackOnItemClickListener(getLayoutPosition(), v1));
        }

        @Override
        public void invalidateItemView(int position, ShareBean shareBean) {

            mIvIcon.setImageResource(shareBean.mIconResId);
            mTvName.setText(shareBean.mName);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(inflate(parent, R.layout.lib_item_share));
    }
}
