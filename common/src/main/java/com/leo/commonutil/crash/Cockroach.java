package com.leo.commonutil.crash;

import android.os.Handler;
import android.os.Looper;

import org.jetbrains.annotations.NotNull;

/**
 * Created by wanjian on 2017/2/14.
 */
public final class Cockroach {
    private static Cockroach mCockroach;
    private CrashHandler mCrashHandler;
    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;
    private boolean mInstalled = false;//标记位，避免重复安装卸载
    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    public interface CrashHandler {
        void handlerException(Thread thread, Throwable throwable);
    }

    private Cockroach() {
    }

    public static Cockroach getInstance() {
        if (null == mCockroach) {
            synchronized (Cockroach.class) {
                if (null == mCockroach) {
                    mCockroach = new Cockroach();
                }
            }
        }
        return mCockroach;
    }

    /**
     * 当主线程或子线程抛出异常时会调用exceptionHandler.handlerException(Thread thread, Throwable throwable)
     * <p>
     * crashHandler.handlerException可能运行在非UI线程中。
     * <p>
     * 若设置了Thread.setDefaultUncaughtExceptionHandler则可能无法捕获子线程异常。
     *
     * @param crashHandler
     */
    public synchronized void install(CrashHandler crashHandler) {
        if (mInstalled) {
            return;
        }
        mInstalled = true;
        mCrashHandler = crashHandler;
        mUIHandler.post(() -> {
            while (true) {
                try {
                    Looper.loop();
                } catch (Throwable e) {
                    if (e instanceof QuitCockroachException) {
                        return;
                    }
                    if (mCrashHandler != null) {
                        mCrashHandler.handlerException(Looper.getMainLooper().getThread(), e);
                    }
                }
            }
        });
        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 所有线程异常拦截，由于主线程的异常都被catch了，所以下面的代码拦截的都是子线程的异常
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NotNull Thread t, @NotNull Throwable e) {
                if (mCrashHandler != null) {
                    mCrashHandler.handlerException(t, e);
                }
            }
        });

    }

    public synchronized void uninstall() {
        if (!mInstalled) {
            return;
        }
        mInstalled = false;
        mCrashHandler = null;
        //卸载后恢复默认的异常处理逻辑，否则主线程再次抛出异常后将导致ANR，并且无法捕获到异常位置
        if (null != mUncaughtExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler);
        }
        mUIHandler.post(() -> {
            throw new QuitCockroachException("Quit Cockroach.....");//主线程抛出异常，迫使 while (true) {}结束
        });
    }
}