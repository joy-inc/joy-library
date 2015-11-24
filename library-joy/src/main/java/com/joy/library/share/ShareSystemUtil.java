/**
 * Copyright (c) 2004, qyer.com, Inc. All rights reserved. 
 */
package com.joy.library.share;

import android.content.Context;
import android.content.Intent;

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


}
