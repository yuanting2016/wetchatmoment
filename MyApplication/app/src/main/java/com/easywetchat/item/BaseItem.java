package com.easywetchat.item;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.easywetchat.adapter.MultiTypeAdapter;

public abstract class BaseItem implements MultiTypeAdapter.IItem {

    private ViewDataBinding viewDataBinding;

    private MultiTypeAdapter adapter;

    @Override
    public int getVariableId() {
        return BR.item;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
        if (viewDataBinding == null) {
            viewDataBinding = binding;
        }
    }

    @Override
    public void onBinding(ViewDataBinding binding, int position, Object payload) {
        if (viewDataBinding == null) {
            viewDataBinding = binding;
        }
    }

    @Override
    public void unBinding() {
        viewDataBinding = null;
        recycle();
    }

    public <T extends ViewDataBinding> T getViewDataBinding() {
        if (viewDataBinding == null) {
            return null;
        } else {
            return (T) viewDataBinding;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onViewAttachedToWindow(MultiTypeAdapter.ItemViewHolder holder) {
        viewDataBinding = holder.getBinding();
    }

    @Override
    public void onViewDetachedFromWindow(MultiTypeAdapter.ItemViewHolder holder) {
        viewDataBinding = null;
        recycle();
    }

    @Override
    public void attachAdapter(MultiTypeAdapter adapter) {
        this.adapter = adapter;
    }

    protected MultiTypeAdapter getAdapter() {
        return adapter;
    }

    @Override
    public boolean doOnScreenShot() {
        return true;
    }

    @Override
    public MultiTypeAdapter.IItem clone() {
        return this;
    }

    /**
     * item event
     */
    private View.OnClickListener onClickListener;

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setVisibility(boolean isVisible) {
        View itemView = getViewDataBinding().getRoot();
        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        if (isVisible) {
            param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            param.width = LinearLayout.LayoutParams.MATCH_PARENT;
            itemView.setVisibility(View.VISIBLE);
        } else {
            itemView.setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;
        }
        itemView.setLayoutParams(param);
    }

    protected void recycle() {
    }
}
