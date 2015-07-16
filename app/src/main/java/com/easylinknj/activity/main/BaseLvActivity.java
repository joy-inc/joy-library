package com.easylinknj.activity.main;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.easylinknj.R;

/**
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseLvActivity<T> extends BaseActivity<T> {

    private SwipeRefreshLayout mSwipeRefreshWidget;

    @Override
    protected View wrapContentView(View contentView) {

        return super.wrapContentView(wrapSwipeRefresh(contentView));
    }

    private View wrapSwipeRefresh(View contentView) {

        // swipe refresh widget
        mSwipeRefreshWidget = new SwipeRefreshLayout(this);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {// default listener

            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        stopSwipeRefresh();
                    }
                }, 5000);
            }
        });
        mSwipeRefreshWidget.addView(contentView);
        return mSwipeRefreshWidget;
    }

    protected void setColorSchemeResources(int... colorResIds) {

        mSwipeRefreshWidget.setColorSchemeResources(colorResIds);
    }

    protected void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener lisn) {

        mSwipeRefreshWidget.setOnRefreshListener(lisn);
    }

    protected boolean isSwipeRefreshing() {

        return mSwipeRefreshWidget.isRefreshing();
    }

    protected void startSwipeRefresh() {

        if (isSwipeRefreshing())
            return;

        mSwipeRefreshWidget.setRefreshing(true);
    }

    protected void stopSwipeRefresh() {

        if (!isSwipeRefreshing())
            return;

        mSwipeRefreshWidget.setRefreshing(false);
    }
}
