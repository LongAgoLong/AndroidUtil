package com.leo.system.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import androidx.annotation.RequiresPermission;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class BluetoothHelper {
    private static volatile BluetoothHelper mInstance;
    private BluetoothHeadset mBTHeadsetClient;

    private final Set<BluetoothDevice> mConnectedBTDeviceList = new CopyOnWriteArraySet<>();

    private BluetoothHelper() {
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    public static BluetoothHelper getInstance() {
        if (mInstance == null) {
            synchronized (BluetoothHelper.class) {
                if (mInstance == null) {
                    mInstance = new BluetoothHelper();
                }
            }
        }
        return mInstance;
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
    public void init(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.getProfileProxy(context, mProfileListener, BluetoothProfile.HEADSET);

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED);
        context.registerReceiver(mBtReceiver, filter);
    }

    private final BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {

        @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH})
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.HEADSET) {
                mBTHeadsetClient = (BluetoothHeadset) proxy;
                List<BluetoothDevice> connectedDevices = mBTHeadsetClient.getConnectedDevices();
                mConnectedBTDeviceList.clear();
                mConnectedBTDeviceList.addAll(connectedDevices);
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.HEADSET) {
                mBTHeadsetClient = null;
            }
        }
    };

    private final BroadcastReceiver mBtReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (TextUtils.equals(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED, action)) {
                // 连接状态通知
                // 0 - disconnected ; 1 - connecting ; 2 - connected ; 3 - disconnecting
                int state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, 0);
                if (state == 0) {
                    BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mConnectedBTDeviceList.remove(btDevice);
                } else if (state == 2) {
                    BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    mConnectedBTDeviceList.add(btDevice);
                }
            }
        }
    };

    /**
     * 获取连接设备
     *
     * @return
     */
    public Set<BluetoothDevice> getConnectedDevices() {
        return mConnectedBTDeviceList;
    }
}
