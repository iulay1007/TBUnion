package com.example.tbunion.ui.fragment;

import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
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
import com.example.tbunion.utils.SizeUtils;
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
    protected void initListener() {
        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(looperPagerAdapter.getDataSize() == 0)
                    return;
                int targetPosition = position %looperPagerAdapter.getDataSize();
                //切换指示器
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //切换指示器
    private void updateLooperIndicator(int targetPosition) {
        for(int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View point = looperPointContainer.getChildAt(i);
            if(i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }
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
        int dx =(Integer.MAX_VALUE/2)%contents.size();
        int targetCentetPosition =(Integer.MAX_VALUE/2) - dx;
        //设置到中间点
        looperPager.setCurrentItem(targetCentetPosition);
        looperPointContainer.removeAllViews();
        //添加点
        for(int i=0;i<contents.size();i++){
            View point =new View(getContext());
            int size=SizeUtils.dip2px(getContext(),8);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(size,size);
            layoutParams.leftMargin =SizeUtils.dip2px(getContext(),5);
            layoutParams.rightMargin =SizeUtils.dip2px(getContext(),5);
            point.setLayoutParams(layoutParams);
            if(i == 0){
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            }else{
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
            looperPointContainer.addView(point);
        }
    }

    @Override
    protected void release() {
        if(categoryPagePresenter != null){
            categoryPagePresenter.unregisterViewCallback(this);
        }
    }
}
