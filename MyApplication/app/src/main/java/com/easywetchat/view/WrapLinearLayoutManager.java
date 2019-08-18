package com.easywetchat.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * a user-defined linearlayoutmanager
 * has not added other function for a moment
 */
public class WrapLinearLayoutManager extends LinearLayoutManager {
    private static final String TAG = "WrapLinearLayoutManager";

    public WrapLinearLayoutManager(Context context) {
        super(context);
    }

    public WrapLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public WrapLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int re = -1;
        try {
            re = super.scrollVerticallyBy(dy, recycler, state);
        } catch (Exception e) {
        }
        return re;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int re = -1;
        try {
            re = super.scrollVerticallyBy(dx, recycler, state);
        } catch (Exception e) {
        }

        return re;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);

        } catch (Exception e) {
            Log.e(TAG, "on layoutchildren error", e);
        }
    }
}
