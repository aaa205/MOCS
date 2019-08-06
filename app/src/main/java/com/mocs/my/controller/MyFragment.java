package com.mocs.my.controller;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mocs.R;
import com.mocs.common.base.BaseLazyFragment;
import com.mocs.common.bean.User;
import com.mocs.main.model.UserModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * “我的”界面
 */
public class MyFragment extends BaseLazyFragment {

    @BindView(R.id.img_user_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.text_user_id)
    TextView textId;
    @BindView(R.id.text_user_nickname)
    TextView textNickname;
    private Unbinder mUnbinder;
    private User mLocalUser;//当前登陆用户
    private UserModel mUserModel;
    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        fragment.mUserModel=new UserModel();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocalUser=(User) getActivity().getIntent().getSerializableExtra("local_user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_my, container, false);
        mUnbinder = ButterKnife.bind(this,rootView);
        super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    protected void loadData() {
        textId.setText(String.valueOf(mLocalUser.getId()));
        textNickname.setText(mLocalUser.getNickname());
        new MyAsynTask().execute();//加载qq信息
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /**执行耗时操作,加载头像*/
 private class MyAsynTask extends AsyncTask<Void,Integer,User>{

     @Override
     protected User doInBackground(Void... voids) {
         mUserModel.getQQUserInfo(mLocalUser);//加载头像
         return mLocalUser;
     }

        @Override
        protected void onPostExecute(User user) {
            Glide.with(MyFragment.this)
                    .load(user.getAvatarImageUrl())
                    .into(imgAvatar);
        }
    }
}

