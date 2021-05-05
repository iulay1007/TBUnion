package com.example.tbunion.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class TbNestedScrollView extends NestedScrollView {
    private int headerHeight = 0;
    private int originScroll = 0;

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
}
