package com.easylinknj.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

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

        setTitleText("列表页");
    }

    @Override
    protected void initContentView() {

        ListView lvCity = (ListView) findViewById(R.id.lvCity);
        mCityAdapter = new CityAdapter();
        lvCity.setAdapter(mCityAdapter);
        lvCity.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                View v = view.findViewById(R.id.sdvPhoto);
                String url = mCityAdapter.getItem(position).getPhoto();
                DetailActivity.startActivity(MainActivity.this, v, url);
            }
        });
    }

    @Override
    protected void onObjResponse(List<HotCityItem> datas) {

        mCityAdapter.setData(datas);
        mCityAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onFailedResponse(String msg) {

    }

    public static void startActivity(Activity act) {

        if (act == null)
            return;

        Intent intent = new Intent(act, MainActivity.class);
        act.startActivity(intent);
    }
}
