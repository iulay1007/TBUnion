package com.lcodecore.tkrefreshlayout.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

public class TbNestedScrollView extends NestedScrollView {
    private int headerHeight = 0;
    private int originScroll = 0;
    private RecyclerView mRecyclerView;

    public TbNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeaderHeight(int headerHeight){
        this.headerHeight = headerHeight;
    }

   @Override
   public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

     if(target instanceof RecyclerView){
         this.mRecyclerView = (RecyclerView)target;
     }
       if(originScroll< headerHeight){
        scrollBy(dx,dy);
        consumed[0]=dx;
        consumed[1]=dy;
       }
       super.onNestedPreScroll(target, dx, dy, consumed, type);
   }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        originScroll = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

    //判断子类是否已经滑动到底部
    public boolean isInBottom() {
        if (mRecyclerView != null) {
          boolean isBottom =  !mRecyclerView.canScrollVertically(1);
          return isBottom;
        }

        return false;
    }
}
