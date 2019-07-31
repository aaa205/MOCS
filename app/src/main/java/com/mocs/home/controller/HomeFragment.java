package com.mocs.home.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mocs.R;
import com.mocs.common.base.BaseLazyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 实现了懒加载,首页碎片
 */
public class HomeFragment extends BaseLazyFragment {
    private View rootView;
    private Unbinder mUnbinder;
    @BindView(R.id.textView_home)
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        super.onCreateView(inflater, container, savedInstanceState);//一定要先加载自己的layout再调用父类的方法,才能实现懒加载
        return rootView;
    }

    /**
     * 加载数据放在这里可以实现懒加载
     */
    @Override
    protected void loadData() {

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Log.d("Lazy", "load data");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

}


