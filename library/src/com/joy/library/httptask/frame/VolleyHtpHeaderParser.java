package com.joy.library.httptask.frame;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

/**
 * Created by KEVIN.DAI on 15/11/8.
 * Utility methods for parsing HTTP headers.
 * 跟默认的比，可以强制缓存，忽略服务器的设置。
 */
public class VolleyHtpHeaderParser extends HttpHeaderParser {

    private static long MAX_AGE = 1000 * 60 * 60 * 24 * 7;// 7天

    /**
     * Extracts a {@link Cache.Entry} from a {@link NetworkResponse}.
     *
     * @param response The network response to parse headers from
     * @return a cache entry for the given response, or null if the response is not cacheable.
     */
    public static Cache.Entry parseCacheHeaders(NetworkResponse response) {

        Map<String, String> headers = response.headers;

        long serverDate = 0;
        String headerValue = headers.get("Date");
        if (headerValue != null)
            serverDate = parseDateAsEpoch(headerValue);

        long softExpire = serverDate + MAX_AGE;
        long finalExpire = softExpire;

        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = null;// server Etag
        entry.softTtl = softExpire;
        entry.ttl = finalExpire;
        entry.serverDate = serverDate;
        entry.lastModified = 0;// last modified
        entry.responseHeaders = headers;

        return entry;
    }
}