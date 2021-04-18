package com.example.tbunion.view;

import com.example.tbunion.base.IBaseCallback;
import com.example.tbunion.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallback {
    //数据加载
    void onContentLoaded(List<HomePagerContent.DataBean> contents);
    //
    int getCategoryId();
    //加载更多时出错
    void onLoaderMoreError();
    //没有更多内容
    void onLoaderMoreEmpty();
    //加载到更多内容
    void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents);
    //轮播图内容加载
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);
}
