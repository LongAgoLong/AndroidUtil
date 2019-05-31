package com.leo.commonutil.location;

import android.location.Location;

public class LocationTransHelp {
    private static final double pi = 3.1415926535897932384626;
    private static final double a = 6378245.0;
    private static final double ee = 0.00669342162296594323;

    private LocationTransHelp() {
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 将 GCJ-02 坐标转换成 BD-09 坐标
     *
     * @param location 源坐标：gcj02坐标系
     * @return
     */
    public static Location convertGCJ02ToBD09LL(Location location) {
        LocationTransResult transResult = convertGCJ02ToBD09LL(location.getLatitude(), location.getLongitude());
        Location locationBD09LL = new Location(location);
        locationBD09LL.setLongitude(transResult.longitude);
        locationBD09LL.setLatitude(transResult.latitude);
        return locationBD09LL;
    }

    public static LocationTransResult convertGCJ02ToBD09LL(double latitude, double longitude) {
        // 纬度
        double x = longitude;
        // 经度
        double y = latitude;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);
        double bdLon = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        return new LocationTransResult(bdLat, bdLon);
    }

    /**
     * wgs84 to 火星坐标系 (GCJ-02) World Geodetic System ==> Mars Geodetic System
     *
     * @param locationWGS84 源坐标：wgs84坐标系
     * @return
     */
    public static Location convertWGS84ToGCJ02(Location locationWGS84) {
        LocationTransResult transResult = convertWGS84ToGCJ02(locationWGS84.getLatitude(), locationWGS84.getLongitude());
        if (null == transResult) {
            return null;
        }
        Location locationGCJ02 = new Location(locationWGS84);
        locationGCJ02.setLongitude(transResult.longitude);
        locationGCJ02.setLatitude(transResult.latitude);
        return locationGCJ02;
    }

    public static LocationTransResult convertWGS84ToGCJ02(double latitude, double longitude) {
        if (isOutOfChina(latitude, longitude)) {
            return null;
        }
        double dLat = transformLat(longitude - 105.0, latitude - 35.0);
        double dLon = transformLon(longitude - 105.0, latitude - 35.0);
        double radLat = latitude / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = latitude + dLat;
        double mgLon = longitude + dLon;
        return new LocationTransResult(mgLat, mgLon);
    }

    private static boolean isOutOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347) {
            return true;
        }
        if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
                * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
                * pi)) * 2.0 / 3.0;
        return ret;
    }
}
