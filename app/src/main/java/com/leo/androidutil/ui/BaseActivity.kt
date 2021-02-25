package com.leo.androidutil.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.leo.commonutil.app.AppStackManager

open class BaseActivity : AppCompatActivity() {
    val TAG = BaseActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActionBar()
        AppStackManager.instance.addActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppStackManager.instance.killActivity(this)
    }

    /**
     * 初始化状态栏
     */
    protected open fun initActionBar() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
