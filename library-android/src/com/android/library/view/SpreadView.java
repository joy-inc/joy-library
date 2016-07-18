package com.android.library.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.android.library.utils.DimenCons;

import java.text.DecimalFormat;

/**
 * Created by Daisw on 16/7/16.
 */
public class SpreadView extends View {

    public static final int STATE_NOT_STARTED = 0;
    public static final int STATE_FILL_STARTED = 1;
    public static final int STATE_FILL_FINISHED = 2;
    public static final int STATE_EMPTY_STARTED = 3;
    public static final int STATE_EMPTY_FINISHED = 4;

    private static final Interpolator ACCELERATE = new AccelerateInterpolator();
    private static final Interpolator DECELERATE = new DecelerateInterpolator();
    private static final int FILL_TIME = 400;

    private int state = STATE_NOT_STARTED;

    private Paint fillPaint;
    private int currentRadius;
    ObjectAnimator spreadAnimator;

    private int startLocationX;
    private int startLocationY;
    private int diagonal;

    private OnStateChangeListener onStateChangeListener;

    public SpreadView(Context context) {

        super(context);
        init();
    }

    public SpreadView(Context context, AttributeSet attrs) {

        super(context, attrs);
        init();
    }

    public SpreadView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpreadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.WHITE);

        double d = StrictMath.pow(StrictMath.pow(DimenCons.SCREEN_WIDTH, 2d) + StrictMath.pow(DimenCons.SCREEN_HEIGHT_ABSOLUTE, 2d), 1.0 / 2);
        DecimalFormat df = new DecimalFormat("0");
        diagonal = Integer.valueOf(df.format(d));
    }

    public void setFillPaintColor(@ColorInt int color) {

        fillPaint.setColor(color);
    }

    public void startFromLocation(int[] tapLocationOnScreen) {

        changeState(STATE_FILL_STARTED);
        startLocationX = tapLocationOnScreen[0];
        startLocationY = tapLocationOnScreen[1];
        spreadAnimator = ObjectAnimator.ofInt(this, "currentRadius", 0, diagonal).setDuration(FILL_TIME);
        spreadAnimator.setInterpolator(ACCELERATE);
        spreadAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                changeState(STATE_FILL_FINISHED);
            }
        });
        spreadAnimator.start();
    }

    public void startToLocation() {

        changeState(STATE_EMPTY_STARTED);
        spreadAnimator = ObjectAnimator.ofInt(this, "currentRadius", diagonal, 0).setDuration(FILL_TIME);
        spreadAnimator.setInterpolator(DECELERATE);
        spreadAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {

                changeState(STATE_EMPTY_FINISHED);
            }
        });
        spreadAnimator.start();
    }

    public void setToFinishedFrame() {

        changeState(STATE_FILL_FINISHED);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

//        if (state == STATE_FILL_FINISHED) {
//
//            canvas.drawRect(0, 0, getWidth(), getHeight(), fillPaint);
//        } else {

        canvas.drawCircle(startLocationX, startLocationY, currentRadius, fillPaint);
//        }
    }

    private void changeState(int state) {

        if (this.state == state)
            return;

        this.state = state;
        if (onStateChangeListener != null)
            onStateChangeListener.onStateChange(state);
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {

        this.onStateChangeListener = onStateChangeListener;
    }

    public void setCurrentRadius(int radius) {

        this.currentRadius = radius;
        invalidate();
    }

    public interface OnStateChangeListener {

        void onStateChange(int state);
    }
}
