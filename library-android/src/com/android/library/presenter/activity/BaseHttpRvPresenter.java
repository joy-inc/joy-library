package com.android.library.presenter.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;

import com.android.library.R;
import com.android.library.adapter.ExRvAdapter;
import com.android.library.httptask.ObjectRequest;
import com.android.library.httptask.RequestMode;
import com.android.library.listener.OnLoadMoreListener;
import com.android.library.ui.activity.interfaces.BaseViewNetRv;
import com.android.library.utils.DeviceUtil;
import com.android.library.widget.JLoadingView;
import com.android.library.widget.JRecyclerView;

import java.util.List;

import rx.Observable;

import static com.android.library.httptask.RequestMode.CACHE_AND_REFRESH;
import static com.android.library.httptask.RequestMode.REFRESH_ONLY;
import static com.android.library.ui.RefreshMode.FRAME;
import static com.android.library.ui.RefreshMode.LOADMORE;
import static com.android.library.ui.RefreshMode.SWIPE;

/**
 * Created by Daisw on 16/6/8.
 */
public class BaseHttpRvPresenter<T, V extends BaseViewNetRv> extends RequestLauncherImpl<T, V> {

    private static final int PAGE_UPPER_LIMIT = 20;// 默认分页大小
    private static final int PAGE_START_INDEX = 1;// 默认起始页码
    private int mPageLimit = PAGE_UPPER_LIMIT;
    private int mPageIndex = PAGE_START_INDEX;
    private int mSortIndex = mPageIndex;

    @Override
    public void attachView(V v) {

        super.attachView(v);
        getBaseView().getSwipeRefreshLayout().setOnRefreshListener(getRefreshLisn());
        RecyclerView rv = getBaseView().getRecyclerView();
        if (rv instanceof JRecyclerView) {

            JRecyclerView jrv = (JRecyclerView) rv;
            jrv.setLoadMoreListener(getLoadMoreLisn());
        }
    }

    private OnRefreshListener getRefreshLisn() {

        return () -> {

            if (DeviceUtil.isNetworkDisable()) {

                getBaseView().hideSwipeRefresh();
                getBaseView().showToast(R.string.toast_common_no_network);
            } else {

                mSortIndex = mPageIndex;

                setPageIndex(PAGE_START_INDEX);
                getBaseView().setRefreshMode(SWIPE);
                launch(getObjectRequest(getParams()), REFRESH_ONLY);// refresh only, don't cache
            }
        };
    }

    private OnLoadMoreListener getLoadMoreLisn() {

        return isAuto -> {

            if (DeviceUtil.isNetworkDisable()) {

                getBaseView().setLoadMoreFailed();
                if (!isAuto)
                    getBaseView().showToast(R.string.toast_common_no_network);
            } else {

                if (mPageIndex == PAGE_START_INDEX) {

                    if (getBaseView().getAdapter().getItemCount() == mPageLimit)
                        mPageIndex++;
                    else
                        mPageIndex = mSortIndex;
                }

                getBaseView().setRefreshMode(LOADMORE);
                launch(getObjectRequest(getParams()), REFRESH_ONLY);// refresh only, don't cache
            }
        };
    }

    @Override
    public Observable<T> launch(ObjectRequest<T> request, RequestMode mode) {

        Observable<T> observable = super.launch(request, mode);
        observable
                .doOnSubscribe(super::doOnFirst)
                .filter(super::filterNull)
                .map(this::transform)
                .filter(this::filterEmpty)
                .subscribe(
                        this::onNext,
                        super::onError);
        return observable;
    }

    public List<?> transform(T t) {

        return (List<?>) t;
    }

    public boolean filterEmpty(List<?> ts) {

        if (ts.isEmpty())
            onEmpty();
        return !ts.isEmpty();
    }

    @Override
    public void onEmpty() {

        ExRvAdapter adapter = getBaseView().getAdapter();
        if (adapter != null) {

            if (mPageIndex == PAGE_START_INDEX) {

                final int adapterItemCount = adapter.getItemCount();
                if (adapterItemCount > 0) {

                    adapter.clear();
                    adapter.notifyItemRangeRemoved(0, adapterItemCount);
                }
            } else {

                getBaseView().setLoadMoreEnable(false);
                return;
            }
        }
        super.onEmpty();
    }

    public void onNext(List<?> ts) {

        final int currentItemCount = ts.size();
        getBaseView().setLoadMoreEnable(currentItemCount >= mPageLimit);

        ExRvAdapter adapter = getBaseView().getAdapter();
        if (adapter != null) {

            final int adapterItemCount = adapter.getItemCount();
            if (mPageIndex == PAGE_START_INDEX) {

                adapter.setData(ts);
                if (adapterItemCount == 0) {

                    adapter.notifyItemRangeInserted(0, currentItemCount);
                    ((JRecyclerView) getBaseView().getRecyclerView()).addLoadMoreIfNotExist();
                } else {

                    adapter.notifyItemRangeRemoved(0, adapterItemCount);
                    adapter.notifyItemRangeInserted(0, currentItemCount);
                    getBaseView().getLayoutManager().scrollToPosition(0);
                }
            } else {

                adapter.addAll(ts);
                adapter.notifyItemRangeInserted(adapterItemCount, currentItemCount);
            }
            if (isFinalResponse())
                mPageIndex++;
        }
        super.onNext(null);
    }

    @Override
    public final Observable<T> launchRefreshOnly(String... params) {

        setPageIndex(PAGE_START_INDEX);
        getBaseView().setRefreshMode(FRAME);
        return super.launchRefreshOnly(params);
    }

    @Override
    public final Observable<T> launchCacheOnly(String... params) {

        setPageIndex(PAGE_START_INDEX);
        getBaseView().setRefreshMode(FRAME);
        return super.launchCacheOnly(params);
    }

    @Override
    public final Observable<T> launchRefreshAndCache(String... params) {

        setPageIndex(PAGE_START_INDEX);
        getBaseView().setRefreshMode(FRAME);
        return super.launchRefreshAndCache(params);
    }

    @Override
    public final Observable<T> launchCacheAndRefresh(String... params) {

        setParams(params);
        setPageIndex(PAGE_START_INDEX);
        ObjectRequest<T> req = getObjectRequest(params);
        getBaseView().setRefreshMode(req.hasCache() ? SWIPE : FRAME);
        return launch(req, CACHE_AND_REFRESH);
    }

    /**
     * show swipe refresh view {@link SwipeRefreshLayout}
     */
    public final void launchSwipeRefresh(String... params) {

        setParams(params);
        setPageIndex(PAGE_START_INDEX);
        getBaseView().setRefreshMode(SWIPE);
        launch(getObjectRequest(params), getRequestMode());
    }

    /**
     * show frame refresh view {@link JLoadingView}
     */
    public final void launchFrameRefresh(String... params) {

        setParams(params);
        setPageIndex(PAGE_START_INDEX);
        getBaseView().setRefreshMode(FRAME);
        launch(getObjectRequest(params), getRequestMode());
    }

    /**
     * 设置分页大小
     *
     * @param limit
     */
    public final void setPageLimit(int limit) {

        mPageLimit = limit;
    }

    public final int getPageLimit() {

        return mPageLimit;
    }

    /**
     * 设置页码
     *
     * @param index
     */
    public final void setPageIndex(int index) {

        mPageIndex = index;
    }

    public final int getPageIndex() {

        return mPageIndex;
    }
}
