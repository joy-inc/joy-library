package com.android.library.view.banner;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView.ScaleType;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by KEVIN.DAI on 15/12/17.
 */
public class BannerImage<T> implements BannerHolder<T> {

    private SimpleDraweeView sdvCover;

    protected SimpleDraweeView onCreateView(Context context) {

        SimpleDraweeView sdvCover = new SimpleDraweeView(context);
        sdvCover.setScaleType(ScaleType.CENTER_CROP);
        return sdvCover;
    }

    @Override
    public final View createView(Context context) {

        sdvCover = onCreateView(context);
        return sdvCover;
    }

    @Override
    public final void invalidate(int position, T t) {

        if (t instanceof Integer) {

            sdvCover.setImageResource((Integer) t);
        } else if (t instanceof String) {

            sdvCover.setImageURI(Uri.parse((String) t));
        } else {

            sdvCover.setImageURI(Uri.parse(t.toString()));
        }
    }
}