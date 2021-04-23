package com.example.tbunion.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.tbunion.R;
import com.example.tbunion.base.BaseFragment;
import com.example.tbunion.model.domain.Categories;
import com.example.tbunion.model.domain.HomePagerContent;
import com.example.tbunion.presenter.ICategoryPagerPresenter;
import com.example.tbunion.presenter.impl.CategoryPagePresenterImpl;
import com.example.tbunion.ui.adapter.HomePagerContentAdapter;
import com.example.tbunion.ui.adapter.LooperPagerAdapter;
import com.example.tbunion.utils.Constants;
import com.example.tbunion.utils.LogUtils;
import com.example.tbunion.view.ICategoryPagerCallback;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback {

    private ICategoryPagerPresenter categoryPagePresenter;
    private int materialId;
    private LooperPagerAdapter looperPagerAdapter;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;
    private HomePagerContentAdapter contentAdapter;

    @BindView(R.id.looper_pager)
    public ViewPager looperPager;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitleTv;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;


    public static HomePagerFragment newInstance(Categories.DataBean category){
        HomePagerFragment homePagerFragment = new HomePagerFragment();

        Bundle bundle=new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE,category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID,category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;

    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {
        //RecyclerView设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });
        //RecyclerView创建适配器
        contentAdapter = new HomePagerContentAdapter();
        //RecyclerView设置适配器
        mContentList.setAdapter(contentAdapter);
        //创建轮播图适配器
         looperPagerAdapter =new LooperPagerAdapter();
        //设置轮播图适配器
        looperPager.setAdapter(looperPagerAdapter);


    }

    @Override
    protected void initPresenter() {
        categoryPagePresenter = CategoryPagePresenterImpl.getsInstance();
        categoryPagePresenter.registerViewCallback(this);
    }


    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        materialId=arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        //TODO加载数据
        if(categoryPagePresenter!=null){
            categoryPagePresenter.getContentByCategoryId(materialId);
        }
        if(currentCategoryTitleTv!=null){
            currentCategoryTitleTv.setText(title);
        }
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        //数据列表加载到了
        //TODO:更新UI
        contentAdapter.setData(contents);
        setupState(State.SUCCESS);

    }

    @Override
    public int getCategoryId() {
        return materialId;
    }

    @Override
    public void onLoading() {
        setupState(State.LOADING);
    }

    @Override
    public void onError() {
        //网络错误
        setupState(State.ERROR);
    }

    @Override
    public void onEmpty() {
       setupState(State.EMPTY);
    }

    @Override
    public void onLoaderMoreError() {

    }

    @Override
    public void onLoaderMoreEmpty() {

    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        LogUtils.d(this,"Looper size -- >"+contents.size());
        looperPagerAdapter.setData(contents);
        looperPointContainer.removeAllViews();
        //添加点
        for(int i=0;i<contents.size();i++){
            View view =new View(getContext());
            //TODO
          //  LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams()
            looperPointContainer.addView(view);
        }
    }

    @Override
    protected void release() {
        if(categoryPagePresenter != null){
            categoryPagePresenter.unregisterViewCallback(this);
        }
    }
}
