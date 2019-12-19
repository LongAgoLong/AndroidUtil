package com.leo.androidutil.ui

import android.os.Bundle

import com.leo.commonutil.app.AppStackManager
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

open class BaseActivity : RxAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppStackManager.getInstance().addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppStackManager.getInstance().killActivity(this)
    }

    /**
     * 初始化状态栏
     */
    protected open fun initActionBar() {

    }
}
