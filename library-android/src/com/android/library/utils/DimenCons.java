package com.android.library.utils;

/**
 * Created by KEVIN.DAI on 15/7/16.
 */
public interface DimenCons {

    int DP_1_PX = DensityUtil.dip2px(1);
    int DP_3_PX = DP_1_PX * 3;

    int SCREEN_WIDTH = DeviceUtil.getScreenWidth();
    int SCREEN_HEIGHT = DeviceUtil.getScreenHeight();
    int STATUS_BAR_HEIGHT = DeviceUtil.getStatusBarHeight();
    int NAVIGATION_BAR_HEIGHT = DeviceUtil.getNavigationBarHeight();

    int TITLE_BAR_HEIGHT = DP_1_PX * 56;
}