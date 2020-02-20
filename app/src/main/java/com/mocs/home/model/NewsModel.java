package com.mocs.home.model;

import android.util.Log;

import com.google.gson.Gson;
import com.mocs.common.bean.News;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsModel {
    private static final String BASE_URL = "http://47.103.82.240:8080";
    private static final Gson MY_GSON =new Gson();

    /**
     * 获取新闻Banner和下方列表的内容
     */
    public static HomeData fetchNewsData() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "/news")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        Log.d("fetchNews","start");
        if(response.isSuccessful()) {
            String data=response.body().string();

            return MY_GSON.fromJson(data, HomeData.class);
        }
        else
            throw new Exception("获取失败:"+response.code());
    }

    /**
     * 获取某个新闻全部内容
     * @param id
     * @return
     * @throws IOException
     */

    public static News fetchNewsById(int id) throws Exception {
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(BASE_URL+"/news/"+id).get().build();
        Response response=client.newCall(request).execute();
        if(response.isSuccessful())

            return MY_GSON.fromJson(response.body().string(),News.class);
        else
            throw new Exception("获取失败");
    }

}
