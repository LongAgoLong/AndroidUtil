package com.leo.system.util

import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


object FragmentHelper {

    fun clear(@NonNull fragmentManager: FragmentManager) {
        val fragments = fragmentManager.fragments
        //遍历list容器,清除所有的碎片
        if (fragments.isNotEmpty()) {
            for (fragmentTemp in fragments) {
                fragmentManager.beginTransaction().remove(fragmentTemp).commit()
            }
        }
    }

    fun switch(@NonNull fragmentManager: FragmentManager, @NonNull fragment: Fragment,
               @NonNull tag: String, @IdRes viewGroupId: Int) {
        val fragments = fragmentManager.fragments
        //遍历list容器,隐藏所有的碎片
        for (fragmentTemp in fragments) {
            if (fragmentTemp.isHidden) {
                continue
            }
            fragmentManager.beginTransaction().hide(fragmentTemp).commit()
        }
        if (!fragment.isAdded
                && null == fragmentManager.findFragmentByTag(tag)
                && !fragments.contains(fragment)) {
            fragmentManager.beginTransaction().add(viewGroupId, fragment, tag).commit()
        } else {
            fragmentManager.beginTransaction().show(fragment).commit()
        }
    }
}