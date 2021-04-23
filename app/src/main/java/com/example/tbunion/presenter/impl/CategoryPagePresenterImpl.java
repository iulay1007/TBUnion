package com.example.tbunion.presenter.impl;


import com.example.tbunion.model.Api;
import com.example.tbunion.model.domain.HomePagerContent;
import com.example.tbunion.presenter.ICategoryPagerPresenter;
import com.example.tbunion.utils.LogUtils;
import com.example.tbunion.utils.RetrofitManager;
import com.example.tbunion.utils.UrlUtils;
import com.example.tbunion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.HTTP;

public class CategoryPagePresenterImpl implements ICategoryPagerPresenter {

    private Map<Integer,Integer> pageInfo=new HashMap<>();

    public static final int DEFAULT_PAGE = 1;

    private CategoryPagePresenterImpl(){

    }

    private static ICategoryPagerPresenter sInstance=null;

    public static ICategoryPagerPresenter getsInstance(){
        if(sInstance == null){
            sInstance = new CategoryPagePresenterImpl();
        }
        return sInstance;
    }


    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if(callback.getCategoryId() == categoryId){
                callback.onLoading();
            }
        }
        //根据分类ID加载内容
       Integer targetPage=pageInfo.get(categoryId);
        if(targetPage == null){
            targetPage = DEFAULT_PAGE;
            pageInfo.put(categoryId,DEFAULT_PAGE);
        }
        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId,targetPage);
        LogUtils.d(this,"homepager url -->"+homePagerUrl);
        Retrofit retrofit= RetrofitManager.getInstance().getRetrofit();
        Api api =retrofit.create(Api.class);
        api.getHomePageContent(homePagerUrl);
        Call<HomePagerContent> task =api.getHomePageContent(homePagerUrl);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(CategoryPagePresenterImpl.this,"code -->"+code);
                if(code == HttpURLConnection.HTTP_OK){
                   HomePagerContent pagerContent = response.body();
                   LogUtils.d(CategoryPagePresenterImpl.this,"pagecontent  -->"+pagerContent);
                    LogUtils.d(CategoryPagePresenterImpl.this,"categoryId -->"+categoryId);

                    //把数据更新到UI
                    handleHomePageContentResult(pagerContent,categoryId);
                }else {
                    LogUtils.d(CategoryPagePresenterImpl.this,"error code -->"+code);

                    handleNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(CategoryPagePresenterImpl.this,"onFailure -->"+t.toString());
                handleNetworkError(categoryId);
            }
        });

    }

    private void handleNetworkError(int categoryId) {
        for(ICategoryPagerCallback callback : callbacks) {
            if(callback.getCategoryId() == categoryId){
            callback.onError();
            }
        }
        }

    private void handleHomePageContentResult(HomePagerContent pagerContent, int categoryId) {
        //通知UI层更新数据
        List<HomePagerContent.DataBean> data=pagerContent.getData();
        for(ICategoryPagerCallback callback : callbacks){
            if(callback.getCategoryId() == categoryId){

                if(pagerContent == null || pagerContent.getData().size() ==0){
                callback.onEmpty();
            }
            else {
                List<HomePagerContent.DataBean> looperData =data.subList(data.size()-5,data.size());
                callback.onLooperListLoaded(looperData);
                callback.onContentLoaded(data);
            }
            }
        }
    }

    @Override
    public void loaderMore(int categoryId) {

    }

    @Override
    public void reload(int categoryId) {

    }

    private List<ICategoryPagerCallback> callbacks =new ArrayList<>();
    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if(!callbacks.contains(callback)){
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
        callbacks.remove(callback);
    }
}
