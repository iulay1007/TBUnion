package com.example.tbunion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tbunion.R;

public abstract class BaseFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return loadRootView(inflater,container,savedInstanceState);
    }
    protected   View loadRootView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        int resId=getRootViewResId();
        return inflater.inflate(resId,container,false);
    }

    protected abstract int getRootViewResId();
}
