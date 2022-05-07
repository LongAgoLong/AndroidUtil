package com.leo.usblibrary;

import com.github.mjdev.libaums.UsbMassStorageDevice;

import java.util.List;

public interface IUsbDeviceChangedCallback {

    void onUsbDeviceChanged(List<UsbMassStorageDevice> devices);
}
