package com.joy.library.share;

import android.app.Activity;

import com.android.library.utils.TextUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 这里做的事情就是把分享的代码实现,不负责界面展现功能,集成友盟的jar包
 * 在结束activity结束的时候需要调用destroy方法
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-11-12
 */
public class ShareHandler {

    private final UMSocialService mController = UMServiceFactory.getUMSocialService(ShareConstant.getIns().getDescriptor());

    private Activity mActivity;
    private UMWXHandler wxHandler;
    private UMWXHandler wxCircleHandler;
    private EmailHandler emailHandler;
    private SmsHandler smsHandler;
    private SocializeListeners.SnsPostListener mSnsPostListener;

    public ShareHandler(Activity activity, SocializeListeners.SnsPostListener listener) {

        mActivity = activity;
        configPlatforms();
        mSnsPostListener = listener;
    }

    /**
     * 配置分享的平台
     */
    private void configPlatforms() {

        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        addQQZonePlatform();

        addWXPlatform();

        addEmail();

        addSMS();
    }

    /**
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     */
    private void addQQZonePlatform() {

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(mActivity, ShareConstant.getIns().getQqZoneAppid(), ShareConstant.getIns().getQqZoneKey());
        qqSsoHandler.setTargetUrl(ShareConstant.getIns().getQqZoneUrl());
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(mActivity, ShareConstant.getIns().getQqZoneAppid(), ShareConstant.getIns().getQqZoneKey());
        qZoneSsoHandler.addToSocialSDK();
    }

    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = ShareConstant.getIns().getWeixinAppid();
        String appSecret = ShareConstant.getIns().getWeixinSecret();
        // 添加微信平台
        wxHandler = new UMWXHandler(mActivity, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        wxCircleHandler = new UMWXHandler(mActivity, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    /**
     * 添加Email平台</br>
     */
    private void addEmail() {

        emailHandler = new EmailHandler();
        emailHandler.addToSocialSDK();
    }

    /**
     * 添加短信平台</br>
     */
    private void addSMS() {

        smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();
    }


    /**
     * 在退出的时候需要清除
     */
    public void destroy() {

        mController.getConfig().cleanListeners();
    }

    /**
     * 统一处理信息对应的类型发送
     *
     * @param info
     * @param type null的话为系统分享
     * @return
     */
    public boolean handleShare(ShareInfo info, SHARE_MEDIA type) {

        if (type == null) {
            ShareSystemUtil.showSystemShare(mActivity, info.getContent(), info.getTitle());
            return true;
        } else {
            if (type == SHARE_MEDIA.SINA) {
                handleSinaMessage(info);
            } else if (type == SHARE_MEDIA.WEIXIN) {
                handleWeixinMessage(info);
            } else if (type == SHARE_MEDIA.WEIXIN_CIRCLE) {
                handleWeiXinCircleMessage(info);
            } else if (type == SHARE_MEDIA.EMAIL) {
                handleEmailMessage(info);
            } else if (type == SHARE_MEDIA.QQ) {
                handleQqMessage(info);
            } else if (type == SHARE_MEDIA.QZONE) {
                handleQzoneMessage(info);
            } else if (type == SHARE_MEDIA.SMS) {
                handleSmsMessage(info);
            }
            mController.postShare(mActivity, type, mSnsPostListener);
        }
        return false;
    }

    /**
     * 返回一个图片的媒体类
     *
     * @param info
     * @return
     */
    private UMImage getImage(ShareInfo info) {
        if (!TextUtil.isEmpty(info.getUrl())) {
            return new UMImage(mActivity, info.getMediaUrl());
        } else if (info.getMediaResId() > 0) {
            return new UMImage(mActivity, info.getMediaResId());
        }
        return null;
    }

    /**
     * 处理新浪的信息写入
     */
    private void handleSinaMessage(ShareInfo info) {

        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(info.getContent());
        sinaContent.setShareImage(getImage(info));
        mController.setShareMedia(sinaContent);
    }


    /**
     * 处理发送给微信好友
     *
     * @param info
     */
    private void handleWeixinMessage(ShareInfo info) {

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(info.getContent());
        weixinContent.setTitle(info.getTitle());
        weixinContent.setTargetUrl(info.getUrl());
        weixinContent.setShareMedia(getImage(info));
        mController.setShareMedia(weixinContent);
    }

    /**
     * 处理微信朋友圈信息
     *
     * @param info
     */
    private void handleWeiXinCircleMessage(ShareInfo info) {

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(info.getContent());
        weixinContent.setTitle(info.getTitle());
        weixinContent.setTargetUrl(info.getUrl());
        weixinContent.setShareMedia(getImage(info));
        mController.setShareMedia(weixinContent);
    }

    /**
     * 处理email
     *
     * @param info
     */
    private void handleEmailMessage(ShareInfo info) {

        MailShareContent mail = new MailShareContent(getImage(info));
        mail.setTitle(info.getTitle());
        mail.setShareContent(info.getContent());
        mController.setShareMedia(mail);
    }

    /**
     * 处理qq的消息
     *
     * @param info
     */
    private void handleQqMessage(ShareInfo info) {

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(info.getContent());
        qqShareContent.setTitle(info.getTitle());
        qqShareContent.setShareMedia(getImage(info));
        qqShareContent.setTargetUrl(info.getUrl());
        mController.setShareMedia(qqShareContent);
    }

    /**
     * 处理qzone的
     *
     * @param info
     */
    private void handleQzoneMessage(ShareInfo info) {

        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(info.getContent());
        qzone.setTargetUrl(info.getUrl());
        qzone.setTitle(info.getTitle());
        qzone.setShareMedia(getImage(info));
        mController.setShareMedia(qzone);
    }

    /**
     * 短信的
     *
     * @param info
     */
    private void handleSmsMessage(ShareInfo info) {

        SmsShareContent sms = new SmsShareContent();
        sms.setShareContent(info.getContent());
        // sms.setShareImage(urlImage);
        mController.setShareMedia(sms);
    }
}
