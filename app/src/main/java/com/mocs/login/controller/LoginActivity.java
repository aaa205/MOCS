package com.mocs.login.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mocs.R;
import com.mocs.main.controller.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.but_login)
    Button loginButton;
    @BindView(R.id.edit_account)
    EditText edit_account;
    @BindView(R.id.edit_password)
    EditText edit_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);//绑定
    }

    //点击事件 登录
    @OnClick(R.id.but_login)
    public void clickToLogin(){
        //暂时
        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
