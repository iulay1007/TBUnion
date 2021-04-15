package com.example.tbunion.presenter.impl;

import com.example.tbunion.model.Api;
import com.example.tbunion.model.domain.Categories;
import com.example.tbunion.presenter.IHomePresenter;
import com.example.tbunion.utils.LogUtils;
import com.example.tbunion.utils.RetrofitManager;
import com.example.tbunion.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {
    private IHomeCallback mCallback=null;

    @Override
    public void getCategories() {
        //加载分类数据
        Retrofit retrofit=RetrofitManager.getInstance().getRetrofit();
        Api api =retrofit.create(Api.class);
        Call<Categories> task=api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                int code=response.code();
                LogUtils.d(HomePresenterImpl.this,"result code is -->"+code);
                if(code== HttpURLConnection.HTTP_OK){
                    Categories categories=response.body();
                    LogUtils.d(HomePresenterImpl.this,categories.toString());
                    if(mCallback!=null){
                        mCallback.onCategoriesLoaded(categories);
                    }
                }else {

                    LogUtils.i(HomePresenterImpl.this,"请求失败");

                }


            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {

                LogUtils.e(HomePresenterImpl.this,"请求错误"+t);
            }
        });
    }

    @Override
    public void registerCallback(IHomeCallback callback) {
        this.mCallback=callback;
    }

    @Override
    public void unregisterCallback(IHomeCallback callback) {
        mCallback=null;
    }


}
