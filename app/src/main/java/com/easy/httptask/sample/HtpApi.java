package com.easy.httptask.sample;

/**
 * 网络请求 api
 */
public interface HtpApi {

    String URL_BASE = "http://open.qyer.com";// base url

    String URL_GET_SEARCH_HOT_CITY = URL_BASE + "/qyer/hotel/hot_city_list";// 200热门城市
}
