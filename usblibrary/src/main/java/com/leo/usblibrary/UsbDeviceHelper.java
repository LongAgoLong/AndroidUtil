package com.leo.usblibrary;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.ArrayMap;

import androidx.annotation.Nullable;

import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsbDeviceHelper {
    private static final String TAG = "UsbDeviceManager";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static Handler mMainHandler = new Handler(Looper.getMainLooper());
    private static volatile UsbDeviceHelper mInstance;
    private WeakReference<Context> mContextWrf;
    /**
     * U盘设备是否已经就绪
     */
    private volatile boolean mIsUDiskReady;
    /**
     * 设备列表
     */
    private final ArrayList<UsbMassStorageDevice> mDeviceList = new ArrayList<>();
    private IUsbDeviceChangedCallback mUsbDeviceChangedCallback;
    private IUsbInfoListener mUsbInfoListener;

    private UsbDeviceHelper() {
    }

    public static UsbDeviceHelper getInstance() {
        if (mInstance == null) {
            synchronized (UsbDeviceHelper.class) {
                if (mInstance == null) {
                    mInstance = new UsbDeviceHelper();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        this.mContextWrf = new WeakReference<>(context);
        // 监听otg插入 拔出
        IntentFilter usbDeviceStateFilter = new IntentFilter();
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        usbDeviceStateFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        context.registerReceiver(new UsbBroadcastReceiver(this), usbDeviceStateFilter);
    }

    private static class UsbBroadcastReceiver extends BroadcastReceiver {
        private final WeakReference<UsbDeviceHelper> managerWrf;

        public UsbBroadcastReceiver(UsbDeviceHelper manager) {
            this.managerWrf = new WeakReference<>(manager);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            UsbLog.i(TAG, "action: " + action);
            UsbDeviceHelper usbDeviceHelper = managerWrf.get();
            if (usbDeviceHelper == null) {
                UsbLog.e(TAG, "usbDeviceManager is null.");
                return;
            }
            switch (action) {
                case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                    // 接收到U盘设备插入广播
                    usbDeviceHelper.printInfo("U盘已插入");
                    UsbDevice deviceAdd = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (deviceAdd != null) {
                        // 接收到U盘插入广播，尝试读取U盘设备数据
                        usbDeviceHelper.redUDiskDevsList();
                        usbDeviceHelper.notifyUsbDeviceChange();
                    }
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED:
                    // 接收到U盘设设备拔出广播
                    usbDeviceHelper.printInfo("U盘已拔出");
                    usbDeviceHelper.redUDiskDevsList();
                    usbDeviceHelper.notifyUsbDeviceChange();
                    break;
                default:
                    break;
            }
        }
    }

    private synchronized boolean redUDiskDevsList() {
        // 设备管理器
        Context mContext = mContextWrf.get();
        if (mContext == null) {
            UsbLog.e(TAG, "redUDiskDevsList: mContext is null.");
            return false;
        }
        UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        // 获取U盘存储设备
        mDeviceList.clear();
        mDeviceList.addAll(Arrays.asList(UsbMassStorageDevice.getMassStorageDevices(mContext)));
        mIsUDiskReady = false;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,
                new Intent(ACTION_USB_PERMISSION), 0);
        // 一般手机只有1个OTG插口
        for (UsbMassStorageDevice device : mDeviceList) {
            // 读取设备是否有权限
            UsbDevice usbDevice = device.getUsbDevice();
            if (usbManager.hasPermission(usbDevice)) {
                mIsUDiskReady = true;
                UsbLog.i(TAG, "拥有权限设备: " + usbDevice.getDeviceName());
            } else {
                // 没有权限，进行申请
                usbManager.requestPermission(usbDevice, pendingIntent);
                UsbLog.i(TAG, "缺失权限设备: " + usbDevice.getDeviceName());
            }
        }
        if (mDeviceList.isEmpty()) {
            printInfo("请插入可用的U盘");
            mIsUDiskReady = false;
        }
        return mIsUDiskReady;
    }

    public FileSystem readDevice(int index) {
        if (!mIsUDiskReady || mDeviceList.isEmpty()) {
            printInfo("设备未就绪，请先检查Usb设备");
            return null;
        }
        if (index >= mDeviceList.size()) {
            printInfo("设备不存在，请先检查Usb设备");
            return null;
        }
        try {
            Context mContext = mContextWrf.get();
            if (mContext == null) {
                UsbLog.e(TAG, "readDevice: mContext is null.");
                return null;
            }
            UsbMassStorageDevice usbDevice = mDeviceList.get(index);
            UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
            if (!usbManager.hasPermission(usbDevice.getUsbDevice())) {
                printInfo("设备未取得权限，请授予权限");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,
                        new Intent(ACTION_USB_PERMISSION), 0);
                usbManager.requestPermission(usbDevice.getUsbDevice(), pendingIntent);
                return null;
            }
            // before interacting with a device you need to call init()!
            usbDevice.init();
            // Only uses the first partition on the device
            FileSystem currentFs = usbDevice.getPartitions().get(0).getFileSystem();
            UsbLog.d(TAG, "Capacity = " + currentFs.getCapacity()
                    + " ; Occupied Space = " + currentFs.getOccupiedSpace()
                    + " ; Free Space = " + currentFs.getFreeSpace()
                    + " ; Chunk size = " + currentFs.getChunkSize());
//            UsbFile rootDirectory = currentFs.getRootDirectory();
            return currentFs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 搜索文件
     *
     * @param targetFileName
     * @param rootDirectory
     * @param searchType
     * @param iUsbFileSearchRule
     * @return
     */
    public ArrayMap<String, List<UsbFile>> search(String targetFileName,
                                                  UsbFile rootDirectory,
                                                  @SearchType int searchType,
                                                  @Nullable IUsbFileSearchRule iUsbFileSearchRule) {
        ArrayMap<String, List<UsbFile>> result = new ArrayMap<>();
        search(targetFileName, rootDirectory, searchType, result, iUsbFileSearchRule);
        return result;
    }

    private void search(String targetFileName,
                        UsbFile cFolder,
                        @SearchType int searchType,
                        ArrayMap<String, List<UsbFile>> result,
                        @Nullable IUsbFileSearchRule iUsbFileSearchRule) {
        try {
            UsbFile[] usbFiles = cFolder.listFiles();
            if (usbFiles.length == 0) {
                return;
            }
            for (UsbFile file : usbFiles) {
                if (searchType == SearchType.FILE) {
                    if (file.isDirectory()) {
                        search(targetFileName, file, searchType, result, iUsbFileSearchRule);
                        continue;
                    }
                    collect(targetFileName, cFolder, file, result, iUsbFileSearchRule);
                } else if (searchType == SearchType.FOLDER) {
                    if (file.isDirectory()) {
                        collect(targetFileName, cFolder, file, result, iUsbFileSearchRule);
                        search(targetFileName, file, searchType, result, iUsbFileSearchRule);
                    }
                } else if (searchType == SearchType.ANY) {
                    collect(targetFileName, cFolder, file, result, iUsbFileSearchRule);
                    if (file.isDirectory()) {
                        search(targetFileName, file, searchType, result, iUsbFileSearchRule);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void collect(String targetFileName,
                         UsbFile cFolder,
                         UsbFile file,
                         ArrayMap<String, List<UsbFile>> result,
                         @Nullable IUsbFileSearchRule iUsbFileSearchRule) {
        if (iUsbFileSearchRule != null) {
            if (iUsbFileSearchRule.isMatch(targetFileName, file)) {
                String absolutePath = cFolder.getAbsolutePath();
                List<UsbFile> list = result.get(absolutePath);
                if (list == null) {
                    list = new ArrayList<>();
                    result.put(absolutePath, list);
                }
                list.add(file);
            }
        } else if (TextUtils.equals(file.getName(), targetFileName)) {
            String absolutePath = cFolder.getAbsolutePath();
            List<UsbFile> list = result.get(absolutePath);
            if (list == null) {
                list = new ArrayList<>();
                result.put(absolutePath, list);
            }
            list.add(file);
        }
    }

    /**
     * 拷贝文件到U盘
     *
     * @param cFolder 目标usb文件夹
     * @param file    文件
     * @return
     */
    public boolean copyFileToUsbDevice(UsbFile cFolder, File file) {
        FileInputStream fis = null;
        OutputStream os = null;
        try {
            fis = new FileInputStream(file);
            UsbFile usbFile = cFolder.createFile(file.getName());
            os = new UsbFileOutputStream(usbFile);
            byte[] buffer = new byte[1024];
            int byteRead;
            while ((byteRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, byteRead);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 拷贝文件夹到U盘
     *
     * @param folderPath       文件夹目录
     * @param targetFolderName 目标文件夹名称
     */
    public boolean copyFolderToUsbDevice(UsbFile cFolder, String folderPath,
                                         String targetFolderName) {
        printInfo("copyFolderToUsbDevice: folderPath = " + folderPath
                + " ; targetFolderName = " + targetFolderName);
        boolean flag = false;
        File sourceFolder = new File(folderPath);
        if (!sourceFolder.exists()) {
            printInfo("copyFolderToUsbDevice: 资源文件不存在");
            return false;
        }
        if (!sourceFolder.isDirectory()) {
            printInfo("copyFolderToUsbDevice: 资源非文件夹");
            return false;
        }
        try {
            // 获取根目录的文件
            UsbFile[] usbFiles = cFolder.listFiles();
            // 如果有文件则遍历文件列表
            if (usbFiles.length > 0) {
                for (UsbFile file : usbFiles) {
                    UsbLog.d(TAG, "file: " + file.getName());
                    if (file.isDirectory()
                            && TextUtils.equals(file.getName(), targetFolderName)) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                printInfo("拷贝文件夹失败，目标目录已存在");
                return false;
            }
            // 若所需文件未存在，创建所需文件
            UsbFile directory = cFolder.createDirectory(targetFolderName);
            UsbFile targetDirectory = directory.createDirectory(sourceFolder.getName());
            copyAll(targetDirectory, sourceFolder);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param directory 目标usb文件夹
     * @param folder    需要拷贝的资源文件夹
     * @throws IOException
     */
    private void copyAll(UsbFile directory, File folder) throws IOException {
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files) {
            if (file.isFile()) {
                copyFileToUsbDevice(directory, file);
            } else if (file.isDirectory()) {
                copyAll(directory.createDirectory(file.getName()), file);
            }
        }
    }

    public void setUsbDeviceChangedCallback(IUsbDeviceChangedCallback mUsbDeviceChangedCallback) {
        this.mUsbDeviceChangedCallback = mUsbDeviceChangedCallback;
    }

    public void setUsbInfoListener(IUsbInfoListener mUsbInfoListener) {
        this.mUsbInfoListener = mUsbInfoListener;
    }

    private void printInfo(String msg) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (mUsbInfoListener != null) {
                mUsbInfoListener.onUsbInfo(msg);
            } else {
                UsbLog.i(TAG, msg);
            }
        } else {
            mMainHandler.post(() -> {
                if (mUsbInfoListener != null) {
                    mUsbInfoListener.onUsbInfo(msg);
                } else {
                    UsbLog.i(TAG, msg);
                }
            });
        }
    }

    private void notifyUsbDeviceChange() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (mUsbDeviceChangedCallback != null) {
                mUsbDeviceChangedCallback.onUsbDeviceChanged(mDeviceList);
            }
        } else {
            mMainHandler.post(() -> {
                if (mUsbDeviceChangedCallback != null) {
                    mUsbDeviceChangedCallback.onUsbDeviceChanged(mDeviceList);
                }
            });
        }
    }
}
