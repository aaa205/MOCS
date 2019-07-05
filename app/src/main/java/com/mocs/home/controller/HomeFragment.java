package com.mocs.home.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mocs.R;

/**
 *实现了懒加载,首页碎片
 */
public class HomeFragment extends Fragment {
    private View rootView;
    private boolean isLoaded;//数据是否已加载
    private boolean isViewPrepared;//UI是否已准备好

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_home,container,false);
        isViewPrepared =true;//UI准备好了
        lazyLoad();
        return rootView;
    }
    //懒加载
    protected void lazyLoad(){
        //当前对用户可见，UI已准备好，未加载数据时，加载数据
        Log.d("Lazy","run lazyLoad");
        if (getUserVisibleHint()&& isViewPrepared &&!isLoaded){
            Log.d("Lazy","lazy ok");
            loadData();
        }
    }
    //加载数据
    private void loadData(){
        new Thread(){
            @Override
            public void run() {
                try {
                    Log.d("Lazy","load data");
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            lazyLoad();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoaded=false;
        isViewPrepared=false;
    }

    public HomeFragment() {
        isLoaded =false;//未加载数据
        isViewPrepared =false;
    }

    public static HomeFragment newInstance(){
        return new HomeFragment();
    }

}
