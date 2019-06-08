package com.leo.recyclerview_help.manager;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
/**
 * Created by LEO
 * On 2019/6/8
 * Description:可设置是否滚动的GridLayoutManager
 */
public class CstGridLayoutManager extends GridLayoutManager implements IScrollEnable {
    private boolean isScrollEnable;

    public CstGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CstGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public CstGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
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
