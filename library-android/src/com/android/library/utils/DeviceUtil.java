package com.android.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.library.BaseApplication;

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

        return !isNetworkEnable();
    }

    public static boolean sdcardIsEnable() {

        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                && !Environment.getExternalStorageState().equals(Environment.MEDIA_SHARED);
    }

    /**
     * 检查app是否有安装
     * @param packageName
     * @return
     */
    public static boolean checkAppHas(String packageName) {
        try {

            PackageInfo packageInfo = BaseApplication.getContext().getPackageManager().getPackageInfo(packageName, 0);
            if (packageInfo == null)
                return false;

            int highBit = packageInfo.versionName.charAt(0);
            return highBit > 50 ? true : false;// 50 = 2

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}