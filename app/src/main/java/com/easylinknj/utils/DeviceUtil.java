package com.easylinknj.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.easylinknj.BuildConfig;
import com.easylinknj.EasyApplication;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public class DeviceUtil {

    public static String getIMEI() {

        String imei = "";

        try {

            Context ctx = EasyApplication.getContext();
            TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {

                imei = telephonyManager.getDeviceId();
                if (TextUtils.isEmpty(imei))
                    imei = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);

                if (imei == null)
                    imei = "";
            }

        } catch (Exception e) {

            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }

        return imei;
    }
}
