package com.leo.usblibrary;

import com.github.mjdev.libaums.fs.UsbFile;

public interface IUsbFileSearchRule {
    boolean isMatch(String targetFileName, UsbFile usbFile);
}
