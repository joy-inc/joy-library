package com.android.library.view.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;

import com.android.library.R;
import com.android.library.utils.DimenCons;

/**
 * Created by Daisw on 16/8/4.
 */

public class JBottomSheetDialog extends BottomSheetDialog implements DimenCons {

    public JBottomSheetDialog(@NonNull Context context) {

        this(context, R.style.base_light_BottomSheetDialog);
    }

    public JBottomSheetDialog(@NonNull Context context, @StyleRes int theme) {

        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setLayout(SCREEN_WIDTH, SCREEN_HEIGHT - STATUS_BAR_HEIGHT);
    }
}
