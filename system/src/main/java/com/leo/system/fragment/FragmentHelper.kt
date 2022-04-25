package com.leo.system.fragment

import android.view.ViewGroup
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
     * @param group ViewGroup?
     */
    fun clear(@NonNull fragmentManager: FragmentManager, group: ViewGroup? = null) {
        val fragments = fragmentManager.fragments
        // 遍历list容器,清除所有的碎片
        if (fragments.isNotEmpty()) {
            val beginTransaction = fragmentManager.beginTransaction()
            for (fragmentTemp in fragments) {
                beginTransaction.remove(fragmentTemp)
            }
            beginTransaction.commit()
        }
        group?.removeAllViews()
    }

    /**
     * 切换fragment
     * @param fragmentManager FragmentManager
     * @param fragment Fragment
     * @param tag String
     * @param group ViewGroup
     */
    fun switch(
        @NonNull fragmentManager: FragmentManager,
        @NonNull fragment: Fragment,
        @NonNull tag: String,
        group: ViewGroup
    ) {
        val fragments = fragmentManager.fragments
        // 遍历list容器,隐藏所有的fragment
        if (!fragments.isNullOrEmpty()) {
            val beginTransaction = fragmentManager.beginTransaction()
            for (fragmentTemp in fragments) {
                if (fragmentTemp.isHidden) {
                    continue
                }
                beginTransaction.hide(fragmentTemp)
            }
            beginTransaction.commit()
        }
        val transaction = fragmentManager.beginTransaction()
        if (!fragment.isAdded
            && fragmentManager.findFragmentByTag(tag) == null
        ) {
            transaction.add(group.id, fragment, tag)
        } else {
            transaction.show(fragment)
        }
        transaction.commit()
    }
}