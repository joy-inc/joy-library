package com.android.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.library.R;
import com.android.library.utils.ViewUtil;

/**
 * Created by KEVIN.DAI on 15/11/20.
 */
public class JFooter extends LinearLayout {

    private View mRootView, mRetryView, mLoadingDiv, mProgressView;
    private RetryLoadClickListener mRetryClickLisn;

    public JFooter(Context context) {

        super(context);
        initView(context);
    }

    public JFooter(Context context, AttributeSet attrs) {

        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {

        setOrientation(VERTICAL);

        mRootView = LayoutInflater.from(context).inflate(R.layout.lib_view_footer, null);
        addView(mRootView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mLoadingDiv = findViewById(R.id.llLoadingDiv);
        mRetryView = findViewById(R.id.jtvReload);
        mRetryView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mRetryClickLisn != null)
                    mRetryClickLisn.onRetry();
            }
        });
    }

    public void setLoadingState() {

        ViewUtil.hideView(mRetryView);
        ViewUtil.showView(mProgressView);
        ViewUtil.showView(mLoadingDiv);
    }

    public void setFailedState() {

        ViewUtil.hideView(mProgressView);
        ViewUtil.hideView(mLoadingDiv);
        ViewUtil.showView(mRetryView);
    }

    public void setLoadingView(View v, FrameLayout.LayoutParams flLp) {

        FrameLayout flDiv = (FrameLayout) findViewById(R.id.flProgressBarDiv);
        if (flDiv.getChildCount() > 0)
            flDiv.removeAllViews();

        flDiv.addView(v, flLp);
        mProgressView = v;
    }

    public void done() {

        ViewUtil.hideView(mProgressView);
        LayoutParams lp = (LayoutParams) mRootView.getLayoutParams();
        lp.height = 0;
    }

    public void ready() {

        LayoutParams lp = (LayoutParams) mRootView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mRootView.setLayoutParams(lp);
    }

    public void setRetryLoadClickListener(RetryLoadClickListener lisn) {

        mRetryClickLisn = lisn;
    }

    public interface RetryLoadClickListener {

        void onRetry();
    }
}