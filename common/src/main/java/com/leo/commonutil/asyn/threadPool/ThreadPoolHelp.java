package com.leo.commonutil.asyn.threadPool;

import android.support.annotation.NonNull;

import com.leo.commonutil.storage.IOUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 线程池
 */
public class ThreadPoolHelp {
    private ThreadPoolHelp() {
    }

    /**
     * 异步执行，同步获取返回值
     *
     * @param runnable
     * @param <T>
     * @return
     */
    public static <T> Future<T> submit(@NonNull Callable<T> runnable) {
        return IOUtil.getThreadPool().submit(runnable);
    }

    /**
     * 异步执行
     *
     * @param runnable
     */
    public static void execute(@NonNull Runnable runnable) {
        IOUtil.getThreadPool().execute(runnable);
    }
}
