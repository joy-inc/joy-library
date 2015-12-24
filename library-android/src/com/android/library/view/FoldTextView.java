package com.android.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.android.library.R;
import com.android.library.utils.DensityUtil;
import com.android.library.utils.MathUtil;
import com.android.library.utils.TextUtil;

import java.util.ArrayList;

/**
 * Created by Daisw on 15/4/15.
 */
public class FoldTextView extends TextView {

    private static final int DEFAULT_LINE_SPACING = DensityUtil.dip2px(3);
    private static final boolean DEFAULT_UNFOLDED = false;// 默认为不展开
    private static final int DEFAULT_MIN_LINES = 5;
    private static final long DEFAULT_ANIMATION_DURATION = 200;
    private static final String DEFAULT_ELLIPSIZE = "···";

    private boolean mIsUnfolded = DEFAULT_UNFOLDED;
    private boolean mIsFolded = !mIsUnfolded;
    private int mMinLines = DEFAULT_MIN_LINES;

    private TextPaint mNormalPaint, mPromptPaint;
    private String mPromptText, mCollapseText;

    private float mUnfoldHeight, mFoldHeight;

    private boolean mIsFoldEnable = true;
    private boolean mInitialized = false;
    private boolean mAnimating;

    private long mDuration = DEFAULT_ANIMATION_DURATION;
    private Interpolator mInterpolator;

    private int mWidth;

    public FoldTextView(Context context) {

        super(context);
        init(context, null);
    }

    public FoldTextView(Context context, AttributeSet attrs) {

        super(context, attrs);
        init(context, attrs);
    }

