package com.joy.library.share.weibo.auth;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * 扩展添加用户名
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-12-09
 */
public class JoyOauth2AccessToken extends Oauth2AccessToken {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
