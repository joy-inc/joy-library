package com.joy.library.activity.frame;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.joy.library.R;
import com.joy.library.adapter.frame.ExAdapter;

import java.util.List;

/**
 * Created by KEVIN.DAI on 15/7/16.
 */
public abstract class BaseHttpLvActivity<T extends List<?>> extends BaseHttpUiActivity<T> {

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private ListView mListView;

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
    protected ListView getDefaultListView() {

        return (ListView) inflateLayout(R.layout.view_listview);
    }

    private View wrapSwipeRefresh(View contentView) {

        // swipe refresh widget
        mSwipeRefreshWidget = new SwipeRefreshLayout(this);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light, R.color.holo_orange_light, R.color.holo_red_light);
        mSwipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {// default listener

            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        stopSwipeRefresh();
                    }
                }, 5000);
            }
        });
        mSwipeRefreshWidget.addView(contentView);
        return mSwipeRefreshWidget;
    }

    protected ListView getListView() {

        return mListView;
    }

    protected void addHeaderView(View v) {

        getListView().addHeaderView(v);
    }

    protected void addFooterView(View v) {

        getListView().addFooterView(v);
    }

    protected void setAdapter(ExAdapter<?> adapter) {

        getListView().setAdapter(adapter);
    }

    protected void setOnItemClickListener(OnItemClickListener listener) {

        getListView().setOnItemClickListener(listener);
    }

    @Override
    protected void invalidateContent(T datas) {

        ExAdapter adapter = getAdapter();
        if (adapter != null) {

            adapter.setData(datas);
            adapter.notifyDataSetChanged();
        }
    }

    protected ExAdapter<?> getAdapter() {

        ListAdapter la = getListView().getAdapter();

        if (la == null)
            return null;

        if (la instanceof HeaderViewListAdapter) {

            HeaderViewListAdapter hvla = (HeaderViewListAdapter) la;
            return (ExAdapter<?>) hvla.getWrappedAdapter();
        } else {

            return (ExAdapter<?>) la;
        }
    }

    protected void setColorSchemeResources(int... colorResIds) {

        mSwipeRefreshWidget.setColorSchemeResources(colorResIds);
    }

    protected void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener lisn) {

        mSwipeRefreshWidget.setOnRefreshListener(lisn);
    }

    protected boolean isSwipeRefreshing() {

        return mSwipeRefreshWidget.isRefreshing();
    }

    protected void startSwipeRefresh() {

        if (isSwipeRefreshing())
            return;

        mSwipeRefreshWidget.setRefreshing(true);
    }

    protected void stopSwipeRefresh() {

        if (!isSwipeRefreshing())
            return;

        mSwipeRefreshWidget.setRefreshing(false);
    }
}
