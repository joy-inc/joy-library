package com.android.library.view;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.android.library.utils.MathUtil;
import com.android.library.utils.TextUtil;

/**
 * Created by Daisw on 15/4/15.
 */
public class FoldTextView2 extends TextView {

    private static final boolean DEFAULT_UNFOLDED = false;// 默认为不展开
    private static final int DEFAULT_MIN_LINES = 5;
    private static final long DEFAULT_ANIMATION_DURATION = 200;

    private boolean mIsUnfolded = DEFAULT_UNFOLDED;
    private int mMinLines = DEFAULT_MIN_LINES;
    private long mDuration = DEFAULT_ANIMATION_DURATION;

    private float mUnfoldHeight, mFoldHeight;
    private boolean mIsEnable = true;
    private boolean mInitialized, mAnimating;

    public FoldTextView2(Context context) {

        super(context);
    }

    public FoldTextView2(Context context, AttributeSet attrs) {

        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (TextUtil.isEmpty(getText()))
            return;

        if (!mInitialized) {

            mInitialized = true;

            int lineCount = getLineCount();
            if (lineCount < mMinLines)
                mMinLines = lineCount;

            mIsEnable = lineCount > mMinLines;

            Layout layout = getLayout();
            mUnfoldHeight = layout.getLineBottom(lineCount - 1);
            mFoldHeight = layout.getLineBottom(mMinLines - 1);

            changeExpanderHeight(mFoldHeight);
        }
    }

    public void toggle() {

        toggle(true);
    }

    public void toggle(boolean animated) {

        if (mIsUnfolded) {

            fold(animated);
        } else {

            unfold(animated);
        }
    }

    public void unfold(boolean animated) {

        if (mAnimating)
            return;

        if (animated) {

            startAnimation(new ExpandAnimation());
        }
        mIsUnfolded = true;
    }

    public void fold(boolean animated) {

        if (mAnimating)
            return;

        if (animated) {

            startAnimation(new ExpandAnimation());
        }
        mIsUnfolded = false;
    }

    public boolean isEnable() {

        return mIsEnable;
    }

    private void changeExpanderHeight(float height) {

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = MathUtil.round(height);
        setLayoutParams(params);
    }

    private class ExpandAnimation extends Animation {

        private final float mStartHeight, mDistance;

        public ExpandAnimation() {

            super();

            float endHeight;
            if (mIsUnfolded) {

                mStartHeight = mUnfoldHeight;
                endHeight = mFoldHeight;
            } else {

                mStartHeight = mFoldHeight;
                endHeight = mUnfoldHeight;
            }
            mDistance = endHeight - mStartHeight;
            setDuration(mDuration);
            setInterpolator(getInterpolator());
            setAnimationListener(new ExpandAnimationListener());
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            changeExpanderHeight(mDistance * interpolatedTime + mStartHeight);
        }
    }

    private class ExpandAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

            mAnimating = true;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            mAnimating = false;

            if (mOnFoldListener != null)
                mOnFoldListener.onFold(mIsUnfolded);
        }
    }

    public interface OnFoldListener {

        void onFold(boolean isUnfold);
    }

    private OnFoldListener mOnFoldListener;

    public void setOnFoldListener(OnFoldListener lisn) {

        mOnFoldListener = lisn;
    }
}