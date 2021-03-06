package com.example.tbunion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.example.tbunion.ui.activity.TicketActivity;
import com.example.tbunion.ui.adapter.HomePagerContentAdapter;
import com.example.tbunion.ui.adapter.LooperPagerAdapter;
import com.example.tbunion.ui.custom.AutoLoopViewPager;
import com.example.tbunion.utils.Constants;
import com.example.tbunion.utils.LogUtils;
import com.example.tbunion.utils.SizeUtils;
import com.example.tbunion.utils.ToastUtil;
import com.example.tbunion.view.ICategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.TbNestedScrollView;


import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, HomePagerContentAdapter.onListItemClickListener, LooperPagerAdapter.onLooperPageItemClickListener {

    private ICategoryPagerPresenter categoryPagePresenter;
    private int materialId;
    private LooperPagerAdapter looperPagerAdapter;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;
    private HomePagerContentAdapter contentAdapter;

    @BindView(R.id.looper_pager)
    public AutoLoopViewPager looperPager;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitleTv;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout twinklingRefreshLayout;

    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView(R.id.home_pager_header_container)
    public LinearLayout homeHeaderContainer;

    @BindView(R.id.home_pager_nested_scroll)
    public TbNestedScrollView homePagerNestedView;

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
        //RecyclerView?????????????????????
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });
        //RecyclerView???????????????
        contentAdapter = new HomePagerContentAdapter();
        //RecyclerView???????????????
        mContentList.setAdapter(contentAdapter);
        //????????????????????????
         looperPagerAdapter =new LooperPagerAdapter();
        //????????????????????????
        looperPager.setAdapter(looperPagerAdapter);
        looperPager.setmDuration(5000);      //??????Refresh????????????
        twinklingRefreshLayout.setEnableRefresh(false);
        twinklingRefreshLayout.setEnableLoadmore(true);



    }

    @Override
    public void onResume() {
        super.onResume();
        //??????????????????????????????start
        looperPager.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        looperPager.stopLoop();
    }

    @Override
    protected void initListener() {
        contentAdapter.setOnListItemClickListener(this);
        looperPagerAdapter.setonLooperPageItemClickListener(this);
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (homeHeaderContainer == null) {
                    return;
                }
                int headerHeight = homeHeaderContainer.getMeasuredHeight();
                homePagerNestedView.setHeaderHeight(headerHeight);
                int measureHeight = homePagerParent.getMeasuredHeight();

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
                layoutParams.height = measureHeight;
                mContentList.setLayoutParams(layoutParams);
                if(measureHeight != 0){
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(looperPagerAdapter.getDataSize() == 0)
                    return;
                int targetPosition = position %looperPagerAdapter.getDataSize();
                //???????????????
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                LogUtils.d(HomePagerFragment.this,"onLoadMore...");
                if (categoryPagePresenter != null) {
                    categoryPagePresenter.loaderMore(materialId);
                }
            }

        });
    }

    //???????????????
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
        //TODO:????????????
        if(categoryPagePresenter!=null){
            categoryPagePresenter.getContentByCategoryId(materialId);
        }
        if(currentCategoryTitleTv!=null){
            currentCategoryTitleTv.setText(title);
        }
    }

    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        //????????????????????????
        //TODO:??????UI
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
        //????????????
        setupState(State.ERROR);
    }

    @Override
    public void onEmpty() {
       setupState(State.EMPTY);
    }

    @Override
    public void onLoaderMoreError() {
        ToastUtil.showToast("??????????????????????????????");
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }

    }

    @Override
    public void onLoaderMoreEmpty() {
        ToastUtil.showToast("??????????????????");
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {
        contentAdapter.addData(contents);
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
        ToastUtil.showToast("?????????"+contents.size()+"?????????");
    }


    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        LogUtils.d(this,"Looper size -- >"+contents.size());
        looperPagerAdapter.setData(contents);
        int dx =(Integer.MAX_VALUE/2)%contents.size();
        int targetCentetPosition =(Integer.MAX_VALUE/2) - dx;
        //??????????????????
        looperPager.setCurrentItem(targetCentetPosition);
        looperPointContainer.removeAllViews();
        //?????????
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

    @Override
    public void onItemClick(HomePagerContent.DataBean item) {
        //?????????????????????
        handleItemClick(item);

    }

    private void handleItemClick(HomePagerContent.DataBean item) {
        startActivity(new Intent(getContext(), TicketActivity.class));
    }

    @Override
    public void onLooperItemClick(HomePagerContent.DataBean item) {
        //????????????????????????
        handleItemClick(item);
    }
}
