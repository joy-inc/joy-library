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

    private View mRootView, mRetryView, mLoadingDiv;
    private RetryLoadClickListener mRetryClickLisn;

    public JFooter(Context context) {

        super(context);
        init(context);
    }

    public JFooter(Context context, AttributeSet attrs) {

        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        setOrientation(VERTICAL);

        mRootView = LayoutInflater.from(context).inflate(R.layout.lib_view_footer, null);
        addView(mRootView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mRetryView = findViewById(R.id.jtvReload);
        mLoadingDiv = findViewById(R.id.llLoadingDiv);

        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isFailed() && mRetryClickLisn != null)
                    mRetryClickLisn.onRetry();
            }
        });
    }

    public void loading() {

        ViewUtil.hideView(mRetryView);
        ViewUtil.showView(mLoadingDiv);
    }

    public void failed() {

        ViewUtil.hideView(mLoadingDiv);
        ViewUtil.showView(mRetryView);
    }

    public boolean isFailed() {

        return mRetryView.getVisibility() == VISIBLE;
    }

    public void setLoadingView(View v, FrameLayout.LayoutParams flLp) {

        FrameLayout flDiv = (FrameLayout) findViewById(R.id.flProgressBarDiv);
        if (flDiv.getChildCount() > 0)
            flDiv.removeAllViews();

        flDiv.addView(v, flLp);
    }

    public void done() {

        LayoutParams lp = (LayoutParams) mRootView.getLayoutParams();
        lp.height = 0;
        mRootView.setLayoutParams(lp);
    }

    public void ready() {

        LayoutParams lp = (LayoutParams) mRootView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mRootView.setLayoutParams(lp);
    }

    public interface RetryLoadClickListener {

        void onRetry();
    }

    public void setRetryLoadClickListener(RetryLoadClickListener lisn) {

        mRetryClickLisn = lisn;
    }
}