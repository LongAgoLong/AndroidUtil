package com.leo.androidutil.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.leo.androidutil.R
import com.leo.androidutil.databinding.ActivityLocationBinding
import com.leo.androidutil.viewmodels.LocationModel
import com.leo.commonutil.calendar.DatePresetFormat.DATA_YMDHMS
import com.leo.commonutil.calendar.DateUtil
import com.leo.commonutil.enume.UnitTime
import com.leo.commonutil.location.OnLocationCallback
import com.leo.commonutil.location.SystemLocationUtil
import com.leo.lib_permission.annotations.PermissionApply
import com.leo.lib_permission.annotations.PermissionRefused
import com.leo.lib_permission.annotations.PermissionRefusedForever
import com.leo.system.log.XLog

/**
 * 系统定位demo
 */
class LocationActivity : BaseActivity(), OnLocationCallback, View.OnClickListener {
    private lateinit var mBinding: ActivityLocationBinding
    private lateinit var mModel: LocationModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_location)
//        mModel = ViewModelProviders.of(this).get(LocationModel::class.java)
        mModel = ViewModelProvider(this).get(LocationModel::class.java)
        initView()
        initData()
    }

    @SuppressLint("MissingPermission")
    override fun onPause() {
        super.onPause()
        SystemLocationUtil.getInstance().stop()
    }

    override fun initActionBar() {
        super.initActionBar()
        val actionBar = supportActionBar ?: return
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "系统定位"
    }

    private fun initView() {
        mBinding.permissionBtn.setOnClickListener(this)
        mBinding.stopBtn.setOnClickListener(this)
    }

    private fun initData() {
        mModel.mUpdateTime.observe(this, Observer {
            XLog.d(TAG, "数据变化回调:${it}")
            val locationUtil = SystemLocationUtil.getInstance()
            val addressBean = locationUtil.addressBean ?: return@Observer
            mBinding.resultTv.run {
                text = it
                append("\n")
                append(locationUtil.getAddressStr(addressBean))
                append("\n")
                locationUtil.locationWGS84?.let {
                    append("国标系 (wgs84)：\n${it.latitude} - ${it.longitude}\n")
                }
                locationUtil.locationGCJ02?.let {
                    append("火星坐标系 (GCJ-02)：\n${it.latitude} - ${it.longitude}\n")
                }
                locationUtil.locationBD09LL?.let {
                    append("百度坐标系 (BD-09)：\n${it.latitude} - ${it.longitude}\n")
                }
                append("反GEO：\n${addressBean.latitude} - ${addressBean.longitude}")
            }
        })
    }

    @SuppressLint("MissingPermission")
    @PermissionApply(permissions = [Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION],
            requestCode = 1001)
    fun location() {
        XLog.e(TAG, "授权成功")
        val bestProvider = SystemLocationUtil.getInstance().bestProvider
        SystemLocationUtil.getInstance().start(bestProvider,
                10000, this)
    }

    @PermissionRefused(requestCode = 1001)
    fun onPermissionRefused(s: Array<String?>) {
        XLog.e(TAG, "lack of permission")
    }

    @PermissionRefusedForever(requestCode = 1001)
    fun onPermissionRefusedForever(s: Array<String?>) {
        XLog.e(TAG, "lack of permission")
    }

    override fun onLocationChanged() {
    }

    override fun onLocationReverGeoResult() {
        val locationUtil = SystemLocationUtil.getInstance()
        locationUtil.addressBean ?: return
        val timeFormat = DateUtil.format(UnitTime.MILLIONSECOND, System.currentTimeMillis(), DATA_YMDHMS)
        mModel.mUpdateTime.postValue(timeFormat)
    }

    @SuppressLint("MissingPermission")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.permissionBtn -> location()
            R.id.stopBtn -> SystemLocationUtil.getInstance().stop()
        }
    }
}
