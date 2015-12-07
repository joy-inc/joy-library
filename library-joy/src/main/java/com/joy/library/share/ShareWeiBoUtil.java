package com.joy.library.share;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.android.library.BaseApplication;
import com.android.library.utils.DeviceUtil;

import java.util.List;

/**
 * 打开微博客户端
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-12-06
 */
public class ShareWeiBoUtil {

    public static void openSinaClient(Context context, String content, String... images) {
        Intent weiboIntent = new Intent(Intent.ACTION_SEND);
        if (images == null || images.length <= 0) {
            weiboIntent.setType("text/plain");

        } else {
            weiboIntent.setType("image/*");
            weiboIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(images[0]));//SD卡下图片的绝对路径

        }
        weiboIntent.putExtra(Intent.EXTRA_TEXT, content);


        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> matches = pm.queryIntentActivities(weiboIntent, PackageManager.MATCH_DEFAULT_ONLY);
        String packageName = "com.sina.weibo";
        ResolveInfo info = null;
        for (ResolveInfo each : matches) {
            String pkgName = each.activityInfo.applicationInfo.packageName;
            if (packageName.equals(pkgName)) {
                info = each;
                break;
            }
        }
        weiboIntent.setClassName(packageName, info.activityInfo.name);
        context.startActivity(weiboIntent);
    }

    /**
     * 是否有微博客户端
     *
     * @return
     */
    public static boolean hasSinaWeiboClient() {
        return DeviceUtil.checkAppHas("com.sina.weibo");
    }
}
