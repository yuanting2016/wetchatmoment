package com.easywetchat.http;

import com.easywetchat.model.FeedBean;
import com.easywetchat.util.FastJsonUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpRequest {

    public void requestData(String url, final IDataCallback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                     String str = response.body().string();
                    List<FeedBean> result = FastJsonUtils.getArray(str,FeedBean.class);
                    callback.httpResponseResult(result);
                }

            }
        });
    }
}
