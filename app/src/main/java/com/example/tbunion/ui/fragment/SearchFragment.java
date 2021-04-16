package com.example.tbunion.ui.fragment;

import android.view.View;

import com.example.tbunion.R;
import com.example.tbunion.base.BaseFragment;

public class SearchFragment extends BaseFragment {

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View rootView) {
        setupState(State.SUCCESS);
    }
}