package com.mocs.login.controller;

import android.content.Intent;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mocs.R;
import com.mocs.main.controller.MainActivity;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * 登录界面，已实现qq登录，登录成功后传递用户信息给MainActivity
 * qq登录回调后，显示一个loading 屏蔽该页面的输入，等待加载完毕打开MainActivity
 * 待实现：
 * progressBar有问题 不能正常显示
 * 发送qq的openid给服务器
 * 记录登录信息，如果曾经登录过，下次再打开时直接打开MainActivity
 * 普通登录方式（太麻烦了 最后再做）
 * 2019-7-16
 * */
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



    /**
     * 点击qq图标，进行登录,获取qq用户信息
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
                        String openid=((JSONObject)o).getString("openid");//获取用户标识
                        String accessToken=((JSONObject)o).getString("access_token");//获取token，用于以后调用API
                        intent.putExtra("qq_openid",openid);
                        intent.putExtra("qq_access_token",accessToken);
                    } catch (JSONException e) {
                        Toast.makeText(LoginActivity.this,"用户信息解析错误\n"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        setLoadingBarVisibility(false);
                        return;
                    }
                    startActivity(intent);
                    setLoadingBarVisibility(false);
                    finish();
                }

                /**登录失败*/
                @Override
                public void onError(UiError uiError) {
                    Toast.makeText(LoginActivity.this, "ErrorCode:"+uiError.errorCode + "\nErrorMessage:"
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
    /**展示一个转圈加载，可视时屏蔽按钮点击*/
    private void setLoadingBarVisibility(boolean visible){
        if (visible){
            progressBar.setVisibility(View.VISIBLE);
            //不可点击
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }else{
            progressBar.setVisibility(View.INVISIBLE);
            //可点击
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}
