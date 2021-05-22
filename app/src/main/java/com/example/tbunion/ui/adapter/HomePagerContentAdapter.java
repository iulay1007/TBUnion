package com.example.tbunion.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tbunion.R;
import com.example.tbunion.model.domain.HomePagerContent;
import com.example.tbunion.utils.UrlUtils;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.InnerHolder> {
    private List<HomePagerContent.DataBean> data =new ArrayList<>();
    private onListItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content,parent,false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        HomePagerContent.DataBean dataBean =data.get(position);
        //设置数据
        holder.setData(dataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    HomePagerContent.DataBean item = data.get(position);
                    mItemClickListener.onItemClick(item);
                }
            }
        });
    }

    public void setOnListItemClickListener(onListItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public interface onListItemClickListener{
        void onItemClick(HomePagerContent.DataBean item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<HomePagerContent.DataBean> contents) {
        int olderSize = data.size();
        data.addAll(contents);
        notifyItemRangeChanged(olderSize,contents.size());
    }

    public  class InnerHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.goods_cover)
        public ImageView coverIv;

        @BindView(R.id.goods_title)
        public TextView title;

        @BindView(R.id.goods_off_prise)
        public TextView offPriseTv;

        @BindView(R.id.goods_after_off_prise)
        public TextView finalPriseTv;

        @BindView(R.id.goods_original_prise)
        public TextView originalPriseTv;

        @BindView(R.id.goods_sell_count)
        public TextView sellCountTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

        public void setData(HomePagerContent.DataBean dataBean) {
          //  title.setText(dataBean.getTitle());
            //Glide.with(itemView.getContext()).load(UrlUtils.getCoverPath(dataBean.getPict_url())).into(cover);
           //TODO
          //  offPriceTv.setText(dataBean.getCoupon_amount()+"");
            Context context = itemView.getContext();
            title.setText(dataBean.getTitle());
            ViewGroup.LayoutParams layoutParams = coverIv.getLayoutParams();
            int width = layoutParams.width;
            int height = layoutParams.height;
            int coverSize =(width>height ? width:height )/2;

            //ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
            //int width = layoutParams.width;
            //int height = layoutParams.height;
            //int coverSize = (width > height ? width : height) / 2;
            //LogUtils.d(this,"url == > " + dataBean.getPict_url());
            String cover = dataBean.getPict_url();
            if(!TextUtils.isEmpty(cover)) {
                String coverPath = UrlUtils.getCoverPath(cover,coverSize);
                Glide.with(context).load(coverPath).into(this.coverIv);
            } else {
                coverIv.setImageResource(R.mipmap.ic_launcher);
            }
            //LogUtils.d(TAG,"coverPath --- > " + coverPath);
            String finalPrise = dataBean.getZk_final_price();
            long couponAmount = dataBean.getCoupon_amount();
            // LogUtils.d(this,"final prise -- > " + finalPrise);
            float resultPrise = Float.parseFloat(finalPrise) - couponAmount;
            //LogUtils.d(this,"result prise -- -> " + resultPrise);
            finalPriseTv.setText(String.format("%.2f",resultPrise));
            offPriseTv.setText(String.format(context.getString(R.string.text_goods_off_prise),couponAmount));
            originalPriseTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originalPriseTv.setText(String.format(context.getString(R.string.text_goods_original_prise),finalPrise));
            sellCountTv.setText(String.format(context.getString(R.string.text_goods_sell_count),dataBean.getVolume()));
        }
    }
}
