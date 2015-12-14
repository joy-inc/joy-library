package com.android.library.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;

import com.android.library.R;
import com.android.library.adapter.ExRvAdapter;
import com.android.library.httptask.CacheMode;
import com.android.library.httptask.ObjectRequest;
import com.android.library.listener.OnLoadMoreListener;
import com.android.library.utils.CollectionUtil;
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
//        mSwipeRefreshWidget.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_red_light);
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
     * show swipe refresh view
     */
    protected void executeSwipeRefresh() {

        mSwipeRefreshWidget.setRefreshing(true);
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

    protected RecyclerView getRecyclerView() {

        return mRecyclerView;
    }

    protected void addHeaderView(View v) {

        ((RecyclerAdapter) mRecyclerView.getAdapter()).addHeaderView(v);
    }

    protected void addFooterView(View v) {

        ((RecyclerAdapter) mRecyclerView.getAdapter()).addFooterView(v);
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

        if (mRecyclerView instanceof JRecyclerView && isLoadMoreEnable()) {

            JRecyclerView jrv = (JRecyclerView) mRecyclerView;
            jrv.setLoadMoreEnable(datas.size() >= mPageLimit);
            if (jrv.isLoadMoreEnable())
                jrv.stopLoadMore();
        }

        ExRvAdapter adapter = getAdapter();
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

    @Override
    protected void hideContentView() {

        if (getAdapter().isEmpty())
            super.hideContentView();
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

        if (mRecyclerView instanceof JRecyclerView)
            return isLoadMoreEnable() && ((JRecyclerView) mRecyclerView).isLoadingMore();
        return false;
    }

    protected final void onLoadMoreFailed() {

        if (mRecyclerView instanceof JRecyclerView && isLoadMoreEnable())
            ((JRecyclerView) mRecyclerView).stopLoadMoreFailed();
    }

    protected void setLoadMoreEnable(boolean enable) {

        if (mRecyclerView instanceof JRecyclerView)
            ((JRecyclerView) mRecyclerView).setLoadMoreEnable(enable);
    }

    private boolean isLoadMoreEnable() {

        return ((JRecyclerView) mRecyclerView).isLoadMoreEnable();
    }
}