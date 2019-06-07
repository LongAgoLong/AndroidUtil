package com.leo.recyclerview_help.slide.swap;


import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by LEO
 * on 2018/3/26
 * at 17:00
 */
public class SimpleItemTouchCallback extends ItemTouchHelper.Callback {
    private int dragFlags = 0;    //拖动
    private int swipeFlags = 0;   //侧滑
    private boolean isLongPressDragEnabled = false;   //支持长按拖拽
    private boolean isItemViewSwipeEnabled = false;   //支持滑动删除功能

    public SimpleItemTouchCallback(int dragFlags, int swipeFlags,
                                   boolean isLongPressDragEnabled, boolean isItemViewSwipeEnabled) {
        this.dragFlags = dragFlags;
        this.swipeFlags = swipeFlags;
        this.isLongPressDragEnabled = isLongPressDragEnabled;
        this.isItemViewSwipeEnabled = isItemViewSwipeEnabled;
    }

    /**
     * 是否支持长按拖拽，默认为true，表示支持长按拖拽
     * 对应长按移动位置功能
     * 也可以返回false，手动调用startDrag()方法启动拖拽
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return isLongPressDragEnabled;
    }

    /**
     * 是否支持任意位置触摸事件发生时启用滑动操作，默认为true，表示支持滑动
     * 对应滑动删除功能
     * 也可以返回false，手动调用startSwipe()方法启动滑动
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return isItemViewSwipeEnabled;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * 长按选中item时调用
     *
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (isLongPressDragEnabled()
                && !isItemViewSwipeEnabled()
                && actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
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
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (isLongPressDragEnabled()
                && !isItemViewSwipeEnabled()) {
            viewHolder.itemView.setScaleX(1.0f);
            viewHolder.itemView.setScaleY(1.0f);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        onItemDissmiss(viewHolder.getAdapterPosition());
    }

    /**
     * 数据交换
     *
     * @param fromPosition
     * @param toPosition
     */
    public void onItemMove(int fromPosition, int toPosition) {
    }

    /**
     * 数据删除
     *
     * @param position
     */
    public void onItemDissmiss(int position) {
    }
}
