package com.android.library.view.banner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView.ScaleType;

import com.android.library.widget.FrescoImageView;

/**
 * Created by KEVIN.DAI on 15/12/17.
 */
public class BannerImage<T> implements BannerHolder<T> {

    private FrescoImageView fivCover;

    protected FrescoImageView onCreateView(Context context) {

        FrescoImageView fivCover = new FrescoImageView(context);
        fivCover.setScaleType(ScaleType.CENTER_CROP);
        return fivCover;
    }

    @Override
    public final View createView(Context context) {

        fivCover = onCreateView(context);
        return fivCover;
    }

    @Override
    public final void invalidate(int position, T t) {

        if (t == null)
            return;

        if (t instanceof Integer) {

            fivCover.setImageResource((Integer) t);
        } else if (t instanceof String) {

            fivCover.setImageURI((String) t);
        } else {

            fivCover.setImageURI(t.toString());
        }
    }
}