    public FoldTextView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FoldTextView);
        mIsUnfolded = typedArray.getBoolean(R.styleable.FoldTextView_unfolded, DEFAULT_UNFOLDED);
        mMinLines = typedArray.getInteger(R.styleable.FoldTextView_minLines, DEFAULT_MIN_LINES);
        mPromptText = typedArray.getString(R.styleable.FoldTextView_promptText);
        int promptColor = typedArray.getColor(R.styleable.FoldTextView_promptColor, getCurrentTextColor());
        mCollapseText = typedArray.getString(R.styleable.FoldTextView_promptCollapseText);
        typedArray.recycle();

        mNormalPaint = getPaint();
        mNormalPaint.setColor(getCurrentTextColor());
        mNormalPaint.drawableState = getDrawableState();

        mPromptPaint = new TextPaint(mNormalPaint);
        mPromptPaint.setColor(promptColor);

        mIsFolded = !mIsUnfolded;

        float lineSpacingExtra = getLineSpacingExtra();
        setLineSpacing(lineSpacingExtra == 0f ? DEFAULT_LINE_SPACING : lineSpacingExtra + DEFAULT_LINE_SPACING, 1.0f);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        return mIsFoldEnable && super.onTouchEvent(event);
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

            mIsFoldEnable = mIsFoldEnable && lineCount > mMinLines;

            mLineHeight = getLineHeight();
            int totalPadding = getTotalPaddingTop() + getTotalPaddingBottom();
            mUnfoldHeight = mLineHeight * (TextUtil.isNotEmpty(mCollapseText) ? lineCount + 1 : lineCount) + totalPadding;
            mFoldHeight = mLineHeight * mMinLines + totalPadding;

            changeExpanderHeight(mIsUnfolded ? mUnfoldHeight : mFoldHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);

        if (mWidth == 0)
            mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {

        String content = text.toString().trim();

        if (!TextUtil.isEmpty(content)) {

            mInitialized = false;

            if (mUnfoldTexts != null)
                mUnfoldTexts.clear();
            if (mFoldTexts != null)
                mFoldTexts.clear();
        }

        super.setText(Html.fromHtml(content), type);
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

    public void unfold() {

        unfold(true);
    }

    public void unfold(boolean animated) {

        if (mAnimating)
            return;

        if (animated) {

            mAnimating = true;
            startAnimation(new ExpandAnimation());
        }
        mIsUnfolded = true;
    }

    public void fold() {

        fold(true);
    }

    public void fold(boolean animated) {

        if (mAnimating)
            return;

        if (animated) {

            mAnimating = true;
            startAnimation(new ExpandAnimation());
        }
        mIsUnfolded = false;
    }

    /**
     * If you use animation and the animation is not finished, it will return the previous state.
     */
    public boolean isUnfolded() {

        return mIsUnfolded;
    }

    /**
     * When you use this method in ListView with ViewHolder pattern, set it when convertView is not null.
     */
    public void setUnfolded(boolean isUnfolded) {

        mIsUnfolded = isUnfolded;
        mIsFolded = !isUnfolded;
        mAnimating = false;
    }

    public boolean isAnimating() {

        return mAnimating;
    }

    public void setDuration(long durationMillis) {

        if (durationMillis < 0)
            throw new IllegalArgumentException("Animation duration cannot be negative");
        mDuration = durationMillis;
    }

    public void setInterpolator(Context context, int resID) {

        setInterpolator(AnimationUtils.loadInterpolator(context, resID));
    }

    /**
     * Sets the acceleration curve for this animation. Defaults to a linear
     * interpolation.
     */
    public void setInterpolator(Interpolator i) {

        mInterpolator = i;
    }

    /**
     * the default is true.
     * 设置展开后是否可以关闭
     */
    public void setFoldEnable(boolean enable) {

        mIsFoldEnable = enable;
    }

    public boolean isEnable() {

        return mIsFoldEnable;
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
            setInterpolator(mInterpolator == null ? getInterpolator() : mInterpolator);
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

            if (mIsUnfolded)
                mIsFolded = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            mAnimating = false;

            if (!mIsUnfolded)
                mIsFolded = true;

            toggleOnFoldListener(mIsFolded);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {

        final int compoundPaddingLeft = getCompoundPaddingLeft();
        final int compoundPaddingTop = getCompoundPaddingTop();
        final int compoundPaddingRight = getCompoundPaddingRight();
        final int compoundPaddingBottom = getCompoundPaddingBottom();
        final int scrollX = getScrollX();
        final int scrollY = getScrollY();
        final int right = getRight();
        final int left = getLeft();
        final int bottom = getBottom();
        final int top = getTop();

        final int extendedPaddingTop = getExtendedPaddingTop();
        final int extendedPaddingBottom = getExtendedPaddingBottom();

        final int vSpace = bottom - top - compoundPaddingBottom - compoundPaddingTop;
        final int maxScrollY = getLayout().getHeight() - vSpace;

        final float clipLeft = compoundPaddingLeft + scrollX;
        final float clipTop = (scrollY == 0) ? 0 : extendedPaddingTop + scrollY;
        final float clipRight = right - left - compoundPaddingRight + scrollX;
        final float clipBottom = bottom - top + scrollY - ((scrollY == maxScrollY) ? 0 : extendedPaddingBottom);

        canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom);
        canvas.translate(compoundPaddingLeft, getTotalPaddingTop());
        canvas.save();

        if (mIsFolded) {// 折叠状态

            initTextInfo(mFoldTexts, mNormalPaint);

            for (int i = 0; i < mFoldTexts.size(); i++) {

                float y = (i + 1) * mLineHeight - mExtraHeight;
                canvas.drawText(mFoldTexts.get(i), 0f, y, mNormalPaint);
                if (mIsFoldEnable && TextUtil.isNotEmpty(mPromptText) && i == mFoldTexts.size() - 1) {

                    float x = mWidth - getTextWidth(mPromptText, mPromptPaint);
                    canvas.drawText(mPromptText, x, y, mPromptPaint);
                }
            }
        } else {// 非折叠状态

            initTextInfo(mUnfoldTexts, mNormalPaint);

            for (int i = 0; i < mUnfoldTexts.size(); i++) {

                float y = (i + 1) * mLineHeight - mExtraHeight;
                canvas.drawText(mUnfoldTexts.get(i), 0f, y, mNormalPaint);
                if (TextUtil.isNotEmpty(mCollapseText) && i == mUnfoldTexts.size() - 1) {

                    float x = mWidth - getTextWidth(mCollapseText, mPromptPaint);
                    canvas.drawText(mCollapseText, x, y + mLineHeight + getLineSpacingExtra() - mExtraHeight * 2, mPromptPaint);
                }
            }
        }
    }

    public int getLineHeight() {

        Paint.FontMetricsInt fontMetricsInt = getPaint().getFontMetricsInt();
        mExtraHeight = Math.abs(fontMetricsInt.top) - Math.abs(fontMetricsInt.ascent) - (Math.abs(fontMetricsInt.bottom) - Math.abs(fontMetricsInt.descent)) * 3;
        mExtraHeight += getLineSpacingExtra() / 2;

        return MathUtil.round(Math.abs(fontMetricsInt.ascent) + getLineSpacingExtra());
    }

    public float getLineSpacingExtra() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            return super.getLineSpacingExtra();
        } else {

            return 0.0f;
        }
    }

    private ArrayList<String> mUnfoldTexts, mFoldTexts;
    private int mLineHeight, mExtraHeight;

    private void initTextInfo(ArrayList<String> container, Paint paint) {

        if (container == null || !container.isEmpty())// 如果已经填充过数据，则return
            return;

        if (TextUtil.isEmpty(getText()) || paint == null)
            return;

        final String str = getText().toString().trim();
        final int STR_LENGTH = str.length();

        final float[] strWidths = new float[STR_LENGTH];
        paint.getTextWidths(str, strWidths);

        float availableWidth = mWidth, curWidth = 0f;
        int startIndex = 0, nextIndex = 0;

        for (int i = 0; i < STR_LENGTH; i++) {

            curWidth += strWidths[i];

            if (mIsFoldEnable && mIsFolded && container.size() + 1 == mMinLines && startIndex == nextIndex) {

                curWidth += DensityUtil.dip2px(16) + getTextWidth(mPromptText, paint);
            }

            nextIndex = i + 1;

            if (curWidth == availableWidth || (curWidth < availableWidth && nextIndex < STR_LENGTH && curWidth + strWidths[nextIndex] > availableWidth)) {

                availableWidth = curWidth + mWidth;
                container.add(str.substring(startIndex, nextIndex));

                if (mIsFolded && container.size() == mMinLines) {

                    container.set(mMinLines - 1, container.get(mMinLines - 1) + DEFAULT_ELLIPSIZE);
                    break;
                }

                startIndex = nextIndex;
            } else if (i == STR_LENGTH - 1) {

                container.add(str.substring(startIndex));
            }
        }
    }

    private int getTextWidth(String text, Paint paint) {

        if (TextUtil.isEmpty(text))
            return 0;

        float[] widths = new float[text.length()];
        paint.getTextWidths(text, widths);
        float width = 0;

        for (float w : widths)
            width += w;

        return MathUtil.round(width);
    }

    //    private float getTextHeight(Paint paint) {
    //
    //        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
    //        return Math.abs(fontMetrics.ascent);
    //    }

    @Override
    protected void onAttachedToWindow() {

        super.onAttachedToWindow();

        mUnfoldTexts = new ArrayList<>();
        mFoldTexts = new ArrayList<>();
    }

    @Override
    protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();

        if (mUnfoldTexts != null) {

            mUnfoldTexts.clear();
            mUnfoldTexts = null;
        }
        if (mFoldTexts != null) {

            mFoldTexts.clear();
            mFoldTexts = null;
        }
    }

    public interface OnFoldListener {

        void onFold(boolean isFold);
    }

    private OnFoldListener mOnFoldListener;

    public void setOnFoldListener(OnFoldListener lisn) {

        mOnFoldListener = lisn;
    }

    private void toggleOnFoldListener(boolean isFold) {

        if (mOnFoldListener != null)
            mOnFoldListener.onFold(isFold);
    }
}