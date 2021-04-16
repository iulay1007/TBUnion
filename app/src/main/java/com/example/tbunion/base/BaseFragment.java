package com.example.tbunion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.tbunion.R;
import com.example.tbunion.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    private State currentState = State.NONE;
    private View successView;
    private View loadingView;
    private View errorView;
    private View emptyView;

    public enum State{
        NONE,LOADING,SUCCESS,ERROR,EMPTY
    }

    private Unbinder mBind;
    private FrameLayout baseContainer;


    @OnClick(R.id.network_error_tips)
    public void retry(){
        //点击了重新加载内容
        LogUtils.d(this,"on retry");
        onRetryClick();
    }

    //子类可选择是否覆盖
    protected void onRetryClick() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = loadRootView(inflater,container);

        baseContainer=rootView.findViewById(R.id.base_container);
        loadStatesView(inflater,container);

        mBind= ButterKnife.bind(this,rootView);
        initView(rootView);
        initPresenter();
        loadData();
        return rootView;
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout,container,false);
    }


    //加载各种状态的view
    private void loadStatesView(LayoutInflater inflater, ViewGroup container) {
        //成功的View
        successView=loadSuccessView(inflater,container);
        baseContainer.addView(successView);

        //loading的View
        loadingView=loadLoadingView(inflater,container);
        baseContainer.addView(loadingView);

        errorView=loadErrorView(inflater,container);
        baseContainer.addView(errorView);

        emptyView=loadEmptyView(inflater,container);
        baseContainer.addView(emptyView);

        setupState(State.NONE);
    }

    private View loadErrorView(LayoutInflater inflater, ViewGroup container) {
       return inflater.inflate(R.layout.fragment_error,container,false);
    }

    private View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty,container,false);
    }


    //子类通过该方法切换状态页面
    public void setupState(State state){
        this.currentState=state;
        successView.setVisibility(currentState == State.SUCCESS?View.VISIBLE:View.GONE);
        loadingView.setVisibility(currentState == State.LOADING?View.VISIBLE:View.GONE);
        errorView.setVisibility(currentState == State.ERROR?View.VISIBLE:View.GONE);
        emptyView.setVisibility(currentState == State.EMPTY?View.VISIBLE:View.GONE);

    }

    //加载loading界面
    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
       return inflater.inflate(R.layout.fragment_loading,container,false);
    }

    protected void initView(View rootView){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mBind!=null){
            mBind.unbind();
        }
        release();
    }

    protected void release() {
        //释放资源
    }

    protected void initPresenter() {
        //创建Presenter
    }

    protected void loadData(){
        //加载数据
    }


    protected View loadSuccessView(LayoutInflater inflater,ViewGroup container){
        int resId=getRootViewResId();
        return inflater.inflate(resId,container,false);
    }

    protected abstract int getRootViewResId();
}
