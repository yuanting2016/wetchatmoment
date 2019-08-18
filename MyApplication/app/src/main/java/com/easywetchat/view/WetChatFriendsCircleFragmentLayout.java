package com.easywetchat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class WetChatFriendsCircleFragmentLayout extends ViewGroup {
    /**
     * show column
     */
    private int mColumnCount;

    private final float DEFAULT_SPACING = 2.5f;

    private float mSpacing;
    /**
     * picture wide:high
     */
    private float mItemAspectRatio;
    /**
     * 最宽的时候相对可用空间比例（当childCount=1的时候）
     * 当只有一张图片的 最大显示宽度和高度相对于可用宽的的比例
     */
    private final float MAX_WIDTH_PERCENTAGE = 270f / 350;

    private int mItemWidth;
    private int mItemHeight;


    public WetChatFriendsCircleFragmentLayout(Context context) {
        this(context, null);
    }

    public WetChatFriendsCircleFragmentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WetChatFriendsCircleFragmentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSpacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_SPACING,
                context.getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int count = getChildCount();
        final int width = MeasureSpec.getSize(widthMeasureSpec);

        //当只有一张图片的时候 显示一张相对比较大的图片
        //当大于1小于等于4张图片的时候 一排显示两张图片

        if (count == 1) {
            mColumnCount = 1;

            int mItemMaxWidth = (int) (width * MAX_WIDTH_PERCENTAGE);
            int mItemMaxHeight = mItemMaxWidth;
            if (mItemAspectRatio < 1) {
                mItemHeight = mItemMaxHeight;
                mItemWidth = (int) (mItemHeight * mItemAspectRatio);
            } else {
                mItemWidth = mItemMaxWidth;
                mItemHeight = (int) (mItemMaxWidth / mItemAspectRatio);
            }
        } else {
            if (count <= 4) {
                mColumnCount = 2;
            } else {
                mColumnCount = 3;
            }
            mItemWidth = (int) ((width - getPaddingLeft() - getPaddingRight() - 2 * mSpacing) / 3);
            mItemHeight = (int) (mItemWidth / mItemAspectRatio);
        }


        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = mItemWidth;
            layoutParams.height = mItemHeight;
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    getDesiredHeight(mItemHeight), MeasureSpec.EXACTLY);
        }

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(
                    getDesiredWidth(mItemWidth), MeasureSpec.EXACTLY), heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec,
                                int parentHeightMeasureSpec) {
        final LayoutParams lp = child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight(), lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                getPaddingLeft() + getPaddingRight(), lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    private int getDesiredHeight(int mItemHeight) {
        int totalHeight = getPaddingTop() + getPaddingBottom();
        int count = getChildCount();
        if (count > 0) {
            int row = (count - 1) / mColumnCount;
            totalHeight = (int) ((row + 1) * mItemHeight + (row) * mSpacing) + totalHeight;
        }
        return totalHeight;
    }

    private int getDesiredWidth(int mItemWidth) {
        int totalWidth = getPaddingLeft() + getPaddingRight();
        int count = getChildCount();
        if (count > 0) {
            if (count < mColumnCount) {
                totalWidth = (int) (count * mItemWidth + (count - 1) * mSpacing) + totalWidth;
            } else {
                totalWidth = (int) (count * mItemWidth + (count - 1) * mSpacing) + totalWidth;
            }

        }
        return totalWidth;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View imageView = getChildAt(i);

            int column = i % mColumnCount;
            int row = i / mColumnCount;
            int left = (int) (getPaddingLeft() + column * (mSpacing + mItemWidth));
            int top = (int) (getPaddingTop() + row * (mSpacing + mItemHeight));

            imageView.layout(left, top, left + mItemWidth, top + mItemHeight);
        }
    }


    /**
     * 显示图片
     */
    public void setImageUrls(final List<String> imageUrls) {
        removeAllViews();
        if (imageUrls.isEmpty()) {
            return;
        }

        int count = imageUrls.size();
        if (count == 1) {
            //一般在url中会有宽高 可以算出宽高比
            //测试url固定用的 1000:1376
//            mItemAspectRatio = AppUtil.getAspectRatio(imageUrls.get(0));
            mItemAspectRatio = 1000/1376f;
        } else {
            mItemAspectRatio = 1;
        }

        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            //todo imageView.setImageURI(imageUrls.get(i));
            addView(imageView);

        }
    }


    public int getItemWidth() {
        return mItemWidth;
    }

    public int getItemHeight() {
        return mItemHeight;
    }

    public int getColumnCount() {
        return mColumnCount;
    }

    public void setColumnCount(int columnCount) {
        mColumnCount = columnCount;
        invalidate();
    }

    public float getSpacing() {
        return mSpacing;
    }

    public void setSpacing(float spacing) {
        mSpacing = spacing;
        invalidate();
    }

    public float getItemAspectRatio() {
        return mItemAspectRatio;
    }

    public void setItemAspectRatio(float itemAspectRatio) {
        mItemAspectRatio = itemAspectRatio;
    }
}