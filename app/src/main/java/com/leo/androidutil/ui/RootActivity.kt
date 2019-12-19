package com.leo.androidutil.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil

import android.view.View
import android.widget.Button
import androidx.databinding.ViewDataBinding

import com.leo.androidutil.R
import com.leo.androidutil.databinding.ActivityRootBinding
import com.leo.system.IntentUtil

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
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.locationBtn -> IntentUtil.startActivity(this, LocationActivity::class.java)
            R.id.pinyinBtn -> IntentUtil.startActivity(this, PinyinActivity::class.java)
        }
    }
}
