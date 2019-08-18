package com.easywetchat.view;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.easywetchat.R;
import com.easywetchat.adapter.MultiTypeAdapter;
import com.easywetchat.item.ErrorItem;
import com.easywetchat.item.FooterItem;
import com.easywetchat.item.HeaderItem;
import com.easywetchat.listener.CommonRecyclerViewEventListener;

import java.util.List;

/**
 * contains refresh load-more and more actions
 */
public class CommonRecyclerView extends SwipeRefreshLoading {

    public static final int ERROR = 0;

    public static final int EMPTY = 1;

    public static final int LAST = 2;

    public static final int NORMAL = 3;

    private RecyclerView recyclerView;

    private ProgressBar progressBar;

    private MultiTypeAdapter adapter;
    // header
    private HeaderItem headerItem;

    private ErrorItem errorItem;
    // footer
    private FooterItem footerItem;

    private boolean pullRefresh;

    private boolean refreshing;

    private boolean loading;

    private boolean hasHeader;

    private boolean hasFooter;

    private boolean hasError;

    RecyclerView.LayoutManager layoutManager;

    public CommonRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public CommonRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.multitype_adapter_content, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.addView(view);

        recyclerView.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);

        initAdapter(context);
        initStateItem();
        recyclerView.setHasFixedSize(true);

        //default set is LinearLayoutManager
        if (layoutManager == null) {
            layoutManager = new WrapLinearLayoutManager(context);
        }
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE && layoutManager instanceof LinearLayoutManager && ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition() >= adapter.getItemCount() - 1) {
                    loadMoreData();
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && layoutManager instanceof GridLayoutManager && ((GridLayoutManager) layoutManager).findLastVisibleItemPosition() >= adapter.getItemCount() - 1) {
                    loadMoreData();
                }

            }
        });
        recyclerView.setAdapter(adapter);

        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        //default has no pull-refresh
        setEnabled(pullRefresh);
    }

    public void initAdapter(Context context) {
        adapter = new MultiTypeAdapter(context);
        adapter.setOnItemClickListener(new MultiTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (eventListener != null) {
                    // only there is no error，can click header item
                    if (!hasError) {
                        eventListener.onItemClick(position);
                    } else if (hasHeader && position == 0) {
                        eventListener.onItemClick(position);
                    }
                }
            }
        });
    }

    /**
     * init items state
     */
    public void initStateItem() {
        footerItem = new FooterItem();
        errorItem = new ErrorItem();

        footerItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMoreData();
            }
        });

        errorItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshData();
            }
        });
    }

    /**
     * add item state and data
     * has three conditions：error data，empty data，right result。
     * @param resultType
     * @param loadMore
     * @param datas
     */
    public void addItems(int resultType, boolean loadMore, List<MultiTypeAdapter.IItem> datas) {
        if (!loadMore) {
            adapter.clearItems();
            if (hasHeader) {
                adapter.addItem(headerItem);
            }
        }
        if (resultType == ERROR) {
            Log.d("commonRecycleView", "ERROR");
            if (loadMore) {
                adapter.addItem(footerItem.setState(FooterItem.ERROR));
            } else {
                hasError = true;
                adapter.addItem(errorItem.setState(ErrorItem.ERROR));
            }
        } else if (resultType == EMPTY) {
            Log.d("commonRecycleView", "EMPTY");
            if (loadMore) {
                adapter.addItem(footerItem.setState(FooterItem.NO_MORE));
            } else {
                hasError = true;
                adapter.addItem(errorItem.setState(ErrorItem.EMPTY));
            }
        } else if (resultType == LAST) {
            Log.d("commonRecycleView", "LAST");
            if (hasError) {
                adapter.removeItem(errorItem);
            }
            hasError = false;
            addDataItems(datas);
            if (hasFooter) {
                adapter.addItem(footerItem.setState(FooterItem.NO_MORE));
            }
        } else if (resultType == NORMAL) {
            Log.d("commonRecycleView", "NORMAL");
            if (hasError) {
                adapter.removeItem(errorItem);
            }
            hasError = false;
            addDataItems(datas);
            if (hasFooter) {
                adapter.addItem(footerItem.setState(FooterItem.LOADING));
            }
        }

        if (loadMore) {
            loadMoreDataOver();
        } else {
            refreshDataOver();
        }
    }

    /**
     * add normal
     *
     * @param loadMore
     * @param datas
     */
    public void addNormal(boolean loadMore, List<MultiTypeAdapter.IItem> datas) {
        addItems(NORMAL, loadMore, datas);
    }

    /**
     * add error
     *
     * @param loadMore
     */
    public void addError(boolean loadMore) {
        addItems(ERROR, loadMore, null);
    }

    /**
     * add empty
     *
     * @param loadMore
     */
    public void addEmpty(boolean loadMore) {
        addItems(EMPTY, loadMore, null);
    }

    /**
     * add dataitem
     *
     * @param datas
     */
    private void addDataItems(List<MultiTypeAdapter.IItem> datas) {
        adapter.addItems(datas);
    }

    /**
     * add specifiled position item
     *
     * @param position
     * @param item
     */
    public void addItem(int position, MultiTypeAdapter.IItem item) {
        if (hasError) {
            adapter.removeItem(errorItem);
        }
        hasError = false;
        adapter.addItem(item, position);
        adapter.notifyDataSetChanged();
    }

    /**
     * refresh specifiled position item
     * @param position
     * @param item
     */
    public void refreshItem(int position, MultiTypeAdapter.IItem item) {
        adapter.setItem(item, position);
        adapter.notifyItemChanged(position);
    }

    /**
     * remove specifiled position item
     * @param position
     */
    public void removeItem(int position) {
        adapter.removeItem(position);
        adapter.notifyItemRemoved(position);
    }

    /**
     * load more data
     */
    private void loadMoreData() {
        int footerPos = adapter.findPos(footerItem);
        if (!refreshing && !loading && footerPos >= 0 && !footerItem.isNoMore()) {
            Log.d("commonRecycleView", "loadMoreData");
            if (!footerItem.isLoading()) {
                footerItem.setState(FooterItem.LOADING);
                adapter.notifyItemChanged(footerPos);
            }
            loading = true;
            // forbid load data when refresh data
            setEnabled(false);
            if (eventListener != null) {
                eventListener.onLoadMoreData();
            }
        }
    }

    /**
     * load data over
     */
    private void loadMoreDataOver() {
        Log.d("commonRecycleView", "loadMoreData");
        loading = false;
        // 加载数据结束的时候恢复下拉刷新
        setEnabled(pullRefresh);
        adapter.removeItem(footerItem);
        adapter.notifyDataSetChanged();
        showRecyclerView();
    }

    /**
     * refresh data
     */
    private void refreshData() {
        if (!loading && !refreshing) {
            setRefreshState(true);
            if (eventListener != null) {
                eventListener.onRefreshData();
            }
        }
    }

    /**
     * refresh data over
     */
    private void refreshDataOver() {
        setRefreshState(false);
        adapter.notifyDataSetChanged();
        showRecyclerView();
    }

    /**
     * show view and hide progressbar
     */
    private void showRecyclerView() {
        recyclerView.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
    }

    /**
     * show progressbar and hide view
     */
    public void showLoading() {
        recyclerView.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
    }

    /**
     * set refresh state
     * @param refreshState
     */
    private void setRefreshState(boolean refreshState) {
        refreshing = refreshState;
        if (pullRefresh) {
            setRefreshing(refreshState);
        }
    }

    /**
     * set pullrefresh or not
     *
     * @param pullRefresh
     */
    public void setPullRefresh(boolean pullRefresh) {
        this.pullRefresh = pullRefresh;
        setEnabled(pullRefresh);
    }

    /**
     * set header item
     *
     * @param headerItem
     */
    public void setHeaderItem(HeaderItem headerItem) {
        Log.d("CommonRecyclerView", "setHeaderItem");
        this.headerItem = headerItem;
        hasHeader = true;
//        recyclerView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) recyclerView.getLayoutParams();
//        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    }




    /**
     * set pullrefresh endable or not
     *
     * @param pullRefresh
     */
    public void setPullRefreshEndable(boolean pullRefresh) {
        this.pullRefresh = pullRefresh;
        setEnabled(pullRefresh);
    }

    /**
     * set error item
     *
     * @param errorItem
     */
    public void setErrorItem(ErrorItem errorItem) {
        this.errorItem = errorItem;
        errorItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshData();
            }
        });

    }






    /**
     * set user-defined header item
     *
     * @param item
     */
    public void setCustomHeaderItem(HeaderItem item) {
        this.headerItem = item;
    }

    /**
     *set user-defined header item
     *
     * @param item
     */
    public void setCustomFooterItem(FooterItem item) {
        this.footerItem = item;
    }

    /**
     * set user-defined error item
     *
     * @param errorItem
     */
    public void setCustomErrorItem(ErrorItem errorItem) {
        this.errorItem = errorItem;
    }


    /**
     * bind items list
     *
     * @param items
     */
    public void setItems(List<MultiTypeAdapter.IItem> items) {
        adapter.setItems(items);
    }


    /**
     * add items list
     *
     * @param items
     */
    public void AddItems(List<MultiTypeAdapter.IItem> items) {
        adapter.addItems(items);
    }

    /**
     * add a item
     *
     * @param item
     */
    public void AddItem(MultiTypeAdapter.IItem item) {
        adapter.addItem(item);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public MultiTypeAdapter getAdapter() {
        return adapter;
    }

    /**
     * set RecyclerView decoration
     *
     * @param itemDecoration
     */
    public void setItemDecoration(DividerItemDecoration itemDecoration) {
        recyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * set footer
     *
     * @param hasFooter
     */
    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    private CommonRecyclerViewEventListener eventListener;

    public void setEventListener(CommonRecyclerViewEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutmanager) {
        this.layoutManager = layoutmanager;
        recyclerView.setLayoutManager(layoutManager);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }
}
