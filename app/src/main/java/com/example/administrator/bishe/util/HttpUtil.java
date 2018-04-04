package com.example.administrator.bishe.util;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2018/2/1 0001.
 */

public class HttpUtil {
    public static void sendHttpRequest(String address, final Map<String,String> data, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBuilder = new FormBody.Builder();
        for(String key : data.keySet())
        formBuilder.add(key,data.get(key));
        RequestBody requestBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
