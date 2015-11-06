package com.easy.httptask;

/**
 * 网络请求 api
 */
public interface HtpApi {

    String URL_BASE = "http://open.qyer.com";// base url

    String URL_LOGIN = URL_BASE + "/qyer/user/login";// 登录

    String URL_GET_SEARCH_HOT_CITY = URL_BASE + "/qyer/hotel/hot_city_list";// 200热门城市
    String URL_GET_ONWAY_CITY_DETAIL = URL_BASE + "/qyer/onroad/city_detail";// 根据坐标获取对应的城市信息（包含聊天室）
}
