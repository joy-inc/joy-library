package com.easylinknj.activity.main;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.easylinknj.BuildConfig;
import com.easylinknj.httptask.QyerResponse;

import org.json.JSONObject;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public abstract class BaseActivity<T> extends Activity {

    protected RequestQueue mReqQueue;
    private Class mClazz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mReqQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void setContentView(int layoutResID) {

        super.setContentView(layoutResID);
        init();
    }

    @Override
    public void setContentView(View view) {

        super.setContentView(view);
        init();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {

        super.setContentView(view, params);
        init();
    }

    private void init() {

        initData();
        initTitleView();
        initContentView();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mReqQueue = null;
    }

    protected abstract void initData();

    protected abstract void initTitleView();

    protected abstract void initContentView();

    protected abstract void onSuccessResponse(T datas);

    protected abstract void onFailedResponse(String msg);

    protected void executeAPI(String url, Object tag, Class clazz) {

        mClazz = clazz;
        StringRequest req = new StringRequest(url, getSuccessLis(), getErrorLis());
        req.setTag(tag);
        mReqQueue.add(req);
    }

    private Response.Listener<String> getSuccessLis() {

        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (isFinishing())
                    return;

                if (BuildConfig.DEBUG)
                    Log.d("BaseActivity", response);

                onSuccessResponse(onTaskResponse(response).getData());
            }
        };
    }

    private Response.ErrorListener getErrorLis() {

        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (isFinishing())
                    return;

                if (BuildConfig.DEBUG)
                    Log.e("BaseActivity", error.getMessage(), error);

                onFailedResponse(error.getMessage());
            }
        };
    }

    private QyerResponse<T> onTaskResponse(String jsonText) {

        QyerResponse<T> resp = new QyerResponse<>();
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
