package com.android.library.widget;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * 全局统一的Dialog
 * Created by KEVIN.DAI on 15/11/19.
 */
public class JDialog extends AlertDialog {

    public JDialog(Context context) {

        super(context);
    }

    public JDialog(Context context, int theme) {

        super(context, theme);
    }
}
