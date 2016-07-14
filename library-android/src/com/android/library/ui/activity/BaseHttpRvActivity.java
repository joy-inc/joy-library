package com.android.library.ui.activity;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;

import com.android.library.R;
import com.android.library.adapter.ExRvAdapter;
import com.android.library.httptask.ObjectRequest;
import com.android.library.httptask.RequestMode;
import com.android.library.listener.OnLoadMoreListener;
import com.android.library.utils.CollectionUtil;
import com.android.library.utils.DeviceUtil;
import com.android.library.view.recyclerview.RecyclerAdapter;
import com.android.library.view.recyclerview.RecyclerAdapter.OnItemClickListener;
import com.android.library.view.recyclerview.RecyclerAdapter.OnItemLongClickListener;
import com.android.library.widget.JLoadingView;
import com.android.library.widget.JRecyclerView;

import java.util.List;

/**
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseHttpRvActivity<T> extends BaseHttpUiActivity<T> {

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private int mPageLimit = 20;
    private static final int PAGE_START_INDEX = 1;// 默认从第一页开始
    private int mPageIndex = PAGE_START_INDEX;
    private int mSortIndex = mPageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mRecyclerView = getDefaultRecyclerView();
        mRecyclerView.setLayoutManager(getDefaultLayoutManager());
        setContentView(wrapSwipeRefresh(mRecyclerView));
    }

    /**
     * 子类可以复写此方法，为自己定制RecyclerView
     *
     * @return
     */
    protected RecyclerView getDefaultRecyclerView() {

        JRecyclerView jrv = (JRecyclerView) inflateLayout(R.layout.lib_view_recycler);
        jrv.setLoadMoreView(JLoadingView.getLoadMore(this), JLoadingView.getLoadMoreLp());
        jrv.setLoadMoreListener(getDefaultLoadMoreLisn());
        return jrv;
    }

    /**
     * 子类可以复写此方法，为自己定制LayoutManager，默认为LinearLayoutManager
     * LinearLayoutManager (线性显示，类似于ListView)
     * GridLayoutManager (线性宫格显示，类似于GridView)
     * StaggeredGridLayoutManager(线性宫格显示，类似于瀑布流)
     *
     * @return
     */
    protected LayoutManager getDefaultLayoutManager() {

        return new LinearLayoutManager(this);
    }

    private View wrapSwipeRefresh(View contentView) {

        mSwipeRefreshWidget = new SwipeRefreshLayout(this);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.color_accent);
        mSwipeRefreshWidget.setOnRefreshListener(getDefaultRefreshLisn());
        mSwipeRefreshWidget.addView(contentView);
        return mSwipeRefreshWidget;
    }

    private OnRefreshListener getDefaultRefreshLisn() {

        return () -> {

            if (DeviceUtil.isNetworkDisable()) {

                hideSwipeRefresh();
                showToast(R.string.toast_common_no_network);
            } else {

                mSortIndex = mPageIndex;
                mPageIndex = PAGE_START_INDEX;
                stopLoadMore();
                startRefresh();
            }
        };
    }

    private OnLoadMoreListener getDefaultLoadMoreLisn() {

        return (isAuto) -> {

            if (DeviceUtil.isNetworkDisable()) {

                setLoadMoreFailed();
                if (!isAuto)
                    showToast(R.string.toast_common_no_network);
            } else {

                if (mPageIndex == PAGE_START_INDEX) {

                    if (getAdapter().getItemCount() == mPageLimit)
                        mPageIndex++;
                    else
                        mPageIndex = mSortIndex;
                }
                hideSwipeRefresh();
                startRefresh();
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
        onRetry();
    }

    /**
     * show frame refresh view {@link JLoadingView}
     */
    protected void executeFrameRefresh() {

        mPageIndex = PAGE_START_INDEX;
        onRetry();
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

    protected RecyclerView getRecyclerView() {

        return mRecyclerView;
    }

    protected int getHeaderViewsCount() {

        return ((RecyclerAdapter) mRecyclerView.getAdapter()).getHeadersCount();
    }

    protected int getFooterViewsCount() {

        return ((RecyclerAdapter) mRecyclerView.getAdapter()).getFootersCount();
    }

    protected void addHeaderView(View v) {

        ((RecyclerAdapter) mRecyclerView.getAdapter()).addHeaderView(v);
    }

    protected void addFooterView(View v) {

        ((RecyclerAdapter) mRecyclerView.getAdapter()).addFooterView(v);
    }

    protected void removeHeaderView(View v) {

        ((RecyclerAdapter) mRecyclerView.getAdapter()).removeHeader(v);
    }

    protected void removeFooterView(View v) {

        ((RecyclerAdapter) mRecyclerView.getAdapter()).removeFooter(v);
    }

    protected void setOnItemClickListener(OnItemClickListener listener) {

        ((RecyclerAdapter) mRecyclerView.getAdapter()).setOnItemClickListener(listener);
    }

    protected void setOnItemLongClickListener(OnItemLongClickListener listener) {

        ((RecyclerAdapter) mRecyclerView.getAdapter()).setOnItemLongClickListener(listener);
    }

    protected void setAdapter(ExRvAdapter adapter) {

        RecyclerAdapter ra = new RecyclerAdapter(adapter, getDefaultLayoutManager());
        getRecyclerView().setAdapter(ra);
    }

    protected ExRvAdapter getAdapter() {

        Adapter adapter = mRecyclerView.getAdapter();
        if (adapter instanceof RecyclerAdapter)
            return (ExRvAdapter) ((RecyclerAdapter) adapter).getWrappedAdapter();
        else
            return (ExRvAdapter) adapter;
    }

    @Override
    protected boolean invalidateContent(T t) {

        List<?> datas = getListInvalidateContent(t);
        if (CollectionUtil.isEmpty(datas))
            return false;

        setLoadMoreEnable(datas.size() >= mPageLimit);
        stopLoadMore();

        ExRvAdapter adapter = getAdapter();
        if (adapter != null) {

            if (mPageIndex == PAGE_START_INDEX)
                adapter.setData(datas);
            else
                adapter.addAll(datas);

            adapter.notifyDataSetChanged();

            if (isFinalResponse())
                mPageIndex++;
        }
        return true;
    }

    protected List<?> getListInvalidateContent(T t) {

        return (List<?>) t;
    }

    @Override
    final void onHttpFailed(String msg) {

        super.onHttpFailed(msg);

        if (isSwipeRefreshing()) {// 下拉刷新触发

        } else if (isLoadingMore()) {// 加载更多触发

            setLoadMoreFailed();
        } else {// 首次加载触发

        }
    }

    @Override
    protected final void showLoading() {

        if (getRequestMode() == RequestMode.CACHE_AND_REFRESH && isReqHasCache())
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

        if (getAdapter().getItemCount() == 0)
            super.showFailedTip();
    }

    @Override
    protected final void showNoContentTip() {

        if (getAdapter().getItemCount() == 0)
            super.showNoContentTip();
    }

    @Override
    protected final void hideContentView() {

        if (getAdapter().getItemCount() == 0)
            super.hideContentView();
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

        mSwipeRefreshWidget.post(this::showSwipeRefresh);
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

        if (!(mRecyclerView instanceof JRecyclerView))
            return false;
        if (!isLoadMoreEnable())
            return false;

        return ((JRecyclerView) mRecyclerView).isLoadingMore();
    }

    protected final void stopLoadMore() {

        if (!(mRecyclerView instanceof JRecyclerView))
            return;
        if (!isLoadMoreEnable())
            return;

        ((JRecyclerView) mRecyclerView).stopLoadMore();
    }

    protected final void setLoadMoreFailed() {

        if (!(mRecyclerView instanceof JRecyclerView))
            return;
        if (!isLoadMoreEnable())
            return;

        ((JRecyclerView) mRecyclerView).setLoadMoreFailed();
    }

    protected final boolean isLoadMoreFailed() {

        if (!(mRecyclerView instanceof JRecyclerView))
            return false;

        return ((JRecyclerView) mRecyclerView).isLoadMoreFailed();
    }

    protected final void setLoadMoreEnable(boolean enable) {

        if (!(mRecyclerView instanceof JRecyclerView))
            return;

        ((JRecyclerView) mRecyclerView).setLoadMoreEnable(enable);
    }

    protected final boolean isLoadMoreEnable() {

        if (!(mRecyclerView instanceof JRecyclerView))
            return false;

        return ((JRecyclerView) mRecyclerView).isLoadMoreEnable();
    }

    protected final void setLoadMoreDarkTheme() {

        if (!(mRecyclerView instanceof JRecyclerView))
            return;

        ((JRecyclerView) mRecyclerView).setLoadMoreDarkTheme();
    }

    protected final void setLoadMoreLightTheme() {

        if (!(mRecyclerView instanceof JRecyclerView))
            return;

        ((JRecyclerView) mRecyclerView).setLoadMoreLightTheme();
    }

    protected final void setLoadMoreHintTextColor(@ColorRes int resId) {

        if (!(mRecyclerView instanceof JRecyclerView))
            return;

        ((JRecyclerView) mRecyclerView).setLoadMoreHintTextColor(resId);
    }
    // =============================================================================================
}