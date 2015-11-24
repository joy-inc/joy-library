package com.android.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

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
        mFooterView.setRetryLoadClickListener(new JFooter.RetryLoadClickListener() {

            @Override
            public void onRetry() {

                startLoadMore(false);
            }
        });
    }

    private void startLoadMore(boolean isAuto) {

        if (mIsLoadingMore)
            return;

        if (mOnLoadMoreListener != null) {

            mIsLoadingMore = true;
            mFooterView.ready();
            mFooterView.setLoadingState();
            if (isAuto) {

                mOnLoadMoreListener.onAuto();
            } else {

                mOnLoadMoreListener.onManual();
            }
        }
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

        if (!mIsLoadMoreEnable || mIsLoadingMore || totalItemCount == 0)
            return;

        if (visibleItemCount + firstVisibleItem == totalItemCount)
            startLoadMore(true);
    }

    public interface OnLoadMoreListener {

        void onAuto();

        void onManual();
    }

    public void setLoadMoreListener(OnLoadMoreListener l) {

        mOnLoadMoreListener = l;
    }

    public boolean isLoadingMore() {

        return mIsLoadingMore;
    }

    public boolean isLoadMoreEnable() {

        return mIsLoadMoreEnable;
    }

    public void setLoadMoreEnable(boolean enable) {

        if (mIsLoadMoreEnable == enable)
            return;

        mIsLoadMoreEnable = enable;
        mIsLoadingMore = false;

        if (enable) {

            mFooterView.ready();
        } else {

            mFooterView.done();
            mFooterView.setOnClickListener(null);
        }
    }

    public void setLoadMoreView(View v, FrameLayout.LayoutParams flLp) {

        mFooterView.setLoadingView(v, flLp);
    }

    public void stopLoadMore() {

        if (mIsLoadingMore) {

            mIsLoadingMore = false;
            mFooterView.done();
        }
    }

    public void stopLoadMoreFailed() {

        if (mIsLoadingMore) {

            mIsLoadingMore = false;
            mFooterView.setFailedState();
        }
    }
}