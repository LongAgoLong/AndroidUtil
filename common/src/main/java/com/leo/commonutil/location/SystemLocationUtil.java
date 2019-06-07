package com.leo.commonutil.location;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import android.text.TextUtils;

import com.leo.commonutil.asyn.threadPool.ThreadPoolHelp;
import com.leo.system.ContextHelp;
import com.leo.system.LogUtil;
import com.leo.system.SystemUtils;

import java.io.IOException;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Create by LEO
 * 定位系统级-工具
 */
public class SystemLocationUtil implements LocationListener {
    private static final String TAG = SystemLocationUtil.class.getSimpleName();

    private static SystemLocationUtil mInstance;
    private OnLocationCallback mOnLocationCallback;
    private Location mLocationWGS84;
    private Location mLocationGCJ02;
    private Location mLocationBD09LL;
    private Address addr;

    public static SystemLocationUtil getInstance() {
        if (mInstance == null) {
            synchronized (SystemLocationUtil.class) {
                if (mInstance == null) {
                    mInstance = new SystemLocationUtil();
                }
            }
        }
        return mInstance;
    }

    public String getBestProvider() {
        LocationManager locationManager =
                (LocationManager) ContextHelp.getContext().getSystemService(LOCATION_SERVICE);
        if (null == locationManager) {
            LogUtil.e(TAG, "LocationManager is null");
            return null;
        }
        List<String> providers = locationManager.getProviders(true);
        // 测试一般在室内，室内没有gps信号，所以优先网络
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        } else {
            return null;
        }
    }

    /**
     * 开始定位
     *
     * @param provider            LocationManager.GPS_PROVIDER/LocationManager.NETWORK_PROVIDER
     * @param minMillisecond      定位时间间隔
     * @param mOnLocationCallback 回调接口
     */
    public void start(String provider, long minMillisecond, @Nullable OnLocationCallback mOnLocationCallback) {
        Context context = ContextHelp.getContext();
        if (null == context) {
            return;
        }
        this.mOnLocationCallback = mOnLocationCallback;
        // NETWORK_PROVIDER,使用网络定位
        // 定位时间间隔,单位ms；不应小于1000
        LocationManager locationManager =
                (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (null == locationManager) {
            LogUtil.e(TAG, "LocationManager is null");
            return;
        }
        if (!SystemUtils.checkPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION)
                && !SystemUtils.checkPermissions(context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            LogUtil.e(TAG, "lack of permission ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION");
            return;
        }
        if (!locationManager.isProviderEnabled(provider)) {
            LogUtil.e(TAG, "provider is disable");
            return;
        }
        locationManager.requestLocationUpdates(provider, minMillisecond, 0, this);
        LogUtil.i(TAG, "system location start");
    }

    /**
     * 停止定位
     */
    public void stop() {
        mOnLocationCallback = null;
        Context context = ContextHelp.getContext();
        if (null == context) {
            return;
        }
        LocationManager locationManager =
                (LocationManager) context.getSystemService(LOCATION_SERVICE);
        if (null == locationManager) {
            return;
        }
        locationManager.removeUpdates(this);
    }

    /**
     * 获取最新定位结果(火星坐标系)
     *
     * @return
     */
    public Location getLocationGCJ02() {
        return mLocationGCJ02;
    }

    /**
     * 获取最新定位结果(百度坐标系)
     *
     * @return
     */
    public Location getLocationBD09LL() {
        return mLocationBD09LL;
    }

    /**
     * 获取最新定位结果(地球坐标系/国标)
     *
     * @return
     */
    public Location getLocationWGS84() {
        return mLocationWGS84;
    }

    /**
     * 获取详细地址信息
     *
     * @return
     */
    public Address getAddressBean() {
        return addr;
    }

    public String getAddressStr() {
        return getAddressStr(addr);
    }

    public String getAddressStr(Address address) {
        if (null != address) {
            // 省-市-区/县-街道/路
            StringBuilder builder = new StringBuilder();
            builder.append(address.getAdminArea());
            // 直辖市返回的省、市相同，做差异判断
            if (!TextUtils.equals(address.getAdminArea(), address.getLocality())) {
                builder.append(address.getLocality());
            }
            builder.append(!TextUtils.isEmpty(address.getSubLocality()) ? address.getSubLocality() : "")
                    .append(!TextUtils.isEmpty(address.getThoroughfare()) ? address.getThoroughfare() : "");
            if (address.getMaxAddressLineIndex() > 0) {
                // 门址
                builder.append(address.getAddressLine(0));
            }
            return builder.toString();
        }
        return "";
    }

    /**
     * 根据国标WGS84坐标系经纬度获取具体位置信息
     *
     * @param latitude  经度
     * @param longitude 纬度
     */
    private void reverGeo(double latitude, double longitude) {
        ThreadPoolHelp.execute(() -> {
            Context context = ContextHelp.getContext();
            if (null == context) {
                return;
            }
            Geocoder geocoder = new Geocoder(context);
            int maxResults = 1;
            try {
                // 最大结果条数
                List<Address> addresses = geocoder.getFromLocation(latitude,
                        longitude, maxResults);
                if (null != addresses && !addresses.isEmpty()) {
                    // 第一个位置
                    addr = addresses.get(0);
                    if (null != mOnLocationCallback) {
                        mOnLocationCallback.onLocationReverGeoResult();
                    }
                    LogUtil.i(TAG, getAddressStr());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 对外提供根据国标WGS84坐标系经纬度获取具体位置信息Geo
     *
     * @param context
     * @param latitude
     * @param longitude
     * @param onReverGeoCallback
     */
    public void reverGeo(Context context, final double latitude, final double longitude,
                         OnReverGeoCallback onReverGeoCallback) {
        ThreadPoolHelp.execute(() -> {
            Geocoder geocoder = new Geocoder(context);
            int maxResults = 1;
            try {
                // 最大结果条数
                List<Address> addresses = geocoder.getFromLocation(latitude,
                        longitude, maxResults);
                if (null != addresses && !addresses.isEmpty()) {
                    // 第一个位置
                    Address addr = addresses.get(0);
                    if (null != onReverGeoCallback) {
                        onReverGeoCallback.onReverGeo(addr);
                    }
                    LogUtil.i(TAG, getAddressStr());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            LogUtil.i(TAG, "location = " + location.toString());
            mLocationWGS84 = location;
            reverGeo(mLocationWGS84.getLatitude(), mLocationWGS84.getLongitude());
            // 系统返回的是wgs84坐标系
            mLocationGCJ02 = LocationTransHelp.convertWGS84ToGCJ02(mLocationWGS84);
            if (null != mLocationGCJ02) {
                mLocationBD09LL = LocationTransHelp.convertGCJ02ToBD09LL(mLocationGCJ02);
            }
            if (null != mOnLocationCallback) {
                mOnLocationCallback.onLocationChanged();
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
