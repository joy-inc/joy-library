package com.android.library.httptask;

import com.android.volley.Cache.Entry;
import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

/**
 * Created by KEVIN.DAI on 15/11/8.
 * Utility methods for parsing HTTP headers.
 * 跟默认的比，可以强制缓存，忽略服务器的设置。
 */
public class HeaderParser extends HttpHeaderParser {

//    public static int MAX_AGE = 365 * 24 * 60 * 60 * 1000;

    /**
     * Extracts a {@link Entry} from a {@link NetworkResponse}.
     *
     * @param response The network response to parse headers from
     * @return a cache entry for the given response, or null if the response is not cacheable.
     */
    public static Entry parseCacheHeaders(NetworkResponse response) {

        Map<String, String> headers = response.headers;

        long serverDate = 0;
        String headerValue = headers.get("Date");
        if (headerValue != null)
            serverDate = parseDateAsEpoch(headerValue);

        Entry entry = new Entry();
        entry.data = response.data;
        entry.responseHeaders = headers;
        entry.serverDate = serverDate;
//        entry.ttl = serverDate + MAX_AGE;
//        entry.softTtl = entry.ttl;
//        entry.etag = null;
//        entry.lastModified = 0l;

        return entry;
    }
}