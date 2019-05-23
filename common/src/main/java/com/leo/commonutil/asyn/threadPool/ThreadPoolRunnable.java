package com.leo.commonutil.asyn.threadPool;

import com.leo.commonutil.callback.IProguard;

import java.io.IOException;

public interface ThreadPoolRunnable<T> extends IProguard {
    T run() throws Exception;
}
