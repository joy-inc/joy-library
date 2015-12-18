package com.android.library.view.banner;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

import com.ToxicBakery.viewpager.transforms.ABaseTransformer;
import com.android.library.R;
import com.android.library.utils.DimenCons;
import com.android.library.view.ExLayoutWidget;
import com.android.library.view.banner.indicator.CircleIndicator;

/**
 * Created by KEVIN.DAI on 15/12/17.
 */
public class BannerWidget<T> extends ExLayoutWidget implements DimenCons, BannerView.OnItemClickListener {

    private BannerView mBannerView;
    private CircleIndicator mIndicator;

    public BannerWidget(Activity activity, BannerAdapter<T> adapter) {

        super(activity);
        mBannerView.setAdapter(adapter);
        mIndicator.setViewPager(mBannerView);
    }

    @Override
    public void onResume() {

        mBannerView.startAutoScroll();
    }

    @Override
    public void onPause() {

        mBannerView.stopAutoScroll();
    }

    @Override
    protected View onCreateView(Activity activity, Object... args) {

        View rootView = activity.getLayoutInflater().inflate(R.layout.lib_view_banner, null);
        mBannerView = (BannerView) rootView.findViewById(R.id.banner);
        mBannerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, SCREEN_WIDTH / 9 * 5));
        mBannerView.setOnItemClickListener(this);
        mIndicator = (CircleIndicator) rootView.findViewById(R.id.indicator);
        return rootView;
    }

    @Override
    public void onItemClick(int position) {

        BannerAdapter<T> adapter = (BannerAdapter<T>) mBannerView.getAdapter();
        adapter.callbackItemViewClick(position % adapter.getIndicatorCount(), mBannerView);
    }

    public void setHeight(int dp) {

        mBannerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DP_1_PX * dp));
    }

    public void setPageTransformer(ABaseTransformer transformer) {

        mBannerView.setPageTransformer(true, transformer);
    }
}