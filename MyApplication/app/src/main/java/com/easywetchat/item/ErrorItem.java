package com.easywetchat.item;

import android.databinding.ViewDataBinding;
import android.support.annotation.ColorInt;

import com.easywetchat.R;
import com.easywetchat.databinding.MultitypeAdapterItemErrorBinding;

public class ErrorItem extends BaseItem {

    public String txt = "从服务器获取信息失败";
    @ColorInt
    private int emptyTextColor = 0xFF000000;
    @ColorInt
    private int errorTextColor = 0xFF000000;

    public ErrorItem() {
    }

    public ErrorItem(String txt) {
        this.txt = txt;
    }

    public ErrorItem(String txt, @ColorInt int emptyTextColor) {
        this.txt = txt;
        this.emptyTextColor = emptyTextColor;
    }

    public ErrorItem(@ColorInt int errorTextColor) {
        this.errorTextColor = errorTextColor;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
        super.onBinding(binding);
        if (getViewDataBinding() instanceof MultitypeAdapterItemErrorBinding) {
            MultitypeAdapterItemErrorBinding errorBinding = getViewDataBinding();
            errorBinding.emptyText.setTextColor(emptyTextColor);
            errorBinding.errorText.setTextColor(errorTextColor);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.multitype_adapter_item_error;
    }

    public ErrorItem setState(int state) {
        this.state = state;
        return this;
    }

    /**
     * ErrorItem has 2 states: Error, Empty
     */
    public final static int ERROR = 0;
    public final static int EMPTY = 1;

    private int state = ERROR;

    public boolean isError() {
        return state == ERROR;
    }

    public boolean isEmpty() {
        return state == EMPTY;
    }
}
