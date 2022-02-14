package com.leo.system.util

import android.app.Activity
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.os.Build

object RecycleHelper {

    /**
     * 回收每一帧的图片，释放内存资源
     * 取出AnimationDrawable中的每一帧逐个回收，并且设置Callback为null
     * 回收完之后可以请求System.gc()回收
     *
     * @param animationDrawables Array<out AnimationDrawable?>
     */
    fun tryRecycleAnimDrawable(vararg animationDrawables: AnimationDrawable?) {
        if (animationDrawables.isNotEmpty()) {
            for (drawable in animationDrawables) {
                drawable ?: continue
                drawable.stop()
                try {
                    for (i in 0 until drawable.numberOfFrames) {
                        val frame = drawable.getFrame(i) ?: continue
                        if (frame is BitmapDrawable) {
                            frame.bitmap.recycle()
                        }
                        frame.callback = null
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                drawable.callback = null
            }
        }
    }

    /**
     * 判断Activity是否已经被回收或者正在被销毁
     *
     * @param activity
     * @return
     */
    fun isActivityRecycled(activity: Activity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
            && activity.isDestroyed
        ) {
            true
        } else activity.isFinishing
    }
}