package com.example.tbunion.base;


public interface IBasePresenter <T> {
    //注册UI通知更新接口
    void registerViewCallback(T callback);

    //取消注册UI通知更新接口
    void unregisterViewCallback(T callback);
}
