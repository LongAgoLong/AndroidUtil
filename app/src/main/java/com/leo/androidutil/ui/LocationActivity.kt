package com.leo.androidutil.ui

import android.Manifest
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import com.leo.androidutil.R
import com.leo.androidutil.databinding.ActivityLocationBinding
import com.leo.commonutil.asyn.WeakHandler
import com.leo.commonutil.calendar.DateUtil
import com.leo.commonutil.enume.UnitTime
import com.leo.commonutil.location.OnLocationCallback
import com.leo.commonutil.location.SystemLocationUtil
import com.leo.system.LogUtil
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * 系统定位demo
 */
class LocationActivity : BaseActivity(), OnLocationCallback, View.OnClickListener {
    private lateinit var mBinding: ActivityLocationBinding
    private val weakHandler = WeakHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_location)
        initActionBar()
        initView()
    }

    private fun initView() {
        mBinding.permissionBtn?.setOnClickListener(this)
        mBinding.stopBtn?.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        weakHandler.removeCallbacksAndMessages(null)
        SystemLocationUtil.getInstance().stop()
    }

    override fun initActionBar() {
        super.initActionBar()
        val actionBar = supportActionBar ?: return
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.title = "系统定位"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun location() {
        val rxPermissions = RxPermissions(this)
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe({ aBoolean ->
                    if (aBoolean!!) {
                        val bestProvider = SystemLocationUtil.getInstance().bestProvider
                        SystemLocationUtil.getInstance().start(bestProvider,
                                10000, this)
                    } else {
                        LogUtil.e(TAG, "lack of permission")
                    }
                }, { throwable -> LogUtil.e(TAG, throwable.toString()) })
    }

    override fun onLocationChanged() {

    }

    override fun onLocationReverGeoResult() {
        weakHandler.post {
            val locationUtil = SystemLocationUtil.getInstance()
            val addressBean = locationUtil.addressBean ?: return@post
            mBinding.resultTv?.run {
                text = DateUtil.format(UnitTime.MILLIONSECOND, System.currentTimeMillis(), DateUtil.DATA_YMDHMS)
                append("\n")
                append(locationUtil.getAddressStr(addressBean))
                append("\n")
                locationUtil.locationWGS84.let {
                    append("国标系 (wgs84)：\n${it.latitude} - ${it.longitude}\n")
                }
                locationUtil.locationGCJ02.let {
                    append("火星坐标系 (GCJ-02)：\n${it.latitude} - ${it.longitude}\n")
                }
                locationUtil.locationBD09LL.let {
                    append("百度坐标系 (BD-09)：\n${it.latitude} - ${it.longitude}\n")
                }
                append("反GEO：\n${addressBean.latitude} - ${addressBean.longitude}")
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.permissionBtn -> location()
            R.id.stopBtn -> SystemLocationUtil.getInstance().stop()
        }
    }

    companion object {
        private val TAG = LocationActivity::class.java.simpleName
    }
}
