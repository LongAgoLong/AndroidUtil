package com.leo.commonutil.asyn.threadPool;

import com.leo.commonutil.callback.IProguard;

public interface ThreadPoolRunnable<T> extends IProguard {
    T run();
}
