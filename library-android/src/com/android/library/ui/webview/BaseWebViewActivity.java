package com.android.library.ui.webview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.library.BaseApplication;
import com.android.library.R;
import com.android.library.ui.activity.BaseHttpUiActivity2;
import com.android.library.ui.share.ShareUtil;
import com.android.library.utils.TextUtil;

import javax.inject.Inject;

/**
 * Created by Daisw on 16/8/14.
 */

public class BaseWebViewActivity extends BaseHttpUiActivity2 {

    private static final String KEY_URL = "KEY_URL";
    private static final String KEY_TITLE = "KEY_TITLE";
    private String mUrl;
    private String mTitle;

    private TextView mTvTitle;

    @Inject
    BaseWebViewPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.base_theme);
        super.onCreate(savedInstanceState);
        component().inject(this);
        setContentView(mPresenter.mWebView);
        mPresenter.load(mUrl);
    }

    private BaseWebViewComponent component() {

        return DaggerBaseWebViewComponent.builder()
                .appComponent(((BaseApplication) getApplication()).component())
                .baseWebViewModule(new BaseWebViewModule(this))
                .build();
    }

    @Override
    protected void initData() {

        mUrl = getIntent().getStringExtra(KEY_URL);
        mTitle = getIntent().getStringExtra(KEY_TITLE);
    }

    @Override
    protected void initTitleView() {

        ShareUtil joyShare = new ShareUtil(this);

        addTitleLeftBackView();
        addTitleRightView(R.drawable.ic_more_vert_white_24dp, (v) -> joyShare.show());
        if (TextUtil.isNotEmpty(mTitle))
            mTvTitle = addTitleMiddleView(mTitle);
    }

    @Override
    protected void doOnRetry() {

        mPresenter.reload();
    }

    public static void startActivity(Context context, String url) {

        startActivity(context, url, null);
    }

    public static void startActivity(Context context, String url, String title) {

        Intent intent = new Intent(context, BaseWebViewActivity.class);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(KEY_TITLE, title);
        context.startActivity(intent);
    }
}
