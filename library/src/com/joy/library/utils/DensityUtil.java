package com.joy.library.utils;

import com.joy.library.BaseApplication;

public class DensityUtil {

    public static int dip2px(float dpValue) {

        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
