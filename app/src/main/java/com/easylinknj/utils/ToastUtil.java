package com.easylinknj.utils;

import android.widget.Toast;

import com.easylinknj.EasyApplication;

public class ToastUtil {

    private static Toast mToast;

    private static void initToast() {

        if (mToast == null)
            mToast = Toast.makeText(EasyApplication.getContext(), "", Toast.LENGTH_SHORT);
    }

    public static void showToast(int rid) {

        try {

            initToast();
            mToast.setText(rid);
            mToast.show();
        } catch (Throwable t) {
        }
    }

    public static void showToast(String text) {

        try {

            initToast();
            mToast.setText(text);
            mToast.show();
        } catch (Throwable t) {
        }
    }

    public static void showToast(int rid, Object... args) {

        showToast(EasyApplication.getContext().getResources().getString(rid, args));
    }

    public static void release() {

        mToast = null;
    }
}
