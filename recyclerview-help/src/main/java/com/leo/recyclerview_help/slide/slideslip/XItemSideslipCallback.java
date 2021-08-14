package com.leo.recyclerview_help.slide.slideslip;

import android.graphics.Canvas;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by WANG on 18/3/14.
 */

public class XItemSideslipCallback extends XItemSideslipHelper.Callback {
    String type;

    public XItemSideslipCallback(String type) {
        this.type = type;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


    @Override
    int getSlideViewWidth() {
        return 0;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.START);
    }

    @Override
    public String getItemSlideType() {
        return type;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX,
                            float dY, int actionState, boolean isCurrentlyActive) {
        if (viewHolder instanceof XItemSideslipViewHold) {
            XItemSideslipViewHold holder = (XItemSideslipViewHold) viewHolder;
            if (holder.canSlide()) {
                float actionWidth = holder.getActionWidth();
                if (dX < -actionWidth) {
                    dX = -actionWidth;
                }
                holder.getSlideView().setTranslationX(dX);
            }
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
    }
}
