package com.leo.recyclerview_help.slide.swap.listener;

/**
 * Create by LEO
 * on 2018/7/26
 * at 14:54

 */

public interface OnItemTouchCallback {
    //数据交换
    void onItemMove(int fromPosition, int toPosition);

    //数据删除
    void onItemDelete(int position);
}
