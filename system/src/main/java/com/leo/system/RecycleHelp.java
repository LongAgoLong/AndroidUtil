package com.leo.system;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;

public class RecycleHelp {
    private RecycleHelp() {
    }

    /**
     * 回收每一帧的图片，释放内存资源
     * 取出AnimationDrawable中的每一帧逐个回收，并且设置Callback为null
     * 回收完之后可以请求System.gc()回收
     *
     * @param animationDrawables
     */
    public static void tryRecycleAnimationDrawable(AnimationDrawable... animationDrawables) {
        if (animationDrawables != null && animationDrawables.length > 0) {
            for (AnimationDrawable drawable : animationDrawables) {
                if (null == drawable) {
                    continue;
                }
                drawable.stop();
                try {
                    for (int i = 0; i < drawable.getNumberOfFrames(); i++) {
                        Drawable frame = drawable.getFrame(i);
                        if (null == frame) {
                            continue;
                        }
                        if (frame instanceof BitmapDrawable) {
                            ((BitmapDrawable) frame).getBitmap().recycle();
                        }
                        frame.setCallback(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                drawable.setCallback(null);
            }
        }
    }

    /**
     * 判断Activity是否已经被回收或者正在被销毁
     *
     * @param activity
     * @return
     */
    public static boolean isActivityRecycled(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                && activity.isDestroyed()) {
            return true;
        }
        return activity.isFinishing();
    }
}