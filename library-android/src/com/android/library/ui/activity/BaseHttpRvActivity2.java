package com.android.library.ui.activity;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.android.library.R;
import com.android.library.adapter.ExRvAdapter;
import com.android.library.ui.RefreshMode;
import com.android.library.ui.activity.interfaces.BaseViewNetRv;
import com.android.library.view.recyclerview.RecyclerAdapter;
import com.android.library.view.recyclerview.RecyclerAdapter.OnItemClickListener;
import com.android.library.view.recyclerview.RecyclerAdapter.OnItemLongClickListener;
import com.android.library.widget.JLoadingView;
import com.android.library.widget.JRecyclerView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.android.library.ui.RefreshMode.FRAME;
import static com.android.library.ui.RefreshMode.SWIPE;

/**
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseHttpRvActivity2 extends BaseHttpUiActivity2 implements BaseViewNetRv {

    private SwipeRefreshLayout mSwipeRl;
    private RecyclerView mRecyclerView;
    private RefreshMode mRefreshMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mRecyclerView = provideRecyclerView();
        mRecyclerView.setLayoutManager(provideLayoutManager());
        setContentView(wrapSwipeRefresh(mRecyclerView));
    }

    /**
     * 子类可以复写此方法，为自己定制RecyclerView
     */
    @Override
    public RecyclerView provideRecyclerView() {

        JRecyclerView jrv = (JRecyclerView) inflateLayout(R.layout.lib_view_recycler);
        jrv.setLoadMoreView(JLoadingView.getLoadMore(this), JLoadingView.getLoadMoreLp());
        return jrv;
    }

    /**
     * 子类可以复写此方法，为自己定制LayoutManager，默认为LinearLayoutManager
     * LinearLayoutManager (线性显示，类似于ListView)
     * GridLayoutManager (线性宫格显示，类似于GridView)
     * StaggeredGridLayoutManager(线性宫格显示，类似于瀑布流)
     */
    @Override
    public LayoutManager provideLayoutManager() {

        return new LinearLayoutManager(this);
    }

    private View wrapSwipeRefresh(View contentView) {

        mSwipeRl = new SwipeRefreshLayout(this);
        mSwipeRl.setColorSchemeResources(R.color.color_accent);
        mSwipeRl.addView(contentView, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        return mSwipeRl;
    }

    @Override
    public final RecyclerView getRecyclerView() {

        return mRecyclerView;
    }

    @Override
    public final LayoutManager getLayoutManager() {

        return mRecyclerView.getLayoutManager();
    }

    @Override
    public final void setRefreshMode(RefreshMode mode) {

        mRefreshMode = mode;
    }

    @Override
    public final int getHeaderViewsCount() {

        return ((RecyclerAdapter) mRecyclerView.getAdapter()).getHeadersCount();
    }

    @Override
    public final int getFooterViewsCount() {

        return ((RecyclerAdapter) mRecyclerView.getAdapter()).getFootersCount();
    }

    @Override
    public final void addHeaderView(View v) {

        ((RecyclerAdapter) mRecyclerView.getAdapter()).addHeaderView(v);
    }

    @Override
    public final void addFooterView(View v) {

        ((RecyclerAdapter) mRecyclerView.getAdapter()).addFooterView(v);
    }

    @Override
    public final void removeHeaderView(View v) {

        ((RecyclerAdapter) mRecyclerView.getAdapter()).removeHeader(v);
    }

    @Override
    public final void removeFooterView(View v) {

        ((RecyclerAdapter) mRecyclerView.getAdapter()).removeFooter(v);
    }

    @Override
    public final void setOnItemClickListener(OnItemClickListener lisn) {

        ((RecyclerAdapter) mRecyclerView.getAdapter()).setOnItemClickListener(lisn);
    }

    @Override
    public final void setOnItemLongClickListener(OnItemLongClickListener lisn) {

        ((RecyclerAdapter) mRecyclerView.getAdapter()).setOnItemLongClickListener(lisn);
    }

    @Override
    public final void setAdapter(ExRvAdapter adapter) {

        mRecyclerView.setAdapter(new RecyclerAdapter(adapter, getLayoutManager()));
    }

    @Override
    public final ExRvAdapter getAdapter() {

        Adapter adapter = mRecyclerView.getAdapter();
        if (adapter instanceof RecyclerAdapter)
            return (ExRvAdapter) ((RecyclerAdapter) adapter).getWrappedAdapter();
        else
            return (ExRvAdapter) adapter;
    }

    @Override
    public final void showLoading() {

        switch (mRefreshMode) {

            case SWIPE:
                showSwipeRefresh();
                stopLoadMore();
                super.hideLoading();
                break;
            case FRAME:
                hideSwipeRefresh();
                hideLoadMore();
                super.showLoading();
                break;
            case LOADMORE:
                hideSwipeRefresh();
                break;
            default:
                break;
        }
    }

    @Override
    public final void hideLoading() {

        switch (mRefreshMode) {

            case SWIPE:
                hideSwipeRefresh();
                break;
            case FRAME:
                super.hideLoading();
                break;
            case LOADMORE:
                stopLoadMore();
                break;
            default:
                break;
        }
    }

    @Override
    public final void showErrorTip() {

        switch (mRefreshMode) {

            case SWIPE:
                showToast(R.string.toast_common_timeout);
                break;
            case FRAME:
                if (getAdapter().getItemCount() == 0)
                    super.showErrorTip();
                break;
            case LOADMORE:
                setLoadMoreFailed();
                break;
            default:
                break;
        }
    }

    @Override
    public final void showEmptyTip() {

        if ((mRefreshMode == SWIPE || mRefreshMode == FRAME) && getAdapter().getItemCount() == 0)
            super.showEmptyTip();
    }

    @Override
    public final void hideContent() {

        if ((mRefreshMode == SWIPE || mRefreshMode == FRAME) && getAdapter().getItemCount() == 0)
            super.hideContent();
    }

    // --------- swipe refresh ---------
    @Override
    public final SwipeRefreshLayout getSwipeRefreshLayout() {

        return mSwipeRl;
    }

    @Override
    public final void setSwipeRefreshEnable(boolean enable) {

        mSwipeRl.setEnabled(enable);
    }

    @Override
    public final boolean isSwipeRefreshing() {

        return mSwipeRl.isRefreshing();
    }

    @Override
    public final void showSwipeRefresh() {

        if (!isSwipeRefreshing())
            mSwipeRl.post(() -> mSwipeRl.setRefreshing(true));
    }

    @Override
    public final void hideSwipeRefresh() {

        if (isSwipeRefreshing())
            mSwipeRl.setRefreshing(false);
    }

    @Override
    public final void setSwipeRefreshColors(@ColorRes int... resIds) {

        mSwipeRl.setColorSchemeResources(resIds);
    }
    // --------- swipe refresh ---------

    // --------- loadmore ---------
    @Override
    public final void setLoadMoreEnable(boolean enable) {

        if (mRecyclerView instanceof JRecyclerView)
            ((JRecyclerView) mRecyclerView).setLoadMoreEnable(enable);
    }

    @Override
    public final boolean isLoadMoreEnable() {

        return mRecyclerView instanceof JRecyclerView && ((JRecyclerView) mRecyclerView).isLoadMoreEnable();
    }

    @Override
    public final boolean isLoadingMore() {

        return isLoadMoreEnable() && ((JRecyclerView) mRecyclerView).isLoadingMore();
    }

    @Override
    public final void stopLoadMore() {

        if (isLoadMoreEnable())
            ((JRecyclerView) mRecyclerView).stopLoadMore();
    }

    @Override
    public final void setLoadMoreFailed() {

        if (isLoadMoreEnable())
            ((JRecyclerView) mRecyclerView).setLoadMoreFailed();
    }

    @Override
    public final void hideLoadMore() {

        if (isLoadMoreEnable())
            ((JRecyclerView) mRecyclerView).hideLoadMore();
    }

    @Override
    public final void setLoadMoreTheme(Theme theme) {

        if (isLoadMoreEnable()) {

            switch (theme) {

                case LIGHT:
                    ((JRecyclerView) mRecyclerView).setLoadMoreLightTheme();
                    break;
                case DARK:
                    ((JRecyclerView) mRecyclerView).setLoadMoreDarkTheme();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public final void setLoadMoreColor(@ColorRes int resId) {

        if (isLoadMoreEnable())
            ((JRecyclerView) mRecyclerView).setLoadMoreHintTextColor(resId);
    }
    // --------- loadmore ---------
}
