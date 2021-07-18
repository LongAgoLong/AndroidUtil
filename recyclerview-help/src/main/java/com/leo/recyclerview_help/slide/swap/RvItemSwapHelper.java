package com.leo.recyclerview_help.slide.swap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

/**
 * Created by LEO
 * On 2019/6/8
 * Description:拖拽工具类
 */
public final class RvItemSwapHelper {
    private RvItemSwapHelper() {
    }

    /**
     * 关联
     *
     * @param recyclerView
     * @param callback
     */
    public static ItemTouchHelper attach(@NonNull RecyclerView recyclerView,
                                         @NonNull ItemTouchHelper.Callback callback) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return itemTouchHelper;
    }

    /**
     * 拖拽时变更数据工具类
     *
     * @param imgArrayList
     * @param fromPosition
     * @param toPosition
     */
    public static void swapData(List<?> imgArrayList, int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(imgArrayList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(imgArrayList, i, i - 1);
            }
        }
    }
}
