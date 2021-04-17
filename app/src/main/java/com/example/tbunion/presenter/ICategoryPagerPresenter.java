package com.example.tbunion.presenter;

import com.example.tbunion.base.IBasePresenter;
import com.example.tbunion.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {

    //根据分类id去获取内容
    void getContentByCategoryId(int categoryId);

    void loaderMore(int categoryId);

    void reload(int categoryId);


}
