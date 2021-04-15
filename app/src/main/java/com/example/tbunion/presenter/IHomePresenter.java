package com.example.tbunion.presenter;

import com.example.tbunion.view.IHomeCallback;

public interface IHomePresenter {
    //获取商品的分类
    void getCategories();

    //注册UI通知更新接口
    void registerCallback(IHomeCallback callback);

    //取消注册UI通知更新接口
    void unregisterCallback(IHomeCallback callback);
}