package com.android.library.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.library.R;
import com.android.library.adapter.ExAdapter;
import com.android.library.httptask.CacheMode;
import com.android.library.httptask.ObjectRequest;
import com.android.library.listener.OnLoadMoreListener;
import com.android.library.utils.CollectionUtil;
import com.android.library.widget.JListView;
import com.android.library.widget.JLoadingView;

import java.util.List;

/**
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseHttpLvActivity<T> extends BaseHttpUiActivity<T> {

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private ListView mListView;
    private int mPageLimit = 20;
    private static final int PAGE_START_INDEX = 1;// 默认从第一页开始
    private int mPageIndex = PAGE_START_INDEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mListView = getDefaultListView();
        setContentView(wrapSwipeRefresh(mListView));
    }

    /**
     * 子类可以复写此方法，为自己定制ListView
     *
     * @return
     */
    protected JListView getDefaultListView() {

        JListView jlv = (JListView) inflateLayout(R.layout.lib_view_listview);
        jlv.setLoadMoreView(JLoadingView.getLoadMore(this), JLoadingView.getLoadMoreLp());
        jlv.setLoadMoreListener(getDefaultLoadMoreLisn());
        return jlv;
    }

    private View wrapSwipeRefresh(View contentView) {

        mSwipeRefreshWidget = new SwipeRefreshLayout(this);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefreshWidget.setOnRefreshListener(getDefaultRefreshLisn());
        mSwipeRefreshWidget.addView(contentView);
        return mSwipeRefreshWidget;
    }

    private OnRefreshListener getDefaultRefreshLisn() {

        return new OnRefreshListener() {

            @Override
            public void onRefresh() {

                if (isNetworkDisable()) {

                    hideSwipeRefresh();
                    showToast(R.string.toast_common_no_network);
                } else {

                    mPageIndex = PAGE_START_INDEX;
                    startRefresh();
                }
            }
        };
    }

    private OnLoadMoreListener getDefaultLoadMoreLisn() {

        return new OnLoadMoreListener() {

            @Override
            public void onRefresh(boolean isAuto) {

                if (isNetworkDisable()) {

                    onLoadMoreFailed();
                    if (!isAuto)
                        showToast(R.string.toast_common_no_network);
                } else {

                    startRefresh();
                }
            }
        };
    }

    private void startRefresh() {

        executeRefreshOnly();
    }

    @Override
    protected final ObjectRequest<T> getObjectRequest() {

        return getObjectRequest(mPageIndex, mPageLimit);
    }

    protected abstract ObjectRequest<T> getObjectRequest(int pageIndex, int pageLimit);

    /**
     * 设置分页大小
     *
     * @param pageLimit
     */
    protected void setPageLimit(int pageLimit) {

        mPageLimit = pageLimit;
    }

    protected ListView getListView() {

        return mListView;
    }

    protected void addHeaderView(View v) {

        mListView.addHeaderView(v);
    }

    protected void addFooterView(View v) {

        mListView.addFooterView(v);
    }

    protected void setOnItemClickListener(OnItemClickListener listener) {

        mListView.setOnItemClickListener(listener);
    }

    protected void setOnItemLongClickListener(OnItemLongClickListener listener) {

        mListView.setOnItemLongClickListener(listener);
    }

    protected void setAdapter(ExAdapter adapter) {

        mListView.setAdapter(adapter);
    }

    protected ExAdapter getAdapter() {

        ListAdapter adapter = mListView.getAdapter();
        if (adapter instanceof HeaderViewListAdapter)
            return (ExAdapter) ((HeaderViewListAdapter) adapter).getWrappedAdapter();
        else
            return (ExAdapter) adapter;
    }

    @Override
    protected boolean invalidateContent(T t) {

        List<?> datas = getListInvalidateContent(t);
        if (CollectionUtil.isEmpty(datas))
            return false;

        if (mListView instanceof JListView && isLoadMoreEnable()) {

            JListView jlv = (JListView) mListView;
            jlv.setLoadMoreEnable(datas.size() >= mPageLimit);
            if (jlv.isLoadMoreEnable())
                jlv.stopLoadMore();
        }

        ExAdapter adapter = getAdapter();
        if (adapter != null) {

            if (mPageIndex == PAGE_START_INDEX)
                adapter.setData(datas);
            else
                adapter.addAll(datas);

            adapter.notifyDataSetChanged();

            if (!isRespIntermediate())
                mPageIndex++;
        }
        return true;
    }

    protected List<?> getListInvalidateContent(T t) {

        return (List<?>) t;
    }

    @Override
    protected void onHttpFailed(Object tag, String msg) {

        if (isRespIntermediate())
            mPageIndex++;

        onLoadMoreFailed();
    }

    @Override
    protected void showLoading() {

        if (getReqCacheMode() == CacheMode.CACHE_AND_REFRESH && isReqHasCache())
            showSwipeRefresh();
        else if (!isSwipeRefreshing() && !isLoadingMore())
            super.showLoading();
    }

    @Override
    protected void hideLoading() {

        if (isSwipeRefreshing())
            hideSwipeRefresh();
        else if (!isLoadingMore())
            super.hideLoading();
    }

    @Override
    protected void showFailedTip() {

        if (!isSwipeRefreshing() && !isLoadingMore() && getAdapter().isEmpty())
            super.showFailedTip();
    }

    protected void setSwipeRefreshEnable(boolean enable) {

        mSwipeRefreshWidget.setEnabled(enable);
    }

    protected void setColorSchemeResources(int... colorResIds) {

        mSwipeRefreshWidget.setColorSchemeResources(colorResIds);
    }

    protected void setOnRefreshListener(OnRefreshListener lisn) {

        mSwipeRefreshWidget.setOnRefreshListener(lisn);
    }

    protected boolean isSwipeRefreshing() {

        return mSwipeRefreshWidget.isRefreshing();
    }

    protected void showSwipeRefresh() {

        if (isSwipeRefreshing())
            return;

        mSwipeRefreshWidget.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshWidget.setRefreshing(true);
            }
        });
    }

    protected void hideSwipeRefresh() {

        if (!isSwipeRefreshing())
            return;

        mSwipeRefreshWidget.setRefreshing(false);
    }

    protected final boolean isLoadingMore() {

        if (mListView instanceof JListView)
            return isLoadMoreEnable() && ((JListView) mListView).isLoadingMore();
        return false;
    }

    protected final void onLoadMoreFailed() {

        if (mListView instanceof JListView && isLoadMoreEnable())
            ((JListView) mListView).stopLoadMoreFailed();
    }

    protected void setLoadMoreEnable(boolean enable) {

        if (mListView instanceof JListView)
            ((JListView) mListView).setLoadMoreEnable(enable);
    }

    private boolean isLoadMoreEnable() {

        return ((JListView) mListView).isLoadMoreEnable();
    }
}