package com.joy.library.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.joy.library.BaseApplication;

import java.lang.reflect.Field;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public class DeviceUtil {

    public static String getIMEI() {

        String imei = "";

        try {

            Context ctx = BaseApplication.getContext();
            TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {

                imei = telephonyManager.getDeviceId();
                if (TextUtils.isEmpty(imei))
                    imei = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);

                if (imei == null)
                    imei = "";
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return imei;
    }

    /**
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight() {

        int height = 0;

        try {

            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int id = Integer.parseInt(field.get(obj).toString());
            height = BaseApplication.getContext().getResources().getDimensionPixelSize(id);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return height;
    }

    public static int getScreenWidth() {

        return BaseApplication.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {

        return BaseApplication.getContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean isNetworkEnable() {

        ConnectivityManager conManager = (ConnectivityManager) BaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    public static boolean isNetworkDisable() {

        ConnectivityManager conManager = (ConnectivityManager) BaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo == null || !networkInfo.isAvailable();
    }

    public static boolean isLollipopOrUpper() {

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isLollipopLower() {

        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;
    }
}
