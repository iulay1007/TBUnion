package com.example.tbunion.presenter;


import com.example.tbunion.base.IBasePresenter;
import com.example.tbunion.view.IHomeCallback;


public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    //获取商品的分类
    void getCategories();
}