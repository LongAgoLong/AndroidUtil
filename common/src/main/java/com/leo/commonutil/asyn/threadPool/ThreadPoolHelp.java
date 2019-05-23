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
     * 传入一个在子线程中运行的方法
     *
     * @param runnable
     * @param <T>
     * @return
     */
    public <T> T submit(@NonNull ThreadPoolRunnable<T> runnable) {
        Future<T> future = IOUtil.getThreadPool().submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return runnable.run();
            }
        });
        try {
            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
