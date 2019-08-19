package com.mocs.main.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mocs.common.bean.User;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 进行User类的数据加载
 */
public class UserModel {
    private static final String QQ_APPID = "1108752037";
    private static final String GET_QQ_USER_INFO_URL = "https://graph.qq.com/user/get_user_info";

    public UserModel() {
    }

    /**
     * 获取qq用户的信息,现在只有获取qq头像，这个方法会使相关内容被添加进参数中的user
     */
    public boolean getQQUserInfo(@NonNull User user) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(GET_QQ_USER_INFO_URL).newBuilder();
        urlBuilder.addQueryParameter("access_token", user.getQqAccessToken())
                .addQueryParameter("oauth_consumer_key", QQ_APPID)
                .addQueryParameter("openid", user.getQqOpenId());
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();//同步GET
            JsonObject body = new JsonParser().parse(response.body().string()).getAsJsonObject();
            int ret = body.get("ret").getAsInt();//获取返回码
            if (ret == 0) {
                //正确返回处理
                String url = body.get("figureurl_qq_2").getAsString();//获取px100*100的qq头像url
                user.setAvatarImageUrl(url);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Network Error", "onFailure: getQQUserInfo");
        }
        //错误返回
        return false;
    }
}
