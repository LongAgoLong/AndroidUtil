package com.leo.commonutil.http

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import com.leo.commonutil.enume.NetType
import com.leo.system.context.ContextHelp.context
import com.leo.system.LogUtil.i
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import java.util.*

/**
 * Created by LEO
 * on 2018/12/24
 * 网络状态工具类
 */
object NetUtil {
    private val TAG = NetUtil::class.java.simpleName
    /**
     * 判断当前设备是否为手机
     *
     * @param context
     * @return
     */
    fun isPhone(context: Context): Boolean {
        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephony.phoneType != TelephonyManager.PHONE_TYPE_NONE
    }

    /**
     * 获取当前网络类型
     *
     * @return
     */
    @get:RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    val connectedType: Int
        get() {
            val cm = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnectedOrConnecting) {
                val ni = cm.activeNetworkInfo
                return when (ni?.type) {
                    ConnectivityManager.TYPE_WIFI -> NetType.WIFI
                    ConnectivityManager.TYPE_MOBILE -> when (ni.subtype) {
                        TelephonyManager.NETWORK_TYPE_GPRS,
                        TelephonyManager.NETWORK_TYPE_CDMA,
                        TelephonyManager.NETWORK_TYPE_EDGE,
                        TelephonyManager.NETWORK_TYPE_1xRTT,
                        TelephonyManager.NETWORK_TYPE_IDEN,
                        TelephonyManager.NETWORK_TYPE_EVDO_A,
                        TelephonyManager.NETWORK_TYPE_UMTS,
                        TelephonyManager.NETWORK_TYPE_EVDO_0,
                        TelephonyManager.NETWORK_TYPE_HSDPA,
                        TelephonyManager.NETWORK_TYPE_HSUPA,
                        TelephonyManager.NETWORK_TYPE_HSPA,
                        TelephonyManager.NETWORK_TYPE_EVDO_B,
                        TelephonyManager.NETWORK_TYPE_EHRPD,
                        TelephonyManager.NETWORK_TYPE_HSPAP -> NetType.EDGE
                        TelephonyManager.NETWORK_TYPE_LTE -> NetType.LTE
                        else -> NetType.LTE
                    }
                    else -> NetType.LTE
                }
            }
            return NetType.NONE
        }

    /**
     * 判断是否有网络连接
     *
     * @return
     */
    @get:RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    val isNetworkConnected: Boolean
        get() {
            val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            return mNetworkInfo?.isAvailable ?: false
        }

    /**
     * 判断WIFI网络是否可用
     *
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isWifiConnected(context: Context?): Boolean {
        context ?: return false
        val mConnectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWiFiNetworkInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (mWiFiNetworkInfo != null) {
            return mWiFiNetworkInfo.isAvailable
        }
        return false
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isMobileConnected(context: Context?): Boolean {
        context ?: return false
        val mConnectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mMobileNetworkInfo = mConnectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if (mMobileNetworkInfo != null) {
            return mMobileNetworkInfo.isAvailable
        }
        return false
    }

    fun pingIPs(vararg dnsAddress: String): Boolean {
        if (dnsAddress.isEmpty()) {
            return true
        }
        val maxIndex = dnsAddress.size
        var indexIP = Random(maxIndex.toLong()).nextInt()
        val ip = dnsAddress[indexIP]
        i(TAG, " begin ping ip; IP = $ip")
        var result = tcp2DNSServer(ip)
        if (!result) {
            val k = indexIP + 1
            indexIP = k
            while (indexIP < k + 3) {
                result = tcp2DNSServer(dnsAddress[indexIP % maxIndex])
                if (result) {
                    break
                }
                indexIP++
            }
        }
        return result
    }

    private fun tcp2DNSServer(dnsServerIP: String): Boolean {
        return try {
            val timeoutMs = 1500
            val sock = Socket()
            val sockaddr: SocketAddress = InetSocketAddress(dnsServerIP, 53)
            sock.connect(sockaddr, timeoutMs)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }
    }
}