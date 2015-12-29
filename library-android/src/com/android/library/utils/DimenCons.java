package com.android.library.utils;

import com.android.library.BaseApplication;
import com.android.library.R;

/**
 * Created by KEVIN.DAI on 15/7/16.
 */
public interface DimenCons {

    int DP_1_PX = DensityUtil.dip2px(1);

    int SCREEN_WIDTH = DeviceUtil.getScreenWidth();
    int SCREEN_HEIGHT = DeviceUtil.getScreenHeight();
    int STATUS_BAR_HEIGHT = DeviceUtil.getStatusBarHeight();
    int NAVIGATION_BAR_HEIGHT = DeviceUtil.getNavigationBarHeight();

    int TITLE_BAR_HEIGHT = BaseApplication.getAppResources().getDimensionPixelSize(R.dimen.default_toolbar_height);
}