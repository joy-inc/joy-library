package com.android.library.view.banner;

import android.content.Context;
import android.view.View;
import android.widget.ImageView.ScaleType;

import com.android.library.view.fresco.FrescoIv;

/**
 * Created by KEVIN.DAI on 15/12/17.
 */
public class BannerImage<T> implements BannerHolder<T> {

    private FrescoIv fivCover;

    protected FrescoIv onCreateView(Context context) {

        FrescoIv fivCover = new FrescoIv(context);
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