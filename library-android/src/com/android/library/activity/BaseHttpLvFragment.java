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
import com.android.library.utils.LogMgr;
import com.android.library.widget.JListView;
import com.android.library.widget.JLoadingView;

import java.util.List;

/**
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseHttpLvFragment<T> extends BaseHttpUiFragment<T> {

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private ListView mListView;
    private int mPageLimit = 20;
    private static final int PAGE_START_INDEX = 1;// 默认从第一页开始
    private int mPageIndex = PAGE_START_INDEX;
    private int mSortIndex = mPageIndex;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
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
        jlv.setLoadMoreView(JLoadingView.getLoadMore(getActivity()), JLoadingView.getLoadMoreLp());
        jlv.setLoadMoreListener(getDefaultLoadMoreLisn());
        return jlv;
    }

    private View wrapSwipeRefresh(View contentView) {

        mSwipeRefreshWidget = new SwipeRefreshLayout(getActivity());
        mSwipeRefreshWidget.setColorSchemeResources(R.color.color_accent);
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

                    mSortIndex = mPageIndex;
                    mPageIndex = PAGE_START_INDEX;
                    stopLoadMore();
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

                    setLoadMoreFailed();
                    if (!isAuto)
                        showToast(R.string.toast_common_no_network);
                } else {

                    if (mPageIndex == PAGE_START_INDEX) {

                        if (getAdapter().getCount() == mPageLimit)
                            mPageIndex++;
                        else
                            mPageIndex = mSortIndex;
                    }
                    hideSwipeRefresh();
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
     * show swipe refresh view {@link SwipeRefreshLayout}
     */
    protected void executeSwipeRefresh() {

        showSwipeRefresh();
        mPageIndex = PAGE_START_INDEX;
        onRetryCallback();
    }

    /**
     * show frame refresh view {@link JLoadingView}
     */
    protected void executeFrameRefresh() {

        mPageIndex = PAGE_START_INDEX;
        onRetryCallback();
    }

    /**
     * 设置分页大小
     *
     * @param pageLimit
     */
    protected void setPageLimit(int pageLimit) {

        mPageLimit = pageLimit;
    }

    /**
     * 设置页码
     *
     * @param index
     */
    protected void setPageIndex(int index) {

        mPageIndex = index;
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

        setLoadMoreEnable(datas.size() >= mPageLimit);
        stopLoadMore();

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
    final void onHttpFailed(String msg) {

        if (LogMgr.isDebug())
            showToast(getClass().getSimpleName() + ": " + msg);

        if (isSwipeRefreshing()) {// 下拉刷新触发

        } else if (isLoadingMore()) {// 加载更多触发

            setLoadMoreFailed();
        } else {// 首次加载触发

        }
    }

    @Override
    protected final void showLoading() {

        if (getReqCacheMode() == CacheMode.CACHE_AND_REFRESH && isReqHasCache())
            postSwipeRefresh();
        else if (!isSwipeRefreshing() && !isLoadingMore())
            super.showLoading();
    }

    @Override
    protected final void hideLoading() {

        if (isSwipeRefreshing())
            hideSwipeRefresh();
        else
            super.hideLoading();
    }

    @Override
    protected final void showFailedTip() {

        if (getItemCount() - 1 == 0)
            super.showFailedTip();
    }

    @Override
    protected final void showNoContentTip() {

        if (getItemCount() - 1 == 0)
            super.showNoContentTip();
    }

    @Override
    protected final void hideContentView() {

        if (getItemCount() - 1 == 0)
            super.hideContentView();
    }

    private int getItemCount() {

        return mListView.getHeaderViewsCount() + mListView.getFooterViewsCount() + getAdapter().getCount();
    }


    // swipe refresh
    // =============================================================================================
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

    protected void postSwipeRefresh() {

        mSwipeRefreshWidget.post(new Runnable() {

            @Override
            public void run() {

                showSwipeRefresh();
            }
        });
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
    // =============================================================================================


    // load more
    // =============================================================================================
    protected final boolean isLoadingMore() {

        if (!(mListView instanceof JListView))
            return false;
        if (!isLoadMoreEnable())
            return false;

        return ((JListView) mListView).isLoadingMore();
    }

    protected final void stopLoadMore() {

        if (!(mListView instanceof JListView))
            return;
        if (!isLoadMoreEnable())
            return;

        ((JListView) mListView).stopLoadMore();
    }

    protected final void setLoadMoreFailed() {

        if (!(mListView instanceof JListView))
            return;
        if (!isLoadMoreEnable())
            return;

        ((JListView) mListView).stopLoadMoreFailed();
    }

    protected final boolean isLoadMoreFailed() {

        if (!(mListView instanceof JListView))
            return false;

        return ((JListView) mListView).isLoadMoreFailed();
    }

    protected final void setLoadMoreEnable(boolean enable) {

        if (!(mListView instanceof JListView))
            return;

        ((JListView) mListView).setLoadMoreEnable(enable);
    }

    protected final boolean isLoadMoreEnable() {

        if (!(mListView instanceof JListView))
            return false;

        return ((JListView) mListView).isLoadMoreEnable();
    }
    // =============================================================================================
}