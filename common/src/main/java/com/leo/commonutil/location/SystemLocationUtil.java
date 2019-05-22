package com.leo.commonutil.location;

import android.Manifest;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;

import com.leo.commonutil.app.ContextHelp;
import com.leo.commonutil.app.LogUtil;
import com.leo.commonutil.app.SystemUtils;
import com.leo.commonutil.storage.IOUtil;

import java.io.IOException;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Create by LEO
 * 定位系统级-工具
 */
public class SystemLocationUtil implements LocationListener {
    private static final String TAG = SystemLocationUtil.class.getSimpleName();
    private static final double pi = 3.1415926535897932384626;
    private static final double a = 6378245.0;
    private static final double ee = 0.00669342162296594323;

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
    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
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
    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
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
    public Location getmLocationWGS84() {
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
        IOUtil.getThreadPool().execute(() -> {
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
        IOUtil.getThreadPool().execute(() -> {
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
            mLocationGCJ02 = convertWGS84ToGCJ02(mLocationWGS84);
            if (null != mLocationGCJ02) {
                mLocationBD09LL = convertGCJ02ToBD09LL(mLocationGCJ02);
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

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param location 源坐标：gcj02坐标系
     * @return
     */
    public Location convertGCJ02ToBD09LL(Location location) {
        // 纬度
        double x = location.getLongitude();
        // 经度
        double y = location.getLatitude();
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
        double bdLon = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        Location locationBD09LL = new Location(location);
        locationBD09LL.setLongitude(bdLon);
        locationBD09LL.setLatitude(bdLat);
        return locationBD09LL;
    }

    /**
     * wgs84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
     *
     * @param locationWGS84 源坐标：wgs84坐标系
     * @return
     */
    public Location convertWGS84ToGCJ02(Location locationWGS84) {
        double lat = locationWGS84.getLatitude();
        double lon = locationWGS84.getLongitude();
        if (isOutOfChina(lat, lon)) {
            return null;
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        Location locationGCJ02 = new Location(locationWGS84);
        locationGCJ02.setLongitude(mgLon);
        locationGCJ02.setLatitude(mgLat);
        return locationGCJ02;
    }

    private boolean isOutOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347) {
            return true;
        }
        if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }

    public double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }
}
