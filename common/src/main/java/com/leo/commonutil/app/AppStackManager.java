package com.leo.commonutil.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Created by LEO on 2017/2/4.
 * 管理栈中的activity
 */
public class AppStackManager {

    private Stack<Activity> mStack;
    private ArrayList<Activity> mActivities;
    private static AppStackManager mInstance;

    protected AppStackManager() {
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

    /**
     * 获取栈底的Activity
     *
     * @return
     */
    public Activity getFirstActivity() {
        if (null == mStack || mStack.empty()) {
            return null;
        }
        return mStack.firstElement();
    }

    /**
     * 结束指定的Activity
     */
    public void killActivity(Activity activity) {
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
    public void killActivity(Class<?> cls) {
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

    /**
     * 结束指定Activity外其余Activity
     *
     * @param cls
     */
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
    public void killActivitys(ArrayList<Class<?>> activities) {
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
     * 判断某个activity是否在前台显示
     */
    public boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台,返回true，为显示,否则不是
     */
    public boolean isForeground(Activity context, String className) {
        if (context == null || TextUtils.isEmpty(className)) return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) return true;
        }
        return false;
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

    public boolean isActivityRecycler(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed()) {
                return true;
            }
        }
        return activity.isFinishing();
    }

    /**
     * 退出程序
     */
    public void exitApp() {
        killAllActivity();
        System.exit(0);
    }
}
