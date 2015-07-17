package com.easylinknj.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.easylinknj.R;
import com.easylinknj.adapter.frame.ExAdapter;
import com.easylinknj.adapter.frame.ExViewHolder;
import com.easylinknj.adapter.frame.ExViewHolderBase;
import com.easylinknj.bean.HotCityItem;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public class CityAdapter extends ExAdapter<HotCityItem> {

    @Override
    protected ExViewHolder getViewHolder(int position) {

        return new CityViewHolder();
    }

    private class CityViewHolder extends ExViewHolderBase {

        private SimpleDraweeView sdvPhoto;
        private TextView tvName;

        @Override
        public int getConvertViewRid() {

            return R.layout.item_listview;
        }

        @Override
        public void initConvertView(View convertView) {

            sdvPhoto = (SimpleDraweeView) convertView.findViewById(R.id.sdvPhoto);
            tvName = (TextView) convertView.findViewById(R.id.tvName);
        }

        @Override
        public void invalidateConvertView() {

            HotCityItem data = getItem(mPosition);
            sdvPhoto.setImageURI(Uri.parse(data.getPhoto()));
            tvName.setText(data.getCnname());
        }
    }
}
