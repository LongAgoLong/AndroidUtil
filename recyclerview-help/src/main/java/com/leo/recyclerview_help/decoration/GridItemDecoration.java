package com.leo.recyclerview_help.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by NyatoLEO on 2017/4/10.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private int orientation;
    private int spanCount;
    private int itemSpacePX;
    private int halfItemSpacePX;

    public GridItemDecoration(Context context, int orientation, int spanCount, int itemSpacePX) {
        super();
        this.context = context;
        this.orientation = orientation;
        this.spanCount = spanCount;
        this.itemSpacePX = itemSpacePX;
        halfItemSpacePX = itemSpacePX / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int i = position % spanCount;

        if (orientation == OrientationHelper.VERTICAL) {
            int allRow = parent.getChildCount() / spanCount + (parent.getChildCount() % spanCount != 0 ? 1 : 0);
            position++;
            int currentRow = position / spanCount + (position % spanCount != 0 ? 1 : 0);
            if (currentRow == 1) {
                if (i == 0) {
                    outRect.top = itemSpacePX;
                    outRect.bottom = allRow == 1 ? itemSpacePX : halfItemSpacePX;
                    outRect.left = itemSpacePX;
                    outRect.right = halfItemSpacePX;
                } else if (i == spanCount - 1) {
                    outRect.top = itemSpacePX;
                    outRect.bottom = allRow == 1 ? itemSpacePX : halfItemSpacePX;
                    outRect.left = halfItemSpacePX;
                    outRect.right = itemSpacePX;
                } else {
                    outRect.top = itemSpacePX;
                    outRect.bottom = allRow == 1 ? itemSpacePX : halfItemSpacePX;
                    outRect.left = halfItemSpacePX;
                    outRect.right = halfItemSpacePX;
                }
            } else if (currentRow == allRow) {
                if (i == 0) {
                    outRect.top = halfItemSpacePX;
                    outRect.bottom = itemSpacePX;
                    outRect.left = itemSpacePX;
                    outRect.right = halfItemSpacePX;
                } else if (i == spanCount - 1) {
                    outRect.top = halfItemSpacePX;
                    outRect.bottom = itemSpacePX;
                    outRect.left = halfItemSpacePX;
                    outRect.right = itemSpacePX;
                } else {
                    outRect.top = halfItemSpacePX;
                    outRect.bottom = itemSpacePX;
                    outRect.left = halfItemSpacePX;
                    outRect.right = halfItemSpacePX;
                }
            } else {
                if (i == 0) {
                    outRect.top = halfItemSpacePX;
                    outRect.bottom = halfItemSpacePX;
                    outRect.left = itemSpacePX;
                    outRect.right = halfItemSpacePX;
                } else if (i == spanCount - 1) {
                    outRect.top = halfItemSpacePX;
                    outRect.bottom = halfItemSpacePX;
                    outRect.left = halfItemSpacePX;
                    outRect.right = itemSpacePX;
                } else {
                    outRect.top = halfItemSpacePX;
                    outRect.bottom = halfItemSpacePX;
                    outRect.left = halfItemSpacePX;
                    outRect.right = halfItemSpacePX;
                }
            }
        } else {
            int allColumn = parent.getChildCount() / spanCount + (parent.getChildCount() % spanCount != 0 ? 1 : 0);
            position++;
            int currentColumn = position / spanCount + (position % spanCount != 0 ? 1 : 0);
            if (currentColumn == 1) {
                if (i == 0) {
                    outRect.top = itemSpacePX;
                    outRect.bottom = parent.getChildCount() == 1 ? itemSpacePX : halfItemSpacePX;
                    outRect.left = itemSpacePX;
                    outRect.right = allColumn == 1 ? itemSpacePX : halfItemSpacePX;
                } else if (i == spanCount - 1) {
                    outRect.top = halfItemSpacePX;
                    outRect.bottom = itemSpacePX;
                    outRect.left = itemSpacePX;
                    outRect.right = allColumn == 1 ? itemSpacePX : halfItemSpacePX;
                } else {
                    outRect.top = halfItemSpacePX;
                    outRect.bottom = halfItemSpacePX;
                    outRect.left = itemSpacePX;
                    outRect.right = allColumn == 1 ? itemSpacePX : halfItemSpacePX;
                }
            } else if (currentColumn == allColumn) {
                if (i == 0) {
                    outRect.top = itemSpacePX;
                    outRect.bottom = halfItemSpacePX;
                    outRect.left = halfItemSpacePX;
                    outRect.right = itemSpacePX;
                } else if (i == spanCount - 1) {
                    outRect.top = halfItemSpacePX;
                    outRect.bottom = itemSpacePX;
                    outRect.left = halfItemSpacePX;
                    outRect.right = itemSpacePX;
                } else {
                    outRect.top = halfItemSpacePX;
                    outRect.bottom = halfItemSpacePX;
                    outRect.left = halfItemSpacePX;
                    outRect.right = itemSpacePX;
                }
            } else {
                if (i == 0) {
                    outRect.top = itemSpacePX;
                    outRect.bottom = halfItemSpacePX;
                    outRect.left = halfItemSpacePX;
                    outRect.right = halfItemSpacePX;
                } else if (i == spanCount - 1) {
                    outRect.top = halfItemSpacePX;
                    outRect.bottom = itemSpacePX;
                    outRect.left = halfItemSpacePX;
                    outRect.right = halfItemSpacePX;
                } else {
                    outRect.top = halfItemSpacePX;
                    outRect.bottom = halfItemSpacePX;
                    outRect.left = halfItemSpacePX;
                    outRect.right = halfItemSpacePX;
                }
            }
        }
    }
}
