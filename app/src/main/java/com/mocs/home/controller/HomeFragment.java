package com.mocs.home.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mocs.R;
import com.mocs.common.base.BaseLazyFragment;
import com.mocs.common.bean.News;
import com.mocs.home.adapter.NewsAdapter;
import com.mocs.home.loader.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 实现了懒加载,首页碎片
 */
public class HomeFragment extends BaseLazyFragment {
    private View rootView;
    private Unbinder mUnbinder;
    @BindView(R.id.banner_home)
    Banner banner;
    @BindView(R.id.news_list_home)
    RecyclerView newsRecycler;
    private List<News> mNewsList = new ArrayList<>();

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
    protected void lazyLoadData() {
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        List<String> images = new ArrayList<>();
        images.add("http://www.zgjtb.com/photo/img/2019-10/10/30996a91-72fc-4823-94a1-cb1bcd3b2cd4.jpg");
        images.add("http://www.zgjtb.com/photo/img/2019-11/12/t2_(72X0X589X399)7cc44cb5-951c-4950-853a-080e76a6ef8a_zsize_watermark.png");
        List<String> titles=new ArrayList<>();
        titles.add("title1");
        titles.add("title2");
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(images);
        banner.setBannerTitles(titles);
        banner.start();
        for(int i=0;i<3;i++){
            News news=new News();
            news.setId(1);
            news.setTime(System.currentTimeMillis());
            news.setTitle("白洋长江公路大桥过江主跨桥面横梁安装工作进入煞尾冲刺阶段");
            news.setType("资讯");
            news.setBannerURL("http://www.zgjtb.com/photo/img/2019-11/11/ac28d6f2-e5e7-4d75-8f00-e2455e5ca190_zsize_watermark.png");
            mNewsList.add(news);
        }

        NewsAdapter newsAdapter = new NewsAdapter(mNewsList,getContext());
        newsRecycler.setAdapter(newsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        newsRecycler.setLayoutManager(layoutManager);
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


