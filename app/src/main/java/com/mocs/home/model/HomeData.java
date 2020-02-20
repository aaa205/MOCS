package com.mocs.home.model;

import com.mocs.common.bean.News;

import java.util.List;

public class HomeData {
    private List<News> banners;
    private List<News> list;

    public List<News> getBanners() {
        return banners;
    }

    public void setBanners(List<News> banners) {
        this.banners = banners;
    }

    public List<News> getList() {
        return list;
    }

    public void setList(List<News> list) {
        this.list = list;
    }
}
