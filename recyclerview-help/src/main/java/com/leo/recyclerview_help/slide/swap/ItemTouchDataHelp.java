package com.leo.recyclerview_help.slide.swap;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by LEO
 * On 2019/6/7
 * Description:拖拽时变更数据工具类
 */
public final class ItemTouchDataHelp {

    public static void swap(ArrayList imgArrayList, int fromPosition, int toPosition) {
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
