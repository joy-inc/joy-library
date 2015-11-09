package com.joy.library.httptask.frame;

import com.android.volley.Cache;

/**
 * Created by KEVIN.DAI on 15/11/9.
 */
public class CacheEntry extends Cache.Entry {

    @Override
    public boolean refreshNeeded() {

//        return super.refreshNeeded();
        return true;
    }
}
