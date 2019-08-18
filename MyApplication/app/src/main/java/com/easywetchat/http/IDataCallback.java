package com.easywetchat.http;

import com.easywetchat.model.FeedBean;

import java.util.List;

public interface IDataCallback {

    void httpResponseResult(List<FeedBean> beanList);

}
