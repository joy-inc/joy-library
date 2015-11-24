package com.android.library.utils;

import com.android.library.BaseApplication;

public class DensityUtil {

    public static int dip2px(float dpValue) {

        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
