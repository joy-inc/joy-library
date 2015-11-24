package com.android.library.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.android.library.activity.BaseUiFragment;

import java.util.List;

/**
 * @author Daisw
 */
public class ExFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<? extends BaseUiFragment> mFragments;
    private boolean mFragmentItemDestoryEnable = true;

    public ExFragmentPagerAdapter(FragmentManager fragmentManager) {

        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {

        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        if (mFragmentItemDestoryEnable)
            super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {

        return mFragments == null ? 0 : mFragments.size();
    }

    public void setFragments(List<? extends BaseUiFragment> fragments) {

        mFragments = fragments;
    }

    public void setFragmentItemDestoryEnable(boolean enable) {

        mFragmentItemDestoryEnable = enable;
    }

    /**
     * 该方法已被弃用
     */
    @Override
    @Deprecated
    public void destroyItem(View container, int position, Object object) {

        if (mFragmentItemDestoryEnable)
            super.destroyItem(container, position, object);
    }

    /**
     * 该方法已被弃用
     */
    @Override
    @Deprecated
    public Object instantiateItem(View container, int position) {

        return super.instantiateItem(container, position);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mFragments.get(position).getLableText();
    }
}