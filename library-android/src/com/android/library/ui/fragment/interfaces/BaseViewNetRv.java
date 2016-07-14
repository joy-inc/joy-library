package com.android.library.ui.fragment.interfaces;

import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;

import com.android.library.ui.RefreshMode;
import com.android.library.adapter.ExRvAdapter;
import com.android.library.view.recyclerview.RecyclerAdapter.OnItemClickListener;
import com.android.library.view.recyclerview.RecyclerAdapter.OnItemLongClickListener;

/**
 * Created by Daisw on 16/6/7.
 */
public interface BaseViewNetRv extends BaseViewNet {

    enum Theme {LIGHT, DARK}

    RecyclerView provideRecyclerView();
    LayoutManager provideLayoutManager();
    RecyclerView getRecyclerView();
    LayoutManager getLayoutManager();
    void setAdapter(ExRvAdapter adapter);
    ExRvAdapter getAdapter();

    int getHeaderViewsCount();
    int getFooterViewsCount();
    void addHeaderView(View v);
    void addFooterView(View v);
    void removeHeaderView(View v);
    void removeFooterView(View v);

    void setOnItemClickListener(OnItemClickListener lisn);
    void setOnItemLongClickListener(OnItemLongClickListener lisn);

    SwipeRefreshLayout getSwipeRefreshLayout();
    void setSwipeRefreshEnable(boolean enable);
    boolean isSwipeRefreshing();
    void showSwipeRefresh();
    void hideSwipeRefresh();
    void setSwipeRefreshColors(@ColorRes int... resIds);

    void setLoadMoreEnable(boolean enable);
    boolean isLoadMoreEnable();
    boolean isLoadingMore();
    void stopLoadMore();
    void setLoadMoreFailed();
    void hideLoadMore();
    void setLoadMoreTheme(Theme theme);
    void setLoadMoreColor(@ColorRes int resId);

    void setRefreshMode(RefreshMode mode);
}
