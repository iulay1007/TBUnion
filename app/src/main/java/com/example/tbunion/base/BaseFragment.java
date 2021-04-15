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

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    private State currentState = State.NONE;
    private View successView;
    private View loadingView;
    public enum State{
        NONE,LOADING,SUCCESS,ERROR,EMPTY
    }

    private Unbinder mBind;
    private FrameLayout baseContainer;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.base_fragment_layout,container,false);
        baseContainer=rootView.findViewById(R.id.base_container);
        loadStatesView(inflater,container);

        mBind= ButterKnife.bind(this,rootView);
        initView(rootView);
        initPresenter();
        loadData();
        return rootView;
    }


    //加载各种状态的view
    private void loadStatesView(LayoutInflater inflater, ViewGroup container) {
        //成功的View
        successView=loadSuccessView(inflater,container);
        baseContainer.addView(successView);

        //loading的View
        loadingView=loadLoadingView(inflater,container);
        baseContainer.addView(loadingView);

        loadErrorView(inflater,container);

        setupState(State.NONE);
    }

    private View loadErrorView(LayoutInflater inflater, ViewGroup container) {
       return inflater.inflate(R.layout.fragment_error,container,false);
    }

    private View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty,container,false);
    }


    public void setupState(State state){
        this.currentState=state;
        if(currentState == State.SUCCESS){
            successView.setVisibility(View.VISIBLE);
        }else{
            successView.setVisibility(View.GONE);
        }

        if(currentState == State.LOADING){
            loadingView.setVisibility(View.VISIBLE);
        }else{
            loadingView.setVisibility(View.GONE);
        }
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
