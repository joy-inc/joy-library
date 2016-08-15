package com.android.library.view.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by KEVIN.DAI on 15/12/1.
 */
public class RecyclerAdapter extends Adapter<ViewHolder> {

    /**
     * The real adapter for RecyclerView
     */
    private Adapter<ViewHolder> mAdapter;
    private LayoutManager mLayoutManager;
    private SparseArray<View> mHeaderArrays, mFooterArrays;

    public RecyclerAdapter(Adapter<ViewHolder> adapter, LayoutManager lm) {

        mHeaderArrays = new SparseArray<>();
        mFooterArrays = new SparseArray<>();

        setWrappedAdapter(adapter);
        mLayoutManager = lm;

        if (lm instanceof GridLayoutManager) {

            GridLayoutManager glm = (GridLayoutManager) lm;
            glm.setSpanSizeLookup(new SpanSizeLookup(this, glm.getSpanCount()));
        }
    }

    private void setWrappedAdapter(Adapter<ViewHolder> adapter) {

        if (adapter == null)
            return;

        if (mAdapter != null) {

            notifyItemRangeRemoved(getHeadersCount(), getWrappedItemCount());
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
        }

        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(mDataObserver);
        notifyItemRangeInserted(getHeadersCount(), getWrappedItemCount());
    }

    public Adapter<ViewHolder> getWrappedAdapter() {

        return mAdapter;
    }

    private int getWrappedItemCount() {

        return mAdapter.getItemCount();
    }

    @Override
    public int getItemCount() {

        return getHeadersCount() + getFootersCount() + getWrappedItemCount();
    }

    @Override
    public long getItemId(int position) {

        if (isItem(position))
            return mAdapter.getItemId(position - getHeadersCount());

        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemViewType(int position) {

        if (isHeader(position)) {

            return mHeaderArrays.keyAt(position);
        } else if (isFooter(position)) {

            return mFooterArrays.keyAt(position - getHeadersCount() - getWrappedItemCount());
        } else {

            return mAdapter.getItemViewType(position - getHeadersCount());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mHeaderArrays.indexOfKey(viewType) >= 0) {

            return new RvViewHolder(mHeaderArrays.get(viewType));
        } else if (mFooterArrays.indexOfKey(viewType) >= 0) {

            return new RvViewHolder(mFooterArrays.get(viewType));
        } else {

            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (isItem(position)) {

            mAdapter.onBindViewHolder(holder, position - getHeadersCount());
        } else {

            if (mLayoutManager != null && mLayoutManager instanceof StaggeredGridLayoutManager) {

                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                params.setFullSpan(true);
                holder.itemView.setLayoutParams(params);
            }
        }
    }

    private class RvViewHolder extends ViewHolder {

        RvViewHolder(View itemView) {

            super(itemView);
        }
    }

    public int getHeadersCount() {

        return mHeaderArrays.size();
    }

    public View getHeaderView(int index) {

        return mHeaderArrays.valueAt(index);
    }

    public int getFootersCount() {

        return mFooterArrays.size();
    }

    public View getFooterView(int index) {

        return mFooterArrays.valueAt(index);
    }

    public void addHeaderView(View v) {

        if (v == null)
            return;

        if (mHeaderArrays.size() == 0) {

            mHeaderArrays.put(Integer.MIN_VALUE, v);
            notifyItemInserted(getHeadersCount() - 1);
        } else if (mHeaderArrays.indexOfValue(v) < 0) {

            mHeaderArrays.put(mHeaderArrays.keyAt(mHeaderArrays.size() - 1) + 1, v);
            notifyItemInserted(getHeadersCount() - 1);
        }
    }

    public void addFooterView(View v) {

        if (v == null)
            return;

        if (mFooterArrays.size() == 0) {

            mFooterArrays.put(Integer.MAX_VALUE, v);
            notifyItemInserted(getItemCount() - 1);
        } else if (mFooterArrays.indexOfValue(v) < 0) {

            mFooterArrays.put(mFooterArrays.keyAt(mFooterArrays.size() - 1) - 1, v);
            notifyItemInserted(getItemCount() - 1);
        }
    }

    public boolean removeHeader(View v) {

        int index = mHeaderArrays.indexOfValue(v);
        if (index >= 0) {

            mHeaderArrays.removeAt(index);
            notifyItemRemoved(index);
            return true;
        }
        return false;
    }

    public boolean removeFooter(View v) {

        int index = mFooterArrays.indexOfValue(v);
        if (index >= 0) {

            mFooterArrays.removeAt(index);
            notifyItemRemoved(getHeadersCount() + getWrappedItemCount() + index);
            return true;
        }
        return false;
    }

    boolean isHeader(int position) {

        return getHeadersCount() > 0 && position < getHeadersCount();
    }

    private boolean isItem(int position) {

        return position >= getHeadersCount() && position < getHeadersCount() + getWrappedItemCount();
    }

    boolean isFooter(int position) {

        return getFootersCount() > 0 && position >= getHeadersCount() + getWrappedItemCount();
    }

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {

            notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {

            notifyItemRangeInserted(positionStart + getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {

            notifyItemRangeRemoved(positionStart + getHeadersCount(), itemCount);
        }
    };
}
