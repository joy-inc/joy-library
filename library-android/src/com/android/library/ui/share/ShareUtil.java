package com.android.library.ui.share;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

import com.android.library.utils.ToastUtil;
import com.android.library.view.bottomsheet.JBottomSheetRvDialog;
import com.android.library.view.recyclerview.ItemDecoration;

import java.util.Arrays;
import java.util.List;

import static com.android.library.utils.DimenCons.DP_1_PX;

/**
 * Created by Daisw on 16/9/7.
 */

public class ShareUtil {

    private final JBottomSheetRvDialog mShareDialog;

    public ShareUtil(Context context) {

        mShareDialog = new JBottomSheetRvDialog(context);
        mShareDialog.setLayoutManager(new GridLayoutManager(context, 3));
        mShareDialog.addItemDecoration(new ItemDecoration.Builder(context)
                .dividerSize(0)
                .verticalSpace(DP_1_PX * 16)
                .paddingParent(DP_1_PX * 24)
                .build());

        ShareAdapter adapter = new ShareAdapter();
        adapter.setData(getShareBeans());
        adapter.setOnItemClickListener((position, view, shareBean) -> {
            mShareDialog.dismiss();
            ToastUtil.showToast(shareBean.mName);
        });
        mShareDialog.setAdapter(adapter);
    }

    private List<ShareBean> getShareBeans() {

        return Arrays.asList(ShareBean.values());
    }

    public void show() {

        mShareDialog.show();
    }

    public void dismiss() {

        mShareDialog.dismiss();
    }
}
