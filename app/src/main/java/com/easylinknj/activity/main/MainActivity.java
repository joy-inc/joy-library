package com.easylinknj.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.easylinknj.BuildConfig;
import com.easylinknj.R;
import com.easylinknj.adapter.CityAdapter;
import com.easylinknj.bean.HotCityItem;
import com.easylinknj.httptask.TestHtpUtil;

import java.util.List;

/**
 * Created by KEVIN.DAI on 15/7/8.
 */
public class MainActivity extends BaseActivity<List<HotCityItem>> {

    private CityAdapter mCityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
    }

    @Override
    protected void onPause() {

        super.onPause();
        if (isFinishing())
            if (mReqQueue != null)
                mReqQueue.cancelAll(0);
    }

    @Override
    protected void initData() {

        executeAPI(TestHtpUtil.getTestUrl(), 0, HotCityItem.class);
    }

    @Override
    protected void initTitleView() {

    }

    @Override
    protected void initContentView() {

//        // Glide
//        ImageView ivTest = (ImageView) findViewById(R.id.ivTest);
//        Glide.with(this)
//                .load("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg")
//                .transform(new CircleTransform(this))// 转换为圆图
//                .into(ivTest);
//
//        // Fresco
//        SimpleDraweeView sdvTest = (SimpleDraweeView) findViewById(R.id.sdvTest);
//        sdvTest.setImageURI(Uri.parse("http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg"));

        ListView lvCity = (ListView) findViewById(R.id.lvCity);
        mCityAdapter = new CityAdapter();
        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(MainActivity.this);

            }
        });
//        mCityAdapter.setOnItemViewClickListener(new OnItemViewClickListener() {
//
//            @Override
//            public void onItemViewClick(int position, View clickView) {
//
//                startActivity(MainActivity.this);
//            }
//        });
        lvCity.setAdapter(mCityAdapter);
    }

    @Override
    protected void onSuccessResponse(List<HotCityItem> datas) {

        if (BuildConfig.DEBUG)
            Log.d("MainActivity", "~~onSuccessResponse: " + datas.size());
        mCityAdapter.setData(datas);
        mCityAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onFailedResponse(String msg) {

        if (BuildConfig.DEBUG)
            Log.d("MainActivity", "~~onErrorResponse: ");
    }

    public static void startActivity(Activity act) {

        Intent intent = new Intent();
        intent.setClass(act, MainActivity.class);
        act.startActivity(intent);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
