package com.mocs.home.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mocs.R;
import com.mocs.common.base.BaseLazyFragment;
import com.mocs.common.bean.News;
import com.mocs.home.adapter.NewsAdapter;
import com.mocs.home.loader.BannerImageLoader;
import com.mocs.home.model.HomeData;
import com.mocs.home.model.NewsModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.youth.banner.Banner;

import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;

/**
 * 实现了懒加载,首页碎片
 */
public class HomeFragment extends BaseLazyFragment {
    private View rootView;
    private Unbinder mUnbinder;
    @BindView(R.id.banner_home)
    Banner banner;
    @BindView(R.id.news_list_home)
    RecyclerView newsListView;
    @BindView(R.id.refresh_layout_home)
    RefreshLayout refreshLayout;
    private HomeData homeData=new HomeData();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        super.onCreateView(inflater, container, savedInstanceState);//一定要先加载自己的layout再调用父类的方法,才能实现懒加载
        homeData=new HomeData();
        return rootView;
    }

    /**
     * 加载数据放在这里可以实现懒加载
     */
    @Override
    protected void lazyLoadData() {
        fetchData();
    }
    private void fetchData(){
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnRefreshListener(layout->new RefreshAsyncTask().execute());
        refreshLayout.autoRefresh();
    }

    /**
     * 初始化View
     */
    private void initView() {
        banner.setImageLoader(new BannerImageLoader());
        banner.setImages(homeData.getBanners().stream().map(News::getCover).collect(Collectors.toList()));
        banner.setBannerTitles(homeData.getBanners().stream().map(News::getTitle).collect(Collectors.toList()));
        banner.start();
        LinearLayoutManager manager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        newsListView.setLayoutManager(manager);
        newsListView.setItemAnimator(new FadeInUpAnimator());
        newsListView.setNestedScrollingEnabled(false);
        NewsAdapter newsAdapter = new NewsAdapter(homeData.getList(),getContext());
        newsAdapter.setOnItemClickListener(i -> {
            Intent intent = new Intent(getContext(), NewsActivity.class);
            intent.putExtra("news_id", homeData.getList().get(i).getId());
            startActivity(intent);
        });
        newsListView.setAdapter(newsAdapter);
        newsAdapter.notifyItemRangeInserted(0,homeData.getList().size());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private class RefreshAsyncTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String msg = null;
            try {
                homeData= NewsModel.fetchNewsData();
            } catch (Exception e) {
                msg = e.getMessage();
                homeData=new HomeData();
            } finally {
                return msg;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s!=null){
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            refreshLayout.setNoMoreData(true);
            refreshLayout.finishRefresh();
            initView();
        }
    }

}


