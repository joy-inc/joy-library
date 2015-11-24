package com.joy.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;

import com.joy.library.utils.DimenCons;

/**
 * 全局统一的LoadingView
 * Created by KEVIN.DAI on 15/11/19.
 */
public class JLoadingView extends ProgressBar implements DimenCons {

    public JLoadingView(Context context) {

        super(context);
    }

    public JLoadingView(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    public void hide() {

        setVisibility(INVISIBLE);
    }

    public void gone() {

        setVisibility(GONE);
    }

    public void show() {

        setVisibility(VISIBLE);
    }

    public static LayoutParams getLp() {

        return new LayoutParams(DP_1_PX * 80, DP_1_PX * 80, Gravity.CENTER);
    }

    /**
     * 适用于Activity/Fragment页面的加载提示 80dp
     *
     * @param context
     * @return
     */
    public static JLoadingView get(Context context) {

        JLoadingView v = new JLoadingView(context);
        v.setIndeterminate(true);
        return v;
    }

    public static LayoutParams getLoadMoreLp() {

        return new LayoutParams(DP_1_PX * 30, DP_1_PX * 30);
    }

    /**
     * 适用于列表loadmore 30dp
     *
     * @param context
     * @return
     */
    public static JLoadingView getLoadMore(Context context) {

        JLoadingView v = new JLoadingView(context);
        v.setIndeterminate(true);
        return v;
    }
}