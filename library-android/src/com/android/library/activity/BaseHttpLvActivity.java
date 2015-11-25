package com.android.library.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;

import com.android.library.R;
import com.android.library.adapter.ExAdapter;
import com.android.library.httptask.ObjectRequest;
import com.android.library.utils.CollectionUtil;
import com.android.library.widget.JListView;
import com.android.library.widget.JListView.OnLoadMoreListener;
import com.android.library.widget.JLoadingView;

import java.util.List;

/**
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseHttpLvActivity<T> extends BaseHttpUiActivity<T> {

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private JListView mJListView;
    private int mPageLimit = 20;
    private static final int PAGE_START_INDEX = 1;// 默认从第一页开始
    private int mPageIndex = PAGE_START_INDEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mJListView = getDefaultListView();
        setContentView(wrapSwipeRefresh(mJListView));
    }

    @Override
    protected void onPause() {

        super.onPause();
        if (isFinishing()) {

            // TODO abort all http task.
        }
    }

    /**
     * 子类可以复写此方法，为自己定制ListView
     *
     * @return
     */
    protected JListView getDefaultListView() {

        JListView jlv = (JListView) inflateLayout(R.layout.lib_view_listview);
        jlv.setLoadMoreEnable(false);
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

                    // TODO abort load more http task.

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

                    // TODO abort swipe refresh http task.

                    startRefresh();
                }
            }
        };
    }

    private void startRefresh() {

        if (isNeedCache()) {

            executeRefreshAndCache();
        } else {

            executeRefresh();
        }
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

    protected int getPageLimit() {

        return mPageLimit;
    }

    protected int getPageIndex() {

        return mPageIndex;
    }

    protected JListView getListView() {

        return mJListView;
    }

    protected void addHeaderView(View v) {

        mJListView.addHeaderView(v);
    }

    protected void addFooterView(View v) {

        mJListView.addFooterView(v);
    }

    protected void setOnItemClickListener(OnItemClickListener listener) {

        mJListView.setOnItemClickListener(listener);
    }

    protected void setAdapter(ExAdapter adapter) {

        mJListView.setAdapter(adapter);
    }

    protected ExAdapter getAdapter() {

        try {
            ListAdapter adapter = mJListView.getAdapter();
            if (adapter instanceof HeaderViewListAdapter)
                return (ExAdapter) ((HeaderViewListAdapter) adapter).getWrappedAdapter();
            else
                return (ExAdapter) adapter;
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected boolean invalidateContent(T t) {

        List<?> datas = getListInvalidateContent(t);
        if (CollectionUtil.isEmpty(datas))
            return false;

        mJListView.setLoadMoreEnable(mLoadMoreEnable && datas.size() >= mPageLimit);
        if (mJListView.isLoadMoreEnable())
            mJListView.stopLoadMore();

        ExAdapter adapter = getAdapter();
        if (adapter != null) {

            if (mPageIndex == PAGE_START_INDEX)
                adapter.setData(datas);
            else
                adapter.addAll(datas);
            adapter.notifyDataSetChanged();
            mPageIndex++;
        }
        return true;
    }

    protected List<?> getListInvalidateContent(T t) {

        return (List<?>) t;
    }

    @Override
    protected void onHttpFailed(Object tag, String msg) {

        onLoadMoreFailed();
    }

    @Override
    protected void showLoading() {

        if (!isSwipeRefreshing() && !isLoadingMore())
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

        if (!isSwipeRefreshing() && !isLoadingMore())
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

        mSwipeRefreshWidget.setRefreshing(true);
    }

    protected void hideSwipeRefresh() {

        if (!isSwipeRefreshing())
            return;

        mSwipeRefreshWidget.setRefreshing(false);
    }

    protected final boolean isLoadingMore() {

        return mJListView.isLoadingMore();
    }

    protected final void onLoadMoreFailed() {

        mJListView.stopLoadMoreFailed();
    }

    private boolean mLoadMoreEnable = true;

    protected void setLoadMoreEnable(boolean enable) {

        mLoadMoreEnable = enable;
    }
}