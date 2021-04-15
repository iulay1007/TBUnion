package com.example.tbunion.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

    @Override
    protected void initView(View rootView) {
        tabLayout.setupWithViewPager(homePager);
        homePagerAdapter=new HomePagerAdapter(getChildFragmentManager());
        homePager.setAdapter(homePagerAdapter);

    }

    @Override
    protected void initPresenter() {
     homePresenter=new HomePresenterImpl();
     homePresenter.registerCallback(this);
    }

    @Override
    protected void loadData() {
        homePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        if(homePagerAdapter!=null)
        homePagerAdapter.setCategories(categories);

    }


    @Override
    protected void release() {
        //取消注册
        if(homePresenter!=null)
            homePresenter.unregisterCallback(this);
    }
}

