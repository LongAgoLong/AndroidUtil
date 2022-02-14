package com.leo.system.util

import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Fragment辅助工具类
 */
object FragmentHelper {

    /**
     * 清空FragmentManager
     * @param fragmentManager FragmentManager
     */
    fun clear(@NonNull fragmentManager: FragmentManager) {
        val fragments = fragmentManager.fragments
        //遍历list容器,清除所有的碎片
        if (fragments.isNotEmpty()) {
            for (fragmentTemp in fragments) {
                fragmentManager.beginTransaction().remove(fragmentTemp).commit()
            }
        }
    }

    /**
     * 切换fragment
     * @param fragmentManager FragmentManager
     * @param fragment Fragment
     * @param tag String
     * @param viewGroupId Int
     */
    fun switch(
        @NonNull fragmentManager: FragmentManager, @NonNull fragment: Fragment,
        @NonNull tag: String, @IdRes viewGroupId: Int
    ) {
        val fragments = fragmentManager.fragments
        // 遍历list容器,隐藏所有的fragment
        for (fragmentTemp in fragments) {
            if (fragmentTemp.isHidden) {
                continue
            }
            fragmentManager.beginTransaction().hide(fragmentTemp).commit()
        }
        if (!fragment.isAdded
            && fragmentManager.findFragmentByTag(tag) == null
            && !fragments.contains(fragment)
        ) {
            fragmentManager.beginTransaction().add(viewGroupId, fragment, tag).commit()
        } else {
            fragmentManager.beginTransaction().show(fragment).commit()
        }
    }
}