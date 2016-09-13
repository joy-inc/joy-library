package com.android.library.view.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.library.R;
import com.android.library.adapter.ExRvAdapter;
import com.android.library.utils.LayoutInflater;
import com.android.library.widget.JRecyclerView;

/**
 * Created by Daisw on 16/8/4.
 */

public class JBottomSheetRvDialog extends JBottomSheetDialog {

    private RecyclerView.LayoutManager mLayoutManager;
    private ExRvAdapter mAdapter;

    private RecyclerView.ItemDecoration mItemDecoration;

    public JBottomSheetRvDialog(@NonNull Context context) {

        super(context);
        mLayoutManager = new LinearLayoutManager(context);
    }

    public void setLayoutManager(@NonNull RecyclerView.LayoutManager layoutManager) {

        mLayoutManager = layoutManager;
    }

    public void setAdapter(@NonNull ExRvAdapter adapter) {

        mAdapter = adapter;
    }

    public void addItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration) {

        mItemDecoration = itemDecoration;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        JRecyclerView jrv = LayoutInflater.inflate(getContext(), R.layout.lib_view_recycler);
        jrv.setLayoutManager(mLayoutManager);
        jrv.setAdapter(mAdapter);
        if (mItemDecoration != null)
            jrv.addItemDecoration(mItemDecoration);
        setContentView(jrv);

        super.onCreate(savedInstanceState);
    }
}
