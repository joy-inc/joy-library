package com.android.library.utils;

/**
 * Created by KEVIN.DAI on 15/7/16.
 */
public interface DimenCons {

    int SCREEN_WIDTH = DeviceUtil.getScreenWidth();
    int SCREEN_HEIGHT = DeviceUtil.getScreenHeight();
    int STATUS_BAR_HEIGHT = DeviceUtil.getStatusBarHeight();

    int DP_1_PX = DensityUtil.dip2px(1);
    int TITLE_BAR_HEIGHT = DP_1_PX * 56;
    int DP_3_PX = DP_1_PX * 3;
}
