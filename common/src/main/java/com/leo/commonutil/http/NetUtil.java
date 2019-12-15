package com.leo.commonutil.http;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

import com.leo.commonutil.enume.NetType;
import com.leo.system.ContextHelp;
import com.leo.system.LogUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Random;

/**
 * Created by LEO
 * on 2018/12/24
 * 网络状态工具类
 */
public final class NetUtil {
    private static final String TAG = NetUtil.class.getSimpleName();

    private NetUtil() {
    }

    /**
     * 判断当前设备是否为手机
     *
     * @param context
     * @return
     */
    public static boolean isPhone(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null == telephony || telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 获取当前网络类型
     *
     * @return
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static int getConnectedType() {
        ConnectivityManager cm = (ConnectivityManager) ContextHelp.INSTANCE.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != cm && null != cm.getActiveNetworkInfo()
                && cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            switch (ni.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return NetType.WIFI;
                case ConnectivityManager.TYPE_MOBILE:
                    switch (ni.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NetType.EDGE;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NetType.LTE;
                        default:
                            return NetType.LTE;
                    }
                default:
                    return NetType.LTE;
            }
        }
        return NetType.NONE;
    }

    /**
     * 判断是否有网络连接
     *
     * @return
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) ContextHelp.INSTANCE.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }


    /**
     * 判断WIFI网络是否可用
     *
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean pingIPs(String... dnsAddress) {
        if (null == dnsAddress || dnsAddress.length == 0) {
            return true;
        }
        int maxIndex = dnsAddress.length;
        int indexIP = new Random(maxIndex).nextInt();
        String ip = dnsAddress[indexIP];
        LogUtil.i(TAG, " begin ping ip; IP = " + ip);
        boolean result = tcp2DNSServer(ip);
        if (!result) {
            final int k = indexIP + 1;
            for (indexIP = k; indexIP < k + 3; indexIP++) {
                result = tcp2DNSServer(dnsAddress[indexIP % maxIndex]);
                if (result) {
                    break;
                }
            }
        }
        return result;
    }

    private static boolean tcp2DNSServer(String dnsServerIP) {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress(dnsServerIP, 53);
            sock.connect(sockaddr, timeoutMs);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

