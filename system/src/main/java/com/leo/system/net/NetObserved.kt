package com.leo.system.net

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import androidx.annotation.RequiresPermission
import com.leo.system.context.ContextHelper


class NetObserved {
    private var netReceiver: NetReceiver
    private val mNetListeners = mutableListOf<INetChanged>()

    companion object {
        var mInstance: NetObserved? = null
        fun getInstance(): NetObserved {
            return mInstance ?: synchronized(this) {
                mInstance ?: NetObserved().also { mInstance = it }
            }
        }
    }

    init {
        netReceiver = NetReceiver()
        val filter = IntentFilter()
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        ContextHelper.context.registerReceiver(netReceiver, filter)
    }

    fun observed(listen: INetChanged?) {
        if (listen == null || mNetListeners.contains(listen)) {
            return
        }
        mNetListeners.add(listen)
    }

    fun unObserved(listen: INetChanged?) {
        if (listen == null) {
            return
        }
        mNetListeners.remove(listen)
    }

    inner class NetReceiver : BroadcastReceiver() {
        private var mLastState = false

        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        override fun onReceive(context: Context?, intent: Intent?) {
            val connectivityManager = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            val isNetConnect = networkInfo?.isAvailable ?: false
            if (mLastState == isNetConnect) {
                return
            }
            mLastState = isNetConnect
            mNetListeners.forEach { it.onNetChanged(isNetConnect) }
        }
    }

    interface INetChanged {
        fun onNetChanged(isConnect: Boolean)
    }
}