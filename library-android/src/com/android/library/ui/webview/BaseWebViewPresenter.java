package com.android.library.ui.webview;

import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.library.ui.activity.interfaces.BaseViewNet;
import com.android.library.utils.LogMgr;
//import com.joy.router.Router;
//import com.joy.router.RxBus;

import javax.inject.Inject;

/**
 * Created by Daisw on 16/8/14.
 */

public class BaseWebViewPresenter {

    @Inject
    WebView mWebView;

    @Inject
    BaseViewNet mBaseView;

    boolean isError;

    @Inject
    public BaseWebViewPresenter() {

    }

    @Inject
    void setWebViewClient() {

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                isError = false;
                LogMgr.d("daisw", "onPageStarted");
                mBaseView.hideContent();
                mBaseView.hideTipView();
                mBaseView.showLoading();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

                isError = true;
                LogMgr.d("daisw", "onReceivedError");
                mBaseView.hideLoading();
                mBaseView.hideContent();
                mBaseView.showErrorTip();
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if (!isError) {
                    LogMgr.d("daisw", "onPageFinished " + url);
                    mBaseView.hideLoading();
                    mBaseView.hideTipView();
                    mBaseView.showContent();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                LogMgr.d("daisw", "shouldOverrideUrlLoading 1");
                return super.shouldOverrideUrlLoading(view, request);
//                return Router.dispatch(view.getContext(), view.getUrl());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                LogMgr.d("daisw", "shouldOverrideUrlLoading 2");
                return super.shouldOverrideUrlLoading(view, url);
//                return Router.dispatch(view.getContext(), url);
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {

                LogMgr.d("daisw", "onReceivedTitle: " + title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

//                LogMgr.d("daisw", "onProgressChanged: " + newProgress);
            }
        });
    }

    @Inject
    void xxx() {

//        RxBus.get().toObservable(Router.Parcel.class)
//                .compose(mBaseView.bindToLifecycle())
//                .subscribe(parcel -> {
//
//                    LogMgr.i("daisw", "parcel: " + parcel);
//
////                    Intent intent = new Intent(parcel.mContext, xxxActivity.class);
////                    parcel.mContext.startActivity(intent);
//                });
    }

    public void load(String url) {

        mWebView.loadUrl(url);
    }

    public void reload() {

        mWebView.reload();
    }

    public void goBack() {

        mWebView.goBack();
    }

    public void goForward() {

        mWebView.goForward();
    }
}
