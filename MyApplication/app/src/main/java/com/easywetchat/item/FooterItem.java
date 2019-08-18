package com.easywetchat.item;

import com.easywetchat.R;

public class FooterItem extends BaseItem {

    @Override
    public int getLayout() {
        return R.layout.multitype_adapter_item_footer;
    }

    public FooterItem setState(int state) {
        this.state = state;
        return this;
    }

    /**
     * FooterItem has 3 states: Loading, Error, NoMore
     */
    public final static int LOADING = 0;
    public final static int ERROR = 1;
    public final static int NO_MORE = 2;

    private int state = LOADING;

    public boolean isLoading() {
        return state == LOADING;
    }

    public boolean isError() {
        return state == ERROR;
    }

    public boolean isNoMore() {
        return state == NO_MORE;
    }
}
