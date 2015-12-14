package com.joy.library.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.library.widget.JDialog;
import com.joy.library.R;

/**
 * 对话框的显示,
 * User: liulongzhenhai(longzhenhai.liu@qyer.com)
 * Date: 2015-12-11
 */
public class DialogUtil {


    public static AlertDialog getOkCancelDialog(Context context, int okStr, int cancelStr, String content, final DialogInterface.OnClickListener dialogClick) {
        JDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.dialog_base, null);

        TextView body = (TextView) view.findViewById(R.id.tvBody);
        body.setText(content);

        TextView cancel = (TextView) view.findViewById(R.id.tvCancle);
        cancel.setText(cancelStr);

        TextView ok = (TextView) view.findViewById(R.id.tvOk);
        ok.setText(okStr);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogClick.onClick(null, 1);
            }
        };

        cancel.setOnClickListener(clickListener);
        ok.setOnClickListener(clickListener);
        builder.setView(view);
        return builder.create();
    }
}
