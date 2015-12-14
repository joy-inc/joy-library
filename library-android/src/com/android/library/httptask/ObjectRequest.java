package com.android.library.httptask;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.android.library.BaseApplication;
import com.android.library.httptask.TestCache.OnEntryListener;
import com.android.library.utils.CollectionUtil;
import com.android.library.utils.LogMgr;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache.Entry;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public class ObjectRequest<T> extends Request<T> {

    private Class mClazz;
    private ObjectResponseListener<T> mObjRespLis;
    private Map<String, String> mHeaders, mParams;
    private CacheMode mCacheMode = CacheMode.NONE;
    private boolean mHasCache;
    private Response<T> mObjResp;

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url    URL to fetch the Object
     * @param clazz  the Object class to return
     */
    private ObjectRequest(int method, String url, Class clazz) {

        super(method, url, null);
        mClazz = clazz;
        mHasCache = BaseApplication.getVolleyCache().get(getCacheKey()) != null;
        setShouldCache(false);
        addEntryListener();
    }

    private void addEntryListener() {

        TestCache cache = (TestCache) BaseApplication.getVolleyCache();
        if (cache != null)
            cache.addEntryListener(mOnEntryListener);
    }

    private void removeEntryListener() {

        TestCache cache = (TestCache) BaseApplication.getVolleyCache();
        if (cache != null)
            cache.removeEntryListener(mOnEntryListener);
    }

    private OnEntryListener mOnEntryListener = new OnEntryListener() {

        @Override
        public void onEntry(TestEntry entry) {

            if (entry != null)
                entry.setCacheMode(mCacheMode);
        }
    };

    /**
     * Creates a new GET request.
     *
     * @param url   URL to fetch the Object
     * @param clazz the Object class to return
     */
    public static ObjectRequest get(String url, Class clazz) {

        return new ObjectRequest(Method.GET, url, clazz);
    }

    /**
     * Creates a new POST request.
     *
     * @param url   URL to fetch the Object
     * @param clazz the Object class to return
     */
    public static ObjectRequest post(String url, Class clazz) {

        return new ObjectRequest(Method.POST, url, clazz);
    }

    public void setHeaders(Map<String, String> headers) {

        mHeaders = headers;
    }

    public void setParams(Map<String, String> params) {

        mParams = params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        if (CollectionUtil.isNotEmpty(mHeaders))
            return mHeaders;
        return super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {

        if (CollectionUtil.isNotEmpty(mParams))
            return mParams;
        return super.getParams();
    }

    public void setCacheMode(CacheMode mode) {

        mCacheMode = mode;
    }

    public CacheMode getCacheMode() {

        return mCacheMode;
    }

    public boolean hasCache() {

        return mHasCache;
    }

    /**
     * @return True if this response was a soft-expired one and a second one MAY be coming.
     */
    public boolean isRespIntermediate() {

        return mObjResp == null ? false : mObjResp.intermediate;
    }

    /**
     * @param lisn Listener to receive the Object response
     */
    public void setResponseListener(ObjectResponseListener<T> lisn) {

        mObjRespLis = lisn;
        if (mObjRespLis != null)// TODO 暂时先放在这儿
            mObjRespLis.onPre();
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
                mObjRespLis.onError(getTag(), ErrorHelper.getErrorType(error));
        }
    }

    @Override
    public Response<T> parseNetworkResponse(NetworkResponse response) {

        String parsed;
        try {

            String charsetName = HeaderParser.parseCharset(response.headers);
            parsed = new String(response.data, charsetName);
        } catch (UnsupportedEncodingException e) {

            parsed = new String(response.data);
            e.printStackTrace();
        }

        QyerResponse<T> resp = onResponse(parsed);
        if (resp.isSuccess()) {

            Entry entry = HeaderParser.parseCacheHeaders(response);
            mObjResp = Response.success(resp.getData(), entry);
            return mObjResp;
        } else {

            return Response.error(new VolleyError(resp.getMsg()));
        }
    }

    private QyerResponse<T> onResponse(String json) {

        if (LogMgr.isDebug())
            LogMgr.d("ObjectRequest", "~~json: " + json);

        QyerResponse<T> resp = new QyerResponse();

        if (TextUtils.isEmpty(json)) {

            resp.setParseBrokenStatus();
            return resp;
        }

        try {

            JSONObject jsonObj = new JSONObject(json);
            if (jsonObj.has("status"))
                resp.setStatus(jsonObj.getInt("status"));
            if (jsonObj.has("msg"))
                resp.setMsg(jsonObj.getString("msg"));

            if (resp.isSuccess()) {

                json = jsonObj.getString("data");
                if (TextUtils.isEmpty(json)) {

                    resp.setData((T) mClazz.newInstance());
                } else {

                    if (json.startsWith("[")) {// JsonArray

                        resp.setData(((T) JSON.parseArray(json, mClazz)));
                    } else {// JsonObj

                        resp.setData((T) JSON.parseObject(json, mClazz));
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
        t = null;
        mClazz = null;
        mHasCache = false;
//        mCacheMode = CacheMode.NONE;
        mObjRespLis = null;
        mObjResp = null;

        removeEntryListener();

        if (mHeaders != null) {
            mHeaders.clear();
            mHeaders = null;
        }
        if (mParams != null) {
            mParams.clear();
            mParams = null;
        }
    }

    // --- for test data ---
    private T t;

    public void setData(T t) {

        this.t = t;
    }

    private boolean isTestMode() {

        return t != null;
    }

    @Override
    public void cancel() {

        super.cancel();
        if (LogMgr.isDebug())
            LogMgr.d("ObjectRequest", "~~cancel tag: " + getTag());
    }
}