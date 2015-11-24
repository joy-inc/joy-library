package com.android.library.httptask;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.android.volley.Cache.Entry;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public class ObjectRequest<T> extends Request<T> {

    private Class mClazz;
    private ObjectResponseListener<T> mObjRespLis;

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url    URL to fetch the Object
     * @param clazz  the Object class to return
     */
    public ObjectRequest(int method, String url, Class clazz) {

        super(method, url, null);
        mClazz = clazz;
    }

    /**
     * Creates a new GET request.
     *
     * @param url   URL to fetch the Object
     * @param clazz the Object class to return
     */
    public ObjectRequest(String url, Class clazz) {

        this(Method.GET, url, clazz);
    }

    /**
     * @param lisn Listener to receive the Object response
     */
    public void setResponseListener(ObjectResponseListener<T> lisn) {

        mObjRespLis = lisn;
    }

    public void setCacheAndRefresh() {

    }

    public void setLoadFromCache() {

    }

    @Override
    protected void deliverResponse(T t) {

        if (mObjRespLis != null)
            mObjRespLis.onSuccess(getTag(), isTestMode() ? this.t : t);
    }

    @Override
    public void deliverError(VolleyError error) {

//        super.deliverError(error);
        if (isTestMode()) {

            deliverResponse(t);
        } else {

            if (mObjRespLis != null)
                mObjRespLis.onError(getTag(), error);
        }
    }

    @Override
    public Response<T> parseNetworkResponse(NetworkResponse response) {

        String parsed;
        try {

            String charsetName = HttpHeaderParser.parseCharset(response.headers);
            parsed = new String(response.data, charsetName);
        } catch (UnsupportedEncodingException e) {

            parsed = new String(response.data);
            e.printStackTrace();
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

                    if (jsonText.startsWith("[")) {// JsonArray

                        resp.setData(((T) JSON.parseArray(jsonText, mClazz)));
                    } else {// JsonObj

                        resp.setData((T) JSON.parseObject(jsonText, mClazz));
                    }
                }
            }
        } catch (Exception e) {

            resp.setParseBrokenStatus();
            e.printStackTrace();
        }
        return resp;
    }

    @Override
    protected void onFinish() {

//        super.onFinish();
        mClazz = null;
        mObjRespLis = null;
        t = null;
    }

    // --- for test data ---
    private T t;

    public void setData(T t) {

        this.t = t;
    }

    private boolean isTestMode() {

        return t != null;
    }
}