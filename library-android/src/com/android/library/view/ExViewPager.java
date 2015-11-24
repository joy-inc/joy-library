package com.android.library.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 该ViewPager没有别的，就新增了ViewPager是否开启或关闭滚动的api
 *
 * @author yhb, modify by Daisw
 */
public class ExViewPager extends ViewPager {

    private boolean mScrollEnabled = true;

    public ExViewPager(Context context) {

        super(context);
    }

    public ExViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return mScrollEnabled ? super.onTouchEvent(ev) : false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return mScrollEnabled ? super.onInterceptTouchEvent(ev) : false;
    }

    public void setScrollEnabled(boolean enabled) {

        mScrollEnabled = enabled;
    }
}
