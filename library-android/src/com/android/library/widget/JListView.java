package com.android.library.widget;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.library.listener.OnLoadMoreListener;

/**
 * Created by KEVIN.DAI on 15/11/20.
 */
public class JListView extends ListView implements OnScrollListener {

    private OnLoadMoreListener mOnLoadMoreListener;
    private JFooter mFooterView;
    private boolean mIsLoadMoreEnable = true;
    private boolean mIsLoadingMore;
    private boolean mIsFooterAdded;

    public JListView(Context context) {

        super(context);
        init(context);
    }

    public JListView(Context context, AttributeSet attrs) {

        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        setOnScrollListener(this);

        mFooterView = new JFooter(context);
        mFooterView.setOnRetryListener(new JFooter.OnRetryListener() {

            @Override
            public void onRetry() {

                startLoadMore(false);
            }
        });
    }

    @Override
    public void setAdapter(ListAdapter adapter) {

        if (!mIsFooterAdded) {

            addFooterView(mFooterView);
            mIsFooterAdded = true;
        }
        super.setAdapter(adapter);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        int extraItemsCount = getHeaderViewsCount() + getFooterViewsCount();

        if (!mIsLoadMoreEnable || mIsLoadingMore || isLoadMoreFailed() || totalItemCount <= extraItemsCount)
            return;

        if (visibleItemCount + firstVisibleItem == totalItemCount) {

            startLoadMore(true);
        }
    }

    private void startLoadMore(boolean isAuto) {

        if (mIsLoadingMore)
            return;

        mIsLoadingMore = true;

//        mFooterView.ready();
        mFooterView.loading();

        if (mOnLoadMoreListener != null)
            mOnLoadMoreListener.onRefresh(isAuto);
    }

    public void stopLoadMore() {

        if (mIsLoadingMore) {

            mIsLoadingMore = false;
//            mFooterView.done();
        }
    }

    public void stopLoadMoreFailed() {

        if (mIsLoadingMore) {

            mIsLoadingMore = false;
            mFooterView.failed();
        }
    }

    public boolean isLoadingMore() {

        return mIsLoadingMore;
    }

    public boolean isLoadMoreEnable() {

        return mIsLoadMoreEnable;
    }

    public boolean isLoadMoreFailed() {

        return mFooterView != null && mFooterView.isFailed();
    }

    public void setLoadMoreEnable(boolean enable) {

        if (mIsLoadMoreEnable == enable)
            return;

        mIsLoadMoreEnable = enable;

        if (enable) {

            mFooterView.ready();
        } else {

            mFooterView.done();
        }
    }

    public void setLoadMoreView(View v, FrameLayout.LayoutParams flLp) {

        mFooterView.setLoadingView(v, flLp);
    }

    public void setLoadMoreDarkTheme() {

        mFooterView.setDarkTheme();
    }

    public void setLoadMoreLightTheme() {

        mFooterView.setLightTheme();
    }

    public void setLoadMoreHintTextColor(@ColorRes int resId) {

        mFooterView.setHintTextColor(resId);
    }

    public void setLoadMoreListener(OnLoadMoreListener l) {

        mOnLoadMoreListener = l;
    }
}