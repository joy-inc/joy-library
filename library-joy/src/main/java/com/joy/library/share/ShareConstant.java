package com.joy.library.share;

/**
 * 分享的一些常量
 * 包括分享平台的key
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-11-12
 */
public class ShareConstant {

    private String descriptor = "com.joy.lib" ;//"com.umeng.share";

    //微信
    private String weixinAppid;
    private String weixinSecret;
    //qq空间
    private String qqZoneAppid;
    private String qqZoneKey;
    private String qqZoneUrl;
    //微博
    //    private String weiboAppid;
    //    private String weiboSecret;
    //    private String weiboUrl;
    //    private String weiboscope;
    private static ShareConstant mShareConstant = null;

    private ShareConstant() {

    }

    public static ShareConstant getIns() {
        if (mShareConstant == null) {
            mShareConstant = new ShareConstant();
        }
        return mShareConstant;
    }

    public String getWeixinAppid() {
        return weixinAppid;
    }

    public void setWeixinAppid(String weixinAppid) {
        this.weixinAppid = weixinAppid;
    }

    public String getWeixinSecret() {
        return weixinSecret;
    }

    public void setWeixinSecret(String weixinSecret) {
        this.weixinSecret = weixinSecret;
    }

    public String getQqZoneAppid() {
        return qqZoneAppid;
    }

    public void setQqZoneAppid(String qqZoneAppid) {
        this.qqZoneAppid = qqZoneAppid;
    }

    public String getQqZoneKey() {
        return qqZoneKey;
    }

    public void setQqZoneKey(String qqZoneKey) {
        this.qqZoneKey = qqZoneKey;
    }

    public String getQqZoneUrl() {
        return qqZoneUrl;
    }

    public void setQqZoneUrl(String qqZoneUrl) {
        this.qqZoneUrl = qqZoneUrl;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
}
