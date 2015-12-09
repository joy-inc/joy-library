package com.joy.library.share;

import android.app.Activity;

import com.joy.library.share.weibo.auth.WeiBoAuthActivity;

/**
 * 这里做的事情就是把分享的代码实现,不负责界面展现功能,集成友盟的jar包
 * 在结束activity结束的时候需要调用destroy方法
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-11-12
 */
public class ShareHandler {


    private Activity mActivity;

    public ShareHandler(Activity activity) {

        mActivity = activity;
        ShareWeixinUtil.getInstance().initApi(mActivity);

    }


    /**
     * 统一处理信息对应的类型发送
     *
     * @param info
     * @param type null的话为系统分享
     * @return
     */
    public boolean handleShare(ShareInfo info, ShareType type) {

        if (type == ShareType.SINA) {
            handleSinaMessage(info);
        } else if (type == ShareType.WEIXIN) {
            handleWeixinMessage(info);
        } else if (type == ShareType.WEIXIN_CIRCLE) {
            handleWeiXinCircleMessage(info);
        } else if (type == ShareType.EMAIL) {
            handleEmailMessage(info);
        } else if (type == ShareType.SMS) {
            handleSmsMessage(info);
        } else if (type == ShareType.MORE) {
            ShareSystemUtil.showSystemShare(mActivity, info.getContent(), info.getTitle());
        }
        return true;
    }


    /**
     * 处理新浪的信息写入
     */
    private void handleSinaMessage(ShareInfo info) {

//        ShareWeiBoUtil.openSinaClient(mActivity, info.getContent(), info.getMediaUrl());
        WeiBoAuthActivity.startActivity(mActivity,info.getContent(),info.getMediaUrl(),true);
    }


    /**
     * 处理发送给微信好友
     *
     * @param info
     */
    private void handleWeixinMessage(ShareInfo info) {

        ShareWeixinUtil.getInstance().send2WeixinFriend(info.getContent(), info.getUrl(), info.getMediaResId());
    }

    /**
     * 处理微信朋友圈信息
     *
     * @param info
     */
    private void handleWeiXinCircleMessage(ShareInfo info) {
        ShareWeixinUtil.getInstance().send2WeixinCircle(info.getUrl(), info.getContent(), info.getMediaResId());

    }

    /**
     * 处理email
     *
     * @param info
     */
    private void handleEmailMessage(ShareInfo info) {

    }


    /**
     * 短信的
     *
     * @param info
     */
    private void handleSmsMessage(ShareInfo info) {


    }
}
