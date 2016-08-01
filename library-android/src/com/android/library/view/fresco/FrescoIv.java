package com.android.library.view.fresco;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by KEVIN.DAI on 16/1/4.
 */
public class FrescoIv extends SimpleDraweeView {

    public FrescoIv(Context context) {

        super(context);
    }

    public FrescoIv(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    /**
     * Displays an image given by the uri.
     *
     * @param url url of the image
     * @undeprecate
     */
    public void setImageURI(@Nullable String url) {

        setImageURI(Uri.parse(url == null ? "" : url));
    }

    /**
     * Displays an image given by the res id.
     *
     * @param drawableResId drawableResId of the image
     * @undeprecate
     */
    public void setImageURI(@DrawableRes int drawableResId) {

        setImageURI("res://" + getContext().getPackageName() + "/" + drawableResId);
    }

    public void resize(@Nullable String url, int width, int height) {

        resize(Uri.parse(url == null ? "" : url), width, height);
    }

    public void resize(@NonNull Uri uri, int width, int height) {

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(getController())
                .setImageRequest(request)
                .build();
        setController(controller);
    }

    public void blur(@Nullable String url, @IntRange(from = 0, to = 25) int radius) {

        blur(Uri.parse(url == null ? "" : url), radius);
    }

    public void blur(@NonNull Uri uri, @IntRange(from = 0, to = 25) int radius) {

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setPostprocessor(new BlurPostprocessor(getContext(), radius))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(getController())
                .setImageRequest(request)
                .build();
        setController(controller);
    }

    public void resizeBlur(@Nullable String url, int width, int height, @IntRange(from = 0, to = 25) int radius) {

        resizeBlur(Uri.parse(url == null ? "" : url), width, height, radius);
    }

    public void resizeBlur(@NonNull Uri uri, int width, int height, @IntRange(from = 0, to = 25) int radius) {

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .setPostprocessor(new BlurPostprocessor(getContext(), radius))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(getController())
                .setImageRequest(request)
                .build();
        setController(controller);
    }
}
