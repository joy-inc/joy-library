package com.android.library.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

import com.android.library.utils.TextUtil;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by KEVIN.DAI on 16/1/4.
 */
public class FrescoImageView extends SimpleDraweeView {

    public FrescoImageView(Context context) {

        super(context);
    }

    public FrescoImageView(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    /**
     * Displays an image given by the uri.
     *
     * @param uri uri of the image
     * @undeprecate
     */
    public void setImageURI(String uri) {

        if (TextUtil.isEmpty(uri))
            return;

        setImageURI(Uri.parse(uri));
    }
}