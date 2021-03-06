package com.example.tbunion.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.tbunion.model.domain.HomePagerContent;
import com.example.tbunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {
    private List<HomePagerContent.DataBean> data=new ArrayList<>();
    private onLooperPageItemClickListener mItemClickListener = null;

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public int getDataSize(){
        return data.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //处理越界问题
        int realPosition =position %data.size();
        HomePagerContent.DataBean dataBean=data.get(realPosition);
        int measuredHeight = container.getMeasuredHeight();
        int measuredWidth = container.getMeasuredWidth();
        int ivSize =(measuredWidth>measuredHeight?measuredWidth:measuredHeight)/2;
        String coverUrl= UrlUtils.getCoverPath(dataBean.getPict_url(),ivSize);
        ImageView iv=new ImageView(container.getContext());
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(container.getContext()).load(coverUrl).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    HomePagerContent.DataBean item = data.get(realPosition);
                    mItemClickListener.onLooperItemClick(item);
                }
            }
        });
        container.addView(iv);
        return iv;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }

    public void setonLooperPageItemClickListener(onLooperPageItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public interface onLooperPageItemClickListener{
        void onLooperItemClick(HomePagerContent.DataBean item);
    }
}
