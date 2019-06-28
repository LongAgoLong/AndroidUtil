package com.leo.recyclerview_help.slide.swap;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.leo.recyclerview_help.slide.swap.listener.OnItemTouchCallback;

/**
 * Create by LEO
 * on 2018/7/26
 * at 14:57
 * 半自动拖动交换位置
 * 主动调用mItemTouchHelper.startDrag使能某些 item 可拖动
 */
public class SemiAutoItemDragCallback extends ItemTouchHelper.Callback {
    private int dragFlags = 0;    //拖动
    private OnItemTouchCallback onItemTouchCallback;

    public SemiAutoItemDragCallback(int dragFlags) {
        this.dragFlags = dragFlags;
    }

    public void setOnItemTouchCallback(OnItemTouchCallback onItemTouchCallback) {
        this.onItemTouchCallback = onItemTouchCallback;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(dragFlags, 0);
    }

    /**
     * 长按选中item时调用
     *
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setScaleX(1.2f);
            viewHolder.itemView.setScaleY(1.2f);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 手指松开时调用
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setScaleX(1.0f);
        viewHolder.itemView.setScaleY(1.0f);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if (null != onItemTouchCallback) {
            onItemTouchCallback.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
