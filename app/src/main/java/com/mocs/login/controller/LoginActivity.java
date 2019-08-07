package com.mocs.login.controller;

import android.content.Intent;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mocs.R;
import com.mocs.common.bean.User;
import com.mocs.main.controller.MainActivity;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 登录界面，已实现qq登录，登录成功后传递用户信息给MainActivity
 * qq登录回调后，显示一个loading .等待加载完毕打开MainActivity
 * 待实现：
 * progressBar有问题 不能正常显示
 * 发送qq的openid给服务器，获取用户id和token，传给MainActivity
 * 记录登录信息，如果曾经登录过，下次再打开时直接打开MainActivity
 * 普通登录方式（太麻烦了 最后再做）
 * 2019-7-16
 */
public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.but_login)
    Button loginButton;
    @BindView(R.id.edit_account)
    EditText editAccount;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.img_qq)
    ImageView imgQQ;
    @BindView(R.id.progress_bar_login)
    ProgressBar progressBar;
    @BindString(R.string.qq_app_id)
    String QQAppid;
    @BindString(R.string.service_host)
    String host;
    private Tencent mTencent;
    private IUiListener mIUiListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);//绑定
        initLogin();
    }

    private void initLogin() {
        imgQQ.setOnClickListener((v) -> doQQLogin());
    }
    /**调试用 跳过登录*/
    @OnClick(R.id.but_login)
    public void loginDebug(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * 点击qq图标，进行登录,获取qq用户信息
     * intent中传递的数据：
     * local_user : 刚刚登陆的用户User类
     */
    private void doQQLogin() {
        if (mTencent == null)
            mTencent = Tencent.createInstance(QQAppid, getApplicationContext());
        if (mIUiListener == null)
            mIUiListener = new IUiListener() {
                /**登录成功*/
                @Override
                public void onComplete(Object o) {
                    setLoadingBarVisibility(true);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    try {
                        String openid = ((JSONObject) o).getString("openid");//获取用户标识
                        String accessToken = ((JSONObject) o).getString("access_token");//获取token，用于以后调用API
                        User user=new User();
                        user.setQqOpenId(openid);
                        user.setQqAccessToken(accessToken);
                        // 登录自己的服务器
                        JsonObject requestBody = new JsonObject();
                        requestBody.addProperty("qq_openid", openid);
                        OkHttpClient okHttpClient = new OkHttpClient();
                        MediaType mediaType = MediaType.parse("application/json");//发送json
                        Request request = new Request.Builder()
                                .url(host + "/users/login/qq")
                                .post(RequestBody.create(requestBody.toString(), mediaType))
                                .build();
                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Toast.makeText(LoginActivity.this, "登录失败,请检查网络状态", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                JsonObject jsonObject = new JsonParser().parse(response.body().string()).getAsJsonObject();
                                //检查是否登录成功
                                int status =jsonObject.get("status").getAsInt();
                                if(status!=200){
                                    Toast.makeText(LoginActivity.this,"登录失败，请稍后重试",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                int id=jsonObject.get("id").getAsInt();
                                String nickName=jsonObject.get("nickname").getAsString();
                                String accessToken = jsonObject.get("access_token").getAsString();
                                user.setId(id);
                                user.setNickname(nickName);
                                user.setAccessToken(accessToken);
                                intent.putExtra("local_user",user);//传入用户类
                                startActivity(intent);
                                setLoadingBarVisibility(false);
                                finish();
                            }
                        });

                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this, "用户信息解析失败\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "登录失败\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } finally {
                        setLoadingBarVisibility(false);
                    }

                }

                /**登录失败*/
                @Override
                public void onError(UiError uiError) {
                    Toast.makeText(LoginActivity.this, "ErrorCode:" + uiError.errorCode + "\nErrorMessage:"
                            + uiError.errorMessage, Toast.LENGTH_SHORT).show();

                }

                /**取消登录*/
                @Override
                public void onCancel() {
                    Toast.makeText(LoginActivity.this, "cancel qqlogin", Toast.LENGTH_SHORT).show();
                }
            };
        //qq登录
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "get_simple_userinfo", mIUiListener);
        }
    }

    /**
     * 展示一个转圈加载
     */
    private void setLoadingBarVisibility(boolean visible) {
        if (visible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
