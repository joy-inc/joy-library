package com.easy.activity.main;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.easy.R;
import com.easy.activity.frame.BaseUiActivity;

/**
 * Created by KEVIN.DAI on 15/7/11.
 */
public class DetailTestActivity extends BaseUiActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitleView() {

        setTitleText("详情页");
    }

    @Override
    protected void initContentView() {

        ImageView ivPhoto = (ImageView) findViewById(R.id.ivDetailPhoto);
        Glide.with(this)
                .load(getIntent().getStringExtra("photoUrl"))
                .placeholder(R.color.transparent)
                .into(ivPhoto);

        findViewById(R.id.tvTest).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FullScreenActivity.startActivity(DetailTestActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    /**
     * @param act
     * @param view The view which starts the transition
     */
    public static void startActivity(Activity act, View view, String photoUrl) {

        if (act == null || view == null)
            return;

        Intent intent = new Intent(act, DetailTestActivity.class);
        intent.putExtra("photoUrl", photoUrl);

        if (isLollipopOrUpper()) {

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(act, view, view.getTransitionName());
            act.startActivity(intent, options.toBundle());
        } else {

            act.startActivity(intent);
        }
    }
}
