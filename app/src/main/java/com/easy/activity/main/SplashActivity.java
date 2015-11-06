package com.easy.activity.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.easy.R;

/**
 * 闪屏页面
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        delayStartMainActivity();
    }

    private void delayStartMainActivity() {

        new Handler() {

            @Override
            public void handleMessage(Message msg) {

                if (!isFinishing()) {

                    finishToEnterActivity();
                }
            }
        }.sendEmptyMessageDelayed(0, 2000);
    }

    private void finishToEnterActivity() {

        ListTestActivity.startActivity(this);
        finish();
    }
}
