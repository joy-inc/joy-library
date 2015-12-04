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
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public RecyclerAdapter(Adapter adapter, LayoutManager lm) {

        mHeaderArrays = new SparseArray<>();
        mFooterArrays = new SparseArray<>();

        setWrappedAdapter(adapter);
        mLayoutManager = lm;

        if (lm instanceof GridLayoutManager) {

            GridLayoutManager glm = (GridLayoutManager) lm;
            glm.setSpanSizeLookup(new SpanSizeLookup(this, glm.getSpanCount()));
        }
    }

    public void setWrappedAdapter(Adapter<ViewHolder> adapter) {

        if (adapter == null)
            return;

        if (mAdapter != null) {

            notifyItemRangeRemoved(getHeadersCount(), mAdapter.getItemCount());
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
        }

        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(mDataObserver);
        notifyItemRangeInserted(getHeadersCount(), mAdapter.getItemCount());
    }

    public Adapter getWrappedAdapter() {

        return mAdapter;
    }

    @Override
    public int getItemCount() {

        return getHeadersCount() + getFootersCount() + mAdapter.getItemCount();
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

            return mFooterArrays.keyAt(position - getHeadersCount() - mAdapter.getItemCount());
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

            if (mOnItemClickListener != null) {

                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        mOnItemClickListener.onItemClick(holder, position - getHeadersCount());
                    }
                });
            }
            if (mOnItemLongClickListener != null) {

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {

                        return mOnItemLongClickListener.onItemLongCLick(holder, position - getHeadersCount());
                    }
                });
            }
        } else {

            if (mLayoutManager != null && mLayoutManager instanceof StaggeredGridLayoutManager) {

                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                params.setFullSpan(true);
                holder.itemView.setLayoutParams(params);
            }
        }
    }

    private class RvViewHolder extends ViewHolder {

        public RvViewHolder(View itemView) {

            super(itemView);
        }
    }

    public Integer getHeadersCount() {

        return mHeaderArrays.size();
    }

    public View getHeaderView(int index) {

        return mHeaderArrays.valueAt(index);
    }

    public Integer getFootersCount() {

        return mFooterArrays.size();
    }

    public View getFooterView(int index) {

        return mFooterArrays.valueAt(index);
    }

    public void addHeaderView(View v) {

        if (v == null)
            return;

        if (mHeaderArrays.size() == 0)
            mHeaderArrays.put(Integer.MIN_VALUE, v);
        else if (mHeaderArrays.indexOfValue(v) < 0)
            mHeaderArrays.put(mHeaderArrays.keyAt(mHeaderArrays.size() - 1) + 1, v);
        else
            return;
        notifyDataSetChanged();
    }

    public void addFooterView(View v) {

        if (v == null)
            return;

        if (mFooterArrays.size() == 0)
            mFooterArrays.put(Integer.MAX_VALUE, v);
        else if (mFooterArrays.indexOfValue(v) < 0)
            mFooterArrays.put(mFooterArrays.keyAt(mFooterArrays.size() - 1) - 1, v);
        else
            return;
        notifyDataSetChanged();
    }

    public boolean removeHeader(View v) {

        int index = mHeaderArrays.indexOfValue(v);
        if (index >= 0) {

            mHeaderArrays.removeAt(index);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public boolean removeFooter(View v) {

        int index = mFooterArrays.indexOfValue(v);
        if (index >= 0) {

            mFooterArrays.removeAt(index);
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public boolean isHeader(int position) {

        return getHeadersCount() > 0 && position < getHeadersCount();
    }

    public boolean isItem(int position) {

        return position >= getHeadersCount() && position < getHeadersCount() + mAdapter.getItemCount();
    }

    public boolean isFooter(int position) {

        return getFootersCount() > 0 && position >= getHeadersCount() + mAdapter.getItemCount();
    }

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {

        @Override
        public void onChanged() {

            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {

            super.onItemRangeChanged(positionStart, itemCount);
            notifyItemRangeChanged(positionStart + getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {

            super.onItemRangeInserted(positionStart, itemCount);
            notifyItemRangeInserted(positionStart + getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {

            super.onItemRangeRemoved(positionStart, itemCount);
            notifyItemRangeRemoved(positionStart + getHeadersCount(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {

            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            notifyItemRangeChanged(fromPosition + getHeadersCount(), toPosition + getHeadersCount() + itemCount);
        }
    };

    public interface OnItemClickListener {

        void onItemClick(ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener l) {

        mOnItemClickListener = l;
    }

    public interface OnItemLongClickListener {

        boolean onItemLongCLick(ViewHolder holder, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener l) {

        mOnItemLongClickListener = l;
    }
}