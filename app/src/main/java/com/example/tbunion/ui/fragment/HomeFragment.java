package com.example.tbunion.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.tbunion.R;
import com.example.tbunion.base.BaseFragment;
import com.example.tbunion.model.domain.Categories;
import com.example.tbunion.presenter.IHomePresenter;
import com.example.tbunion.presenter.impl.HomePresenterImpl;
import com.example.tbunion.ui.adapter.HomePagerAdapter;
import com.example.tbunion.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements IHomeCallback {

    @BindView(R.id.home_indicator)
    public TabLayout tabLayout;

    @BindView(R.id.home_pager)
    public ViewPager homePager;

    private IHomePresenter homePresenter;
    private   HomePagerAdapter homePagerAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }


    @Override
    protected void initView(View rootView) {
        tabLayout.setupWithViewPager(homePager);
        homePagerAdapter=new HomePagerAdapter(getChildFragmentManager());
        homePager.setAdapter(homePagerAdapter);

    }

    @Override
    protected void initPresenter() {
     homePresenter=new HomePresenterImpl();
     homePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        homePresenter.getCategories();
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout,container,false);
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
            setupState(State.SUCCESS);
        if(homePagerAdapter!=null) {
          //  homePager.setOffscreenPageLimit(categories.getData().size());
            homePagerAdapter.setCategories(categories);
        }
    }

    @Override
    public void onError() {
        setupState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setupState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setupState(State.EMPTY);
    }


    @Override
    protected void release() {
        //????????????
        if(homePresenter!=null)
            homePresenter.unregisterViewCallback(this);
    }

    @Override
    protected void onRetryClick() {
        //????????????????????????
        //??????????????????
        if(homePresenter!=null){
            homePresenter.getCategories();
        }
    }
}

