package com.joy.library.share;

/**
 * 分享的包装信息类
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-11-12
 */
public class ShareInfo {
    private String title;
    private String content;
    private String url;//针对一些分享平台,如url.点击打开的地址
    private String mediaUrl;//分享的图片或媒体url地址
    private int mediaResId;//分享的资源id

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public int getMediaResId() {
        return mediaResId;
    }

    public void setMediaResId(int mediaResId) {
        this.mediaResId = mediaResId;
    }
}
