/**
 * Copyright (c) 2004, qyer.com, Inc. All rights reserved. 
 */
package com.joy.library.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.InputStream;

/**
 * 包含系统基础的一些分享方法, <br>
 * 邮箱<br>
 * 短信<br>
 * 调用系统的<br>
 * @author liulongzhenhai 2015-1-21 下午3:27:18 <br>
 * @see
 */
public class ShareSystemUtil {
	private void SystemShareUtil() {

	}

	/**
	 * 调用系统的分享工具
	 * @param context Context
	 * @param content 内容
	 * @param title 标题
	 */
	public static void showSystemShare(Context context, String content, String title) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, content);
		context.startActivity(Intent.createChooser(shareIntent, title));
	}

	/**
	 * @param context
	 * @param mybody 内容
	 * @param title 标题
	 * @param drawResId 附件的图片资源ID
	 * @param choseDialogTitle "选择的标题栏"请选择"" 如果再多个邮箱的时候的标题
	 */
//	public static void sendMail(Context context, String mybody, String title, int drawResId, String choseDialogTitle) {
//		Intent myIntent = new Intent(Intent.ACTION_SEND);
//
//		myIntent.putExtra(Intent.EXTRA_SUBJECT, title);
//		myIntent.putExtra(Intent.EXTRA_TEXT, mybody);
//
//		InputStream is = context.getResources().openRawResource(drawResId);
//		File file = new File(QaStorageUtil.getQyerTempDir(), "share_image.jpeg");
//		if (DeviceUtil.sdcardIsEnable() && IOUtil.copyFileFromRaw(is, file)) {
//			myIntent.setType("application/octet-stream");
//
//			Uri path = Uri.fromFile(file);
//			myIntent.putExtra(Intent.EXTRA_STREAM, path);
//		} else {
//			myIntent.setType("text/plain");
//		}
//
//		context.startActivity(Intent.createChooser(myIntent, choseDialogTitle));
//	}


}
