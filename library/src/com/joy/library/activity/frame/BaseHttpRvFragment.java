package com.joy.library.activity.frame;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.joy.library.R;
import com.joy.library.adapter.frame.ExRvAdapter;
import com.joy.library.utils.CollectionUtil;

import java.util.List;

/**
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseHttpRvFragment<T> extends BaseHttpUiFragment<T> {

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private int mPageLimit = 20;
    private static final int PAGE_START_INDEX = 1;// 默认从第一页开始
    private int mCurrentPageIndex = PAGE_START_INDEX;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        mRecyclerView = getDefaultRecyclerView();
        setContentView(wrapSwipeRefresh(mRecyclerView));
    }

    @Override
    public void onPause() {

        super.onPause();
        if (isFinishing()) {

            // TODO abort all http task.
        }
    }

    /**
     * 子类可以复写此方法，为自己定制RecyclerView
     *
     * @return
     */
    protected RecyclerView getDefaultRecyclerView() {

        return (RecyclerView) inflateLayout(R.layout.lib_view_recycler);
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

//            abortSwipeRefresh();
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

    protected RecyclerView getRecyclerView() {

        return mRecyclerView;
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

    protected void setAdapter(ExRvAdapter adapter) {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
    }

    protected ExRvAdapter getAdapter() {

        try {
            return (ExRvAdapter) mRecyclerView.getAdapter();
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
        ExRvAdapter adapter = getAdapter();
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