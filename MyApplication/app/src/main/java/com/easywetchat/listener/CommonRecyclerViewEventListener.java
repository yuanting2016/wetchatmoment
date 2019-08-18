package com.easywetchat.listener;

/**
 * recycleview common actions
 */
public interface CommonRecyclerViewEventListener {

    /**
     * load data
     */
    void onLoadMoreData();

    /**
     * refresh data
     */
    void onRefreshData();

    /**
     * item click
     *
     */
    void onItemClick(int position);

    /**
     * item longpress
     *
     */
    void onItemLongClick(int position);

}
