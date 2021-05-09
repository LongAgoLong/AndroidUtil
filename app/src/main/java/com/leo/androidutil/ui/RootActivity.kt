package com.leo.androidutil.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.leo.androidutil.R
import com.leo.androidutil.databinding.ActivityRootBinding
import com.leo.system.util.IntentHelper

class RootActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_root)
        initView()
    }

    private fun initView() {
        mBinding.locationBtn?.setOnClickListener(this)
        mBinding.pinyinBtn?.setOnClickListener(this)
        mBinding.rvDeleteBtn?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.locationBtn -> IntentHelper.startActivity(this, LocationActivity::class.java)
            R.id.pinyinBtn -> IntentHelper.startActivity(this, PinyinActivity::class.java)
            R.id.rvDeleteBtn -> IntentHelper.startActivity(this, RvDeleteItemActivity::class.java)
        }
    }
}
