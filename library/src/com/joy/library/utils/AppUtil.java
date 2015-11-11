package com.joy.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.joy.library.BaseApplication;

/**
 * Created by KEVIN.DAI on 15/7/10.
 */
public class AppUtil {

    /**
     * 获取应用第一次安装时间
     *
     * @return
     */
    public static long getInstallAppTime() {

        long firstInstallTime = 0;

        try {

            Context ctx = BaseApplication.getContext();
            PackageInfo packageInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            firstInstallTime = packageInfo.firstInstallTime;// 应用第一次安装的时间

        } catch (Exception e) {

            e.printStackTrace();
        }

        return firstInstallTime;
    }
    /**
     * 获取VersionCode，默认返回1
     * @return
     */
    public static int getVersionCode() {

        try {

            Context ctx = BaseApplication.getContext();
            PackageInfo packInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return packInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {

//            if(LogMgr.isDebug())
                e.printStackTrace();
        }

        return 1;
    }

    public static String getVersionName() {

        try {

            Context ctx = BaseApplication.getContext();
            PackageInfo packInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return packInfo.versionName == null ? "" : packInfo.versionName;

        } catch (Exception e) {

//            if(LogMgr.isDebug())
                e.printStackTrace();
        }

        return "";
    }
}
