package com.joy.library.share;


/**
 * 返回对应的分享文案等信息
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-11-12
 */
public interface IShareInfo {
    /**
     * 获取对应平台的分享的文案
     * @param platform 平台类型
     * @return
     */
    ShareInfo getShareInfo(ShareType platform);
}
