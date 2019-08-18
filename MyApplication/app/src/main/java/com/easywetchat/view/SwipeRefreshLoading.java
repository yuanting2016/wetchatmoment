package com.easywetchat.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.easywetchat.R;

public class SwipeRefreshLoading extends SwipeRefreshLayout {

    public SwipeRefreshLoading(Context context) {
        super(context, null);
    }

    public SwipeRefreshLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeResources(R.color.colorGreen);
    }

    private boolean mMeasured = false;
    private boolean mPreMeasureRefreshing = false;

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!mMeasured) {
            mMeasured = true;
            setRefreshing(mPreMeasureRefreshing);
        }
    }


    @Override
    public void setRefreshing(boolean refreshing) {


        if (mMeasured) {
            super.setRefreshing(refreshing);
        } else {
            mPreMeasureRefreshing = refreshing;
        }
    }
}