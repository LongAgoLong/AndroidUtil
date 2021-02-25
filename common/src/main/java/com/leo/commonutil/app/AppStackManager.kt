package com.leo.commonutil.app

import android.app.Activity
import android.text.TextUtils
import android.app.ActivityManager
import android.app.ActivityManager.RunningTaskInfo
import android.content.ComponentName
import android.content.Context
import android.os.Build
import com.leo.commonutil.app.AppStackManager
import java.lang.Exception
import java.util.*

/**
 * Created by LEO on 2017/2/4.
 * 管理栈中的activity
 */
class AppStackManager protected constructor() {
    private var mStack: Stack<Activity>? = null
    private var mActivities: ArrayList<Activity>? = null

    /**
     * 添加Activity到栈中
     */
    fun addActivity(activity: Activity) {
        if (null == mStack) {
            mStack = Stack()
        }
        mStack!!.add(activity)
    }

    /**
     * 获取当前Activity
     */
    val currentActivity: Activity?
        get() = if (null == mStack || mStack!!.empty()) {
            null
        } else mStack!!.lastElement()

    /**
     * 获取栈底的Activity
     *
     * @return
     */
    val firstActivity: Activity?
        get() = if (null == mStack || mStack!!.empty()) {
            null
        } else mStack!!.firstElement()

    /**
     * 结束指定的Activity
     */
    fun killActivity(activity: Activity?) {
        if (activity == null) {
            return
        }
        if (null != mStack && !mStack!!.empty()) {
            mStack!!.remove(activity)
        }
        activity.finish()
    }

    /**
     * 结束指定的Activity
     */
    fun killActivity(cls: Class<*>) {
        try {
            if (null == mStack || mStack!!.empty()) {
                return
            }
            val iterator = mStack!!.iterator()
            while (iterator.hasNext()) {
                val activity = iterator.next()
                if (activity.javaClass == cls) {
                    iterator.remove()
                    activity.finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 结束多个Activity
     */
    fun killActivities(activities: ArrayList<Class<*>>) {
        if (null == mStack || mStack!!.empty()) {
            return
        }
        if (mActivities == null) {
            mActivities = ArrayList()
        }
        for (cls in activities) {
            val activity = isHere(cls)
            if (activity != null) {
                mActivities!!.add(activity)
            }
        }
        mStack!!.removeAll(mActivities!!)
        for (activity in mActivities!!) {
            activity.finish()
        }
        mActivities!!.clear()
    }

    /**
     * 结束指定Activity外其余Activity
     *
     * @param cls
     */
    fun keepOnlyActivity(cls: Class<*>) {
        try {
            if (null == mStack || mStack!!.empty()) {
                return
            }
            val iterator = mStack!!.iterator()
            while (iterator.hasNext()) {
                val activity = iterator.next()
                if (activity.javaClass != cls) {
                    iterator.remove()
                    activity.finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun keepOnlyActivity(activity: Activity?) {
        if (null == activity) {
            return
        }
        try {
            if (null == mStack || mStack!!.empty()) {
                return
            }
            val iterator = mStack!!.iterator()
            while (iterator.hasNext()) {
                val act = iterator.next()
                if (act !== activity) {
                    iterator.remove()
                    act.finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 判断某个activity是否在前台显示
     */
    fun isForeground(activity: Activity): Boolean {
        return isForeground(activity, activity.javaClass.name)
    }

    /**
     * 判断某个界面是否在前台,返回true，为显示,否则不是
     */
    fun isForeground(context: Activity?, className: String): Boolean {
        if (context == null || TextUtils.isEmpty(className)) return false
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = am.getRunningTasks(1)
        if (list != null && list.size > 0) {
            val cpn = list[0].topActivity
            if (className == cpn!!.className) return true
        }
        return false
    }

    /**
     * 是否在栈中
     */
    fun isHere(cls: Class<*>): Activity? {
        if (null == mStack || mStack!!.empty()) {
            return null
        }
        for (activity in mStack!!) {
            if (activity.javaClass == cls) {
                return activity
            }
        }
        return null
    }

    /**
     * 结束所有
     */
    fun killAllActivity() {
        if (null == mStack || mStack!!.empty()) {
            return
        }
        for (activity in mStack!!) {
            activity.finish()
        }
        mStack!!.clear()
    }

    fun isActivityRecycler(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity.isDestroyed || activity.isFinishing) {
                return true
            }
        }
        return activity.isFinishing
    }

    /**
     * 退出程序
     */
    fun exitApp() {
        killAllActivity()
        System.exit(0)
    }

    companion object {
        private var mInstance: AppStackManager? = null
        val instance: AppStackManager?
            get() {
                if (mInstance == null) {
                    synchronized(AppStackManager::class.java) {
                        if (null == mInstance) {
                            mInstance = AppStackManager()
                        }
                    }
                }
                return mInstance
            }
    }
}