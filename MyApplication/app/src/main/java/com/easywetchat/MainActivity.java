package com.easywetchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.easywetchat.adapter.MultiTypeAdapter;
import com.easywetchat.http.HttpRequest;
import com.easywetchat.http.IDataCallback;
import com.easywetchat.item.WetChatFriendsCircleItem;
import com.easywetchat.listener.CommonRecyclerViewEventListener;
import com.easywetchat.model.FeedBean;
import com.easywetchat.view.CommonRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * main page
 */
public class MainActivity extends AppCompatActivity{

    private CommonRecyclerView commonRecyclerView;

    private List<FeedBean> localFeedBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        commonRecyclerView = findViewById(R.id.crv);
        commonRecyclerView.setPullRefresh(true);

        // todo get total data
        commonRecyclerView.setPullRefresh(true);

        commonRecyclerView.setEventListener(new CommonRecyclerViewEventListener() {
            @Override
            public void onLoadMoreData() {

            }

            @Override
            public void onRefreshData() {
                HttpRequest request = new HttpRequest();
                request.requestData("http://thoughtworks-ios.herokuapp.com/user/jsmith/tweets", new IDataCallback() {
                    @Override
                    public void httpResponseResult(List<FeedBean> beanList) {
                        localFeedBeanList = beanList;
                        List<MultiTypeAdapter.IItem> itemList = new ArrayList<>();
                        for (FeedBean feedBean : localFeedBeanList){
                            MultiTypeAdapter.IItem item = new WetChatFriendsCircleItem(feedBean);
                            itemList.add(item);
                        }
                        commonRecyclerView.addNormal(false,itemList);
                    }
                });
            }

            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onItemLongClick(int position) {

            }
        });
    }

}
