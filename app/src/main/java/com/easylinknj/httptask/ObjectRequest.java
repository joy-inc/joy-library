package com.easylinknj.httptask;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.android.volley.Cache.Entry;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public class ObjectRequest<T> extends Request<T> {

    private Listener<T> mObjLis;
    private Class mClazz;

    /**
     * Creates a new request with the given method.
     *
     * @param method   the request {@link Method} to use
     * @param url      URL to fetch the Object
     * @param objLis   Listener to receive the Object response
     * @param errorLis Error listener, or null to ignore errors
     * @param clazz    the Object class to return
     */
    public ObjectRequest(int method, String url, Listener<T> objLis, ErrorListener errorLis, Class clazz) {

        super(method, url, errorLis);
        mObjLis = objLis;
        mClazz = clazz;
    }

    /**
     * Creates a new GET request.
     *
     * @param url      URL to fetch the Object
     * @param objLis   Listener to receive the Object response
     * @param errorLis Error listener, or null to ignore errors
     * @param clazz    the Object class to return
     */
    public ObjectRequest(String url, Listener<T> objLis, ErrorListener errorLis, Class clazz) {

        this(Method.GET, url, objLis, errorLis, clazz);
    }

    @Override
    protected void deliverResponse(T response) {

        mObjLis.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        String parsed;
        try {

            String charsetName = HttpHeaderParser.parseCharset(response.headers);
            parsed = new String(response.data, charsetName);
        } catch (UnsupportedEncodingException e) {

            parsed = new String(response.data);
        }

        T t = onResponse(parsed).getData();
        Entry entry = HttpHeaderParser.parseCacheHeaders(response);
        return Response.success(t, entry);
    }

    private QyerResponse<T> onResponse(String jsonText) {

        QyerResponse<T> resp = new QyerResponse();

        if (TextUtils.isEmpty(jsonText)) {

            resp.setParseBrokenStatus();
            return resp;
        }

        try {

            JSONObject jsonObj = new JSONObject(jsonText);
            resp.setStatus(jsonObj.getInt("status"));
            resp.setInfo(jsonObj.getString("info"));

            if (resp.isSuccess()) {

                jsonText = jsonObj.getString("data");
                if (TextUtils.isEmpty(jsonText)) {

                    resp.setData((T) mClazz.newInstance());
                } else {

                    if (jsonText.startsWith("[")) {

                        // JsonArray
                        resp.setData(((T) JSON.parseArray(jsonText, mClazz)));
                    } else {

                        // JsonObj
                        resp.setData((T) JSON.parseObject(jsonText, mClazz));
                    }
                }
            }
        } catch (Exception e) {

            resp.setParseBrokenStatus();
        }

        return resp;
    }
}
