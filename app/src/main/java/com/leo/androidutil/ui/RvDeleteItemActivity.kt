package com.leo.androidutil.ui

import android.graphics.Color
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.leo.androidutil.R
import com.leo.androidutil.adapter.RvItemDeleteAdapter
import com.leo.androidutil.databinding.ActivityRvDeleteItemBinding
import com.leo.recyclerview_help.decoration.LineItemDecoration
import com.leo.recyclerview_help.slide.slideslip.RvItemSideslipCallback
import com.leo.recyclerview_help.slide.slideslip.RvItemSideslipHelper
import com.leo.system.util.WindowUtils


class RvDeleteItemActivity : BaseActivity() {
    private lateinit var mBinding: ActivityRvDeleteItemBinding
    private lateinit var arrayList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rv_delete_item)
        initData()
        initView()
    }

    private fun initData() {
        arrayList = ArrayList()
        for (i in 0..10) {
            arrayList.add("侧滑${i + 1}")
        }
    }

    private fun initView() {
        mBinding.recyclerView.run {
            layoutManager = LinearLayoutManager(this@RvDeleteItemActivity, LinearLayoutManager.VERTICAL, false)
            adapter = RvItemDeleteAdapter(this@RvDeleteItemActivity, arrayList)
            addItemDecoration(LineItemDecoration(this@RvDeleteItemActivity,
                    OrientationHelper.VERTICAL, WindowUtils.dp2px(dpValue = 1f),
                    false, false, Color.GRAY))
            // ①关联
            val callback = RvItemSideslipCallback(RvItemSideslipHelper.SLIDE_TYPE_ITEMVIEW)
            val rvItemSideslipHelper = RvItemSideslipHelper(callback)
            rvItemSideslipHelper.attachToRecyclerView(this)
        }
    }

    override fun initActionBar() {
        super.initActionBar()
        val actionBar = supportActionBar ?: return
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "仿ios侧滑删除"
    }
}