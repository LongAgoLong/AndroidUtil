package com.leo.commonutil.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by NyatoLEO on 2017/2/4.
 * 管理栈中的activity
 */
public class AppStackManager {

    private Stack<Activity> mStack;
    private ArrayList<Activity> mActivities;
    private static AppStackManager mInstance;

    private AppStackManager() {
    }

    public static AppStackManager getInstance() {
        if (mInstance == null) {
            synchronized (AppStackManager.class) {
                if (null == mInstance) {
                    mInstance = new AppStackManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 添加Activity到栈中
     */
    public void addActivity(Activity activity) {
        if (null == mStack) {
            mStack = new Stack<>();
        }
        mStack.add(activity);
    }

    /**
     * 获取当前Activity
     */
    public Activity getCurrentActivity() {
        if (null == mStack || mStack.empty()) {
            return null;
        }
        return mStack.lastElement();
    }

    /*
     * 获取栈底的Activity
     * */
    public Activity getFirstActivity() {
        if (null == mStack || mStack.empty()) {
            return null;
        }
        return mStack.firstElement();
    }

    /**
     * 结束指定的Activity
     */
    public void killSingleActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        if (null != mStack && !mStack.empty()) {
            mStack.remove(activity);
        }
        activity.finish();
    }

    /**
     * 结束指定的Activity
     */
    public void killSingleActivity(Class<?> cls) {
        try {
            if (null == mStack || mStack.empty()) {
                return;
            }
            Iterator<Activity> iterator = mStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (activity.getClass().equals(cls)) {
                    iterator.remove();
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 结束指定Activity外其余Activity
     * */
    public void keepOnlyActivity(Class<?> cls) {
        try {
            if (null == mStack || mStack.empty()) {
                return;
            }
            Iterator<Activity> iterator = mStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (!activity.getClass().equals(cls)) {
                    iterator.remove();
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void keepOnlyActivity(Activity activity) {
        if (null == activity) {
            return;
        }
        try {
            if (null == mStack || mStack.empty()) {
                return;
            }
            Iterator<Activity> iterator = mStack.iterator();
            while (iterator.hasNext()) {
                Activity act = iterator.next();
                if (act != activity) {
                    iterator.remove();
                    act.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束多个Activity
     */
    public void killMoreActivity(ArrayList<Class<?>> activities) {
        if (null == mStack || mStack.empty()) {
            return;
        }
        if (mActivities == null) {
            mActivities = new ArrayList<>();
        }
        for (Class<?> cls : activities) {
            Activity activity = isHere(cls);
            if (activity != null) {
                mActivities.add(activity);
            }
        }
        mStack.removeAll(mActivities);
        for (Activity activity : mActivities) {
            activity.finish();
        }
        mActivities.clear();
    }

    /**
     * 是否在栈中
     */
    public Activity isHere(Class<?> cls) {
        if (null == mStack || mStack.empty()) {
            return null;
        }
        for (Activity activity : mStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 结束所有
     */
    public void killAllActivity() {
        if (null == mStack || mStack.empty()) {
            return;
        }
        for (Activity activity : mStack) {
            activity.finish();
        }
        mStack.clear();
    }

    /**
     * 退出程序
     */
    public void exitApp() {
        killAllActivity();
        System.exit(0);
    }
}
