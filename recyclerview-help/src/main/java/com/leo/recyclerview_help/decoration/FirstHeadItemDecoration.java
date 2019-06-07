package com.leo.recyclerview_help.decoration;


import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by LEO
 * on 2018/3/5.
 */
public class FirstHeadItemDecoration extends RecyclerView.ItemDecoration {
    private final int firstGap;
    private final int otherGap;

    public FirstHeadItemDecoration(int firstGap, int otherGap) {
        this.firstGap = firstGap;
        this.otherGap = otherGap;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (0 == position) {
            outRect.set(0, firstGap, 0, otherGap);
        } else {
            outRect.set(0, 0, 0, otherGap);
        }
    }
}
