package com.easylinknj.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.easylinknj.R;
import com.easylinknj.utils.CircleTransform;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by KEVIN.DAI on 15/7/8.
 */
public class MainActivity extends Activity {

    private RequestQueue mReqQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initData();
        initView();
    }

    @Override
    protected void onPause() {

        super.onPause();
        if (isFinishing())
            if (mReqQueue != null)
                mReqQueue.cancelAll(this);
    }

    private void initData() {

        mReqQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest("http://www.baidu.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (isFinishing()) {

                            Log.i("daisw", "onResponse finishing");
                            return;
                        }
                        Log.d("daisw", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (isFinishing()) {

                            Log.i("daisw", "onErrorResponse finishing");
                            return;
                        }
                        Log.e("daisw", error.getMessage(), error);
                    }
                });
        mReqQueue.add(stringRequest);
    }

    private void initView() {

        ImageView ivTest = (ImageView) findViewById(R.id.ivTest);
        Glide.with(this)
                .load("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg")
                .transform(new CircleTransform(this))// 转换为圆图
                .into(ivTest);

        SimpleDraweeView sdvTest = (SimpleDraweeView) findViewById(R.id.sdvTest);
        sdvTest.setImageURI(Uri.parse("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg"));
    }

    public static void startActivity(Activity activity){

        Intent intent = new Intent();
        intent.setClass(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}
