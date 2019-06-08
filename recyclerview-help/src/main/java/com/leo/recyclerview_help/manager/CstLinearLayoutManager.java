package com.leo.recyclerview_help.manager;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Created by LEO
 * On 2019/6/8
 * Description:可设置是否滚动的LinearLayoutManager
 */
public class CstLinearLayoutManager extends LinearLayoutManager implements IScrollEnable {
    private boolean isScrollEnable;

    public CstLinearLayoutManager(Context context) {
        super(context);
    }

    public CstLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CstLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnable && super.canScrollVertically();
    }

    @Override
    public void setScrollEnable(boolean enable) {
        this.isScrollEnable = enable;
    }

    @Override
    public boolean getScrollEnable() {
        return isScrollEnable;
    }
}
