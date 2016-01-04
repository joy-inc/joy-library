package com.android.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.android.library.R;

/**
 * Created by Daisw on 15/4/8.
 */
public class NewLineLayout extends ViewGroup {

    public int horizontalSpacing;
    public int verticalSpacing;
    public int lines = Integer.MAX_VALUE;
    private int lineIndex;

    public NewLineLayout(Context context, AttributeSet attrs) {

        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NewLineLayout);
        horizontalSpacing = a.getDimensionPixelSize(R.styleable.NewLineLayout_horSpacing, 0);
        verticalSpacing = a.getDimensionPixelSize(R.styleable.NewLineLayout_verSpacing, 0);
        lines = a.getInteger(R.styleable.NewLineLayout_showLines, Integer.MAX_VALUE);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        final int viewGroupWidth = getMeasuredWidth();

        int startPosX = left + getPaddingLeft();
        int startPosY = getPaddingTop();

        final int childCount = getChildCount();
        //        int childIndex = 0;
        for (int i = 0; i < childCount; i++) {

            //            childIndex = i;

            View child = getChildAt(i);

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 如果剩余的空间不够，则移到下一行开始位置
            if (startPosX + childWidth > viewGroupWidth - getPaddingRight()) {

                startPosX = left + getPaddingLeft();
                startPosY += childHeight + verticalSpacing;

                lineIndex++;
            }
            if (lines > lineIndex) {

                // 执行childView的绘制
                child.layout(startPosX, startPosY, startPosX + childWidth, startPosY + childHeight);
            } else {

                //                removeView(child);
                //                return;
                break;
            }

            startPosX += childWidth + horizontalSpacing;

            LayoutParams lp = getLayoutParams();
            lp.height = startPosY + childHeight + getPaddingBottom();
        }
        //        for (int i = childIndex; i < childCount; i++)
        //            removeViewAt(i);
        lineIndex = 0;
    }
}