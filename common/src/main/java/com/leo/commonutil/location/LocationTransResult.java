package com.leo.commonutil.location;

import com.leo.commonutil.callback.IProguard;

public class LocationTransResult implements IProguard {
    // 经度
    public double latitude;
    // 纬度
    public double longitude;

    public LocationTransResult(double latitude, double longitude) {
        this.latitude = latitude;
        longitude = longitude;
    }
}
