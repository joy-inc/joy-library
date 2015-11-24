package com.android.library.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.library.R;
import com.android.library.adapter.ExAdapter;
import com.android.library.utils.CollectionUtil;

import java.util.List;

/**
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseHttpLvFragment<T> extends BaseHttpUiFragment<T> {

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private ListView mListView;
    private int mPageLimit = 20;
    private static final int PAGE_START_INDEX = 1;// 默认从第一页开始
    private int mCurrentPageIndex = PAGE_START_INDEX;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        mListView = getDefaultListView();
        setContentView(wrapSwipeRefresh(mListView));
    }

    @Override
    public void onPause() {

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
    protected ListView getDefaultListView() {

        return (ListView) inflateLayout(R.layout.lib_view_listview);
    }

    private View wrapSwipeRefresh(View contentView) {

        // swipe refresh widget
        mSwipeRefreshWidget = new SwipeRefreshLayout(getActivity());
        mSwipeRefreshWidget.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefreshWidget.setOnRefreshListener(getDefaultRefreshLisn());
        mSwipeRefreshWidget.addView(contentView);
        return mSwipeRefreshWidget;
    }

    private OnRefreshListener getDefaultRefreshLisn() {

        return new OnRefreshListener() {

            @Override
            public void onRefresh() {

                onManualRefresh();
            }
        };
    }

    private void onManualRefresh() {

        if (isNetworkEnable()) {

            startManualRefresh();
        } else {

            hideSwipeRefresh();
            showToast(R.string.toast_common_no_network);
        }
    }

    private void startManualRefresh() {

        // TODO abort load more http task.

        mCurrentPageIndex = PAGE_START_INDEX;// 重置起始页码

        if (isNeedCache()) {

            executeRefreshAndCache();
        } else {

            executeRefresh();
        }
    }

    private void startLoadMoreRefresh() {

//        mSwipeRefreshWidget.stopSwipeRefresh();// 中断下拉刷新
//
//        HttpFrameParams hfp = getXListViewHttpParams(mCurrentPageIndex + 1, mPageLimit);
//        mLoadMoreHttpTask = new HttpTask(hfp.params);
//        mLoadMoreHttpTask.setListener(getLodMoreListener(hfp));
//        mLoadMoreHttpTask.execute();
    }

    protected ListView getListView() {

        return mListView;
    }

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

    protected int getCurrentPageIndex() {

        return mCurrentPageIndex;
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

    protected void setAdapter(ExAdapter adapter) {

        mListView.setAdapter(adapter);
    }

    protected ExAdapter getAdapter() {

        try {
            ListAdapter adapter = mListView.getAdapter();
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
        ExAdapter adapter = getAdapter();
        if (adapter != null) {

            adapter.setData(datas);
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    protected List<?> getListInvalidateContent(T t) {

        return (List<?>) t;
    }

    @Override
    protected void showLoading() {

        if (!isSwipeRefreshing())
            super.showLoading();
    }

    @Override
    protected void hideLoading() {

        if (isSwipeRefreshing())
            hideSwipeRefresh();
        else
            super.hideLoading();
    }

    @Override
    protected void showFailedTip() {

        if (!isSwipeRefreshing())
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
}