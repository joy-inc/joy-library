package com.android.library.ui.webview;

import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.library.injection.ActivityScope;
import com.android.library.ui.activity.BaseHttpUiActivity2;
import com.android.library.ui.activity.interfaces.BaseViewNet;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Daisw on 16/8/16.
 */

@Module
public class BaseWebViewModule {

    private final BaseHttpUiActivity2 mActivity;

    public BaseWebViewModule(BaseHttpUiActivity2 activity) {

        mActivity = activity;
    }

    @Provides
    @ActivityScope
    BaseViewNet provideBaseViewNet() {

        return mActivity;
    }

    @Provides
    @ActivityScope
    WebView provideWebView() {

        WebView webView = new WebView(mActivity);
//        if (SDK_INT >= KITKAT)
//            webView.setLayerType(LAYER_TYPE_HARDWARE, null);
        WebSettings settings = webView.getSettings();
//        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);

        return webView;
    }
}
