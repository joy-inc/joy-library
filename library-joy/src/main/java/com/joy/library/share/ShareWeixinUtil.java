/**
 * Copyright (c) 2004, qyer.com, Inc. All rights reserved.
 */
package com.joy.library.share;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.library.BaseApplication;
import com.android.library.utils.ImageUtil;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信的单例操作,分享和发送给朋友 <br>
 * @author liulongzhenhai 2015-1-21 下午2:38:57 <br>
 * @see
 */
public class ShareWeixinUtil {
    private static ShareWeixinUtil mWeixinUtil;

    private IWXAPI wxapi;

    private ShareWeixinUtil() {

    }

    public static ShareWeixinUtil getInstance() {
        if (mWeixinUtil == null) {
            mWeixinUtil = new ShareWeixinUtil();
        }
        return mWeixinUtil;
    }

    public void initApi(Activity act) {
        wxapi = WXAPIFactory.createWXAPI(act, ShareConstant.getIns().getWeixinAppid(), true);
        wxapi.registerApp(ShareConstant.getIns().getWeixinAppid());
    }

    /**
     * 发送给微信朋友
     * @param url 地址
     * @param content 内容
     * @param iconResId 图标
     */
    public boolean sendWeixinFriend(String title, String content, String url, int iconResId) {

        return send(title, content, url, iconResId, SendMessageToWX.Req.WXSceneSession);
    }

    /**
     * 发送给微信朋友圈
     * @param url 地址
     * @param content 内容
     * @param iconResId 图标
     * @return
     */
    public boolean send2Weixin(String url, String content, int iconResId) {

        return send(content, content, url, iconResId, SendMessageToWX.Req.WXSceneTimeline);
    }

    private boolean send(String title, String content, String url, int iconResId, int scene) {

        if (wxapi.getWXAppSupportAPI() < 0x21020001) {
            return false;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = content;
        msg.description = content;

        Bitmap bmp = BitmapFactory.decodeResource(BaseApplication.getContext().getResources(), iconResId);
        //		Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
        //		bmp.recycle();
        msg.thumbData = ImageUtil.bmpToByteArray(bmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = scene;

        // 调用api接口发送数据到微信
        return wxapi.sendReq(req);
    }

    /**
     * 获取唯一标识符
     * @param type
     * @return
     */
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 是否有微博客户端
     *
     * @return
     */
    public static boolean hasWeChatClient() {
        try {

            PackageInfo packageInfo = BaseApplication.getContext().getPackageManager().getPackageInfo("com.tencent.mm", 0);
            if (packageInfo == null)
                return false;

            int highBit = packageInfo.versionName.charAt(0);
            return highBit > 50 ? true : false;// 50 = 2

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
