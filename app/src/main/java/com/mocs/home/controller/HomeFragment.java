package com.mocs.home.controller;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mocs.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *实现了懒加载,首页碎片
 */
public class HomeFragment extends Fragment {
    private View rootView;
    private View itemRootView;  //绑定item_news.xml
    private boolean isLoaded;//数据是否已加载
    private boolean isViewPrepared;//UI是否已准备好
    /**
     * 2019-07-07简单实现轮播和RecyclerView
    * */
    private AssetManager assetManager;
    private List<String> images;  //保存图片URL地址，用于轮播图
    private int NUM_PICS = 3;     //轮播图数量
    private int NUM_NEWS = 5;     //新闻数量
    private RecyclerView recyclerView;  //新闻页面
    private MyAdapter adapter;      //用于recyclerView
    private LinearLayoutManager layoutManager;
    private ArrayList<News> newsArrayList;  //新闻列表

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_home,container,false);
        itemRootView = inflater.inflate(R.layout.item_news,container,false);
        isViewPrepared =true;//UI准备好了
        Log.d("MainActivity", "initBanner: "+getActivity().getPackageName());
        lazyLoad();
        //2019-07-07新增,初始化轮播图
        if (rootView == null || rootView.getContext() == null){
            Log.d("rootView", "onCreateView: rootView == "+rootView.toString()+'\n'+"rootView.getContext() =="+rootView.getContext().toString());
        } else {
            //配置轮转图
            initBanner();
            //配置RecyclerView
            initRecyclerView();
        }


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
    //图片轮播初始化
    private void initBanner(){

        images = new ArrayList<>();
        assetManager = getActivity().getAssets();
        try {
            String [] imagePaths = assetManager.list("banner_images");
            for(int i = 0; i < NUM_PICS; i++){
                imagePaths[i] = "file:///android_asset/banner_images/"+imagePaths[i];
                //Log.d("assets", "initBanner: "+imagePaths[i]);
            }
            images.addAll(Arrays.asList(imagePaths));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Banner banner = rootView.findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(images);
        banner.setViewPagerIsScroll(true);
        banner.isAutoPlay(true);
        banner.setDelayTime(1500);
        banner.start();
    }

    void initNews(){
        newsArrayList = new ArrayList<>();
        for (int i = 0; i < NUM_NEWS; i++){
            newsArrayList.add(new News());
        }
    }

    void initRecyclerView(){
        initNews();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.newsRV);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(newsArrayList);
        recyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }

    //图片加载器
    public class GlideImageLoader extends ImageLoader{
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }

    //临时新闻类
    class News{
        private int newsId;
        private String title;
        private String subtitle;

        public News() {
            newsId = -1;
            title = "标题";
            subtitle = "副标题";
        }

        public int getNewsId() {
            return newsId;
        }

        public void setNewsId(int newsId) {
            this.newsId = newsId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }
    }
    //MyAdapter
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        private ArrayList<News> data;

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView newsImage;
            TextView newsTitle;
            TextView newsSubtitle;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                newsImage = (ImageView) itemRootView.findViewById(R.id.newsImage);
                newsImage.setVisibility(View.VISIBLE);
                newsImage.setImageResource(R.drawable.news_images_0);
                newsTitle = (TextView) itemRootView.findViewById(R.id.newsTitle);
                newsSubtitle = (TextView) itemRootView.findViewById(R.id.newsSubtitle);
            }
        }

        MyAdapter(ArrayList<News> data){
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(rootView.getContext()).inflate(R.layout.item_news,viewGroup,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            News news = data.get(i);
            //if (viewHolder.newsTitle == null) Log.d("Item组件", "=null");
            viewHolder.newsImage.setImageResource(R.drawable.news_images_2);
            viewHolder.newsTitle.setText(news.getTitle());
            viewHolder.newsSubtitle.setText(news.getSubtitle());
        }

        @Override
        public int getItemCount() {
            return data == null ? 0: data.size();
        }
    }

}
