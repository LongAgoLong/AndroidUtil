package com.leo.androidutil.util

import android.app.Activity
import androidx.fragment.app.Fragment
import com.tbruyelle.rxpermissions2.RxPermissions

object RxPermissionsHelp {
    fun newInstance(activity: Activity): RxPermissions {
        return RxPermissions(activity)
    }

    fun newInstance(fragment: Fragment): RxPermissions {
        return RxPermissions(fragment.activity!!)
    }
}