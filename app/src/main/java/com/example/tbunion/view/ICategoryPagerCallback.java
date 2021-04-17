package com.example.tbunion.view;

import com.example.tbunion.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback {
    //数据加载
    void onContentLoaded(List<HomePagerContent.DataBean> contents);
    //加载中
    void onLoading(int categoryId);
    //加载错误
    void onError(int categoryId);
    //数据为空
    void onEmpty(int categoryId);
    //加载更多时出错
    void onLoaderMoreError(int categoryId);
    //没有更多内容
    void onLoaderMoreEmpty(int categoryId);
    //加载到更多内容
    void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents);
    //轮播图内容加载
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);
}
