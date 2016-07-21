package com.android.library.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.animation.DecelerateInterpolator;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

/**
 * Created by Daisw on 16/7/18.
 *
 * @see RecyclerView#setItemAnimator(RecyclerView.ItemAnimator)
 */
public class ItemAddAnimator extends FadeInUpAnimator {

    private static final int PAGE_UPPER_LIMIT = 20;// 默认分页大小
    private int mPageLimit = PAGE_UPPER_LIMIT;

    private static final long DEFAULT_ADD_DURATION = 120;
    private static final long FIRST_PAGE_ADD_DURATION = 300;
    private static final float DEFAULT_INTERPOLATOR_FACTOR = 3.f;

    public ItemAddAnimator() {

        init();
    }

    public ItemAddAnimator(int pageLimit) {

        mPageLimit = pageLimit;
        init();
    }

    private void init() {

        setAddDuration(FIRST_PAGE_ADD_DURATION);
        setInterpolator(new DecelerateInterpolator(DEFAULT_INTERPOLATOR_FACTOR));
    }

    /**
     * 设置分页大小
     *
     * @param pageLimit 分页大小
     */
    public void setPageLimit(int pageLimit) {

        mPageLimit = pageLimit;
    }

    @Override
    protected long getAddDelay(RecyclerView.ViewHolder holder) {

        long delay;
        if (holder.getLayoutPosition() >= mPageLimit) {

            setAddDuration(DEFAULT_ADD_DURATION);
            delay = 0;
        } else {

            setAddDuration(FIRST_PAGE_ADD_DURATION);
            delay = super.getAddDelay(holder);
        }
        return delay;
    }
}
