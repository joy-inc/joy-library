package com.android.library.ui.share;

import android.support.annotation.DrawableRes;

import com.android.library.R;

/**
 * Created by Daisw on 16/9/7.
 */

public enum ShareBean {

    WECHAT(R.drawable.ic_share_wechat, "微信"),
    WECHAT_ZONE(R.drawable.ic_share_wechat_moments, "朋友圈"),
    QQ(R.drawable.ic_share_qq, "QQ"),
    WEIBO(R.drawable.ic_share_weibo, "微博"),
    EMAIL(R.drawable.ic_share_email, "邮件"),
    COPY_LINK(R.drawable.ic_share_copylink, "复制链接");

    @DrawableRes
    int mIconResId;
    String mName;

    ShareBean(@DrawableRes int iconResId, String name) {

        mIconResId = iconResId;
        mName = name;
    }
}
