package com.example.tbunion.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static final RetrofitManager retrofitInstance =new RetrofitManager();
    private final Retrofit retrofit;
    public static RetrofitManager getInstance(){
        return retrofitInstance;
    }

    private RetrofitManager(){
        //创建Retrofit
         retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public Retrofit getRetrofit(){
        return retrofit;
    }
}
