package com.easylinknj.utils;

import com.easylinknj.EasyApplication;

public class DensityUtil {

    public static int dip2px(float dpValue) {

        final float scale = EasyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
