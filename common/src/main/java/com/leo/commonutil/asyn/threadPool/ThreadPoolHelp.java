package com.leo.commonutil.asyn.threadPool;

import android.support.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程池辅助类
 */
public class ThreadPoolHelp {
    private static ExecutorService mThreadPool = Executors.newFixedThreadPool(5);

    public static ExecutorService getThreadPool() {
        return mThreadPool;
    }

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
        return getThreadPool().submit(runnable);
    }

    /**
     * 异步执行
     *
     * @param runnable
     */
    public static void execute(@NonNull Runnable runnable) {
        getThreadPool().execute(runnable);
    }
}
