package com.leo.commonutil.app

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils

import androidx.annotation.AnimRes

import com.leo.commonutil.R
import com.leo.commonutil.callback.AnimationListenerAdapter
import com.leo.commonutil.callback.OnAnimEndCallback
import com.leo.system.context.ContextHelp

/**
 * Created by LEO
 * on 2018/12/24
 * view显示隐藏动画工具类
 */
object AnimUtil {
    private const val KEY_TAG = 2009

    /**
     * View 从底部滑出隐藏
     *
     * @param context
     * @param view
     * @param time
     */
    fun hideSlideBottom(context: Context, view: View?, time: Int) {
        if (null == view || time < 0) {
            return
        }
        val tag = view.getTag(KEY_TAG)
        if (null != tag && tag as Boolean) {
            return
        }
        view.setTag(KEY_TAG, true)
        val showAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_bottom_out)
        //设置动画时间
        showAnimation.duration = time.toLong()
        showAnimation.setAnimationListener(object : AnimationListenerAdapter() {
            override fun onAnimationEnd(animation: Animation) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
            }
        })
        view.startAnimation(showAnimation)
    }

    /**
     * View 从底部进入显示
     *
     * @param context
     * @param view
     * @param time
     */
    fun showSlideBottom(context: Context, view: View?, time: Int) {
        if (null == view || time < 0) {
            return
        }
        val tag = view.getTag(KEY_TAG)
        if (null != tag && !(tag as Boolean)) {
            return
        }
        view.setTag(KEY_TAG, false)
        val showAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_bottom_in)
        //设置动画时间
        showAnimation.duration = time.toLong()
        showAnimation.setAnimationListener(object : AnimationListenerAdapter() {
            override fun onAnimationStart(animation: Animation) {
                super.onAnimationStart(animation)
                view.visibility = View.VISIBLE
            }
        })
        view.startAnimation(showAnimation)
    }

    /**
     * View 从右部滑出隐藏
     *
     * @param context
     * @param view
     * @param time
     */
    fun hideSlideRight(context: Context, view: View?, time: Int) {
        if (null == view || time < 0) {
            return
        }
        val tag = view.getTag(KEY_TAG)
        if (null != tag && tag as Boolean) {
            return
        }
        view.setTag(KEY_TAG, true)
        val showAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_right_out)
        //设置动画时间
        showAnimation.duration = time.toLong()
        showAnimation.setAnimationListener(object : AnimationListenerAdapter() {
            override fun onAnimationEnd(animation: Animation) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
            }
        })
        view.startAnimation(showAnimation)
    }

    /**
     * View 从底部进入显示
     *
     * @param context
     * @param view
     * @param time
     */
    fun showSlideRight(context: Context, view: View?, time: Int) {
        if (null == view || time < 0) {
            return
        }
        val tag = view.getTag(KEY_TAG)
        if (null != tag && !(tag as Boolean)) {
            return
        }
        view.setTag(KEY_TAG, false)
        val showAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_right_in)
        //设置动画时间
        showAnimation.duration = time.toLong()
        showAnimation.setAnimationListener(object : AnimationListenerAdapter() {
            override fun onAnimationStart(animation: Animation) {
                super.onAnimationStart(animation)
                view.visibility = View.VISIBLE
            }
        })
        view.startAnimation(showAnimation)
    }

    /**
     * View 从淡出隐藏
     *
     * @param view
     * @param time
     */
    fun hideAlpha(view: View?, time: Int) {
        if (null == view || time < 0) {
            return
        }
        val tag = view.getTag(KEY_TAG)
        if (null != tag && tag as Boolean) {
            return
        }
        view.setTag(KEY_TAG, true)
        val alphaAnimation = AlphaAnimation(1f, 0f)
        alphaAnimation.fillAfter = true
        alphaAnimation.duration = time.toLong()
        alphaAnimation.setAnimationListener(object : AnimationListenerAdapter() {
            override fun onAnimationEnd(animation: Animation) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
            }
        })
        view.startAnimation(alphaAnimation)
    }

    /**
     * View 淡入显示
     *
     * @param view
     * @param time
     */
    fun showAlpha(view: View?, time: Int) {
        if (null == view || time < 0) {
            return
        }
        val tag = view.getTag(KEY_TAG)
        if (null != tag && !(tag as Boolean)) {
            return
        }
        view.setTag(KEY_TAG, false)
        val alphaAnimation = AlphaAnimation(0f, 1f)
        alphaAnimation.fillAfter = true
        alphaAnimation.duration = time.toLong()
        alphaAnimation.setAnimationListener(object : AnimationListenerAdapter() {
            override fun onAnimationStart(animation: Animation) {
                super.onAnimationStart(animation)
                view.visibility = View.VISIBLE
            }
        })
        view.startAnimation(alphaAnimation)
    }

    /**
     * View 隐藏
     *
     * @param view
     * @param time
     */
    @JvmOverloads
    fun hide(view: View?, time: Int, @AnimRes animRes: Int,
             onAnimEndCallback: OnAnimEndCallback? = null) {
        if (null == view || time < 0) {
            return
        }
        val tag = view.getTag(KEY_TAG)
        if (null != tag && tag as Boolean) {
            return
        }
        view.setTag(KEY_TAG, true)
        val showAnimation = AnimationUtils.loadAnimation(ContextHelp.context, animRes)
        showAnimation.duration = time.toLong()
        showAnimation.setAnimationListener(object : AnimationListenerAdapter() {
            override fun onAnimationEnd(animation: Animation) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
                onAnimEndCallback?.onAnimEnd()
            }
        })
        view.startAnimation(showAnimation)
    }

    /**
     * View 显示
     *
     * @param view
     * @param time
     */
    @JvmOverloads
    fun show(view: View?, time: Int, @AnimRes animRes: Int,
             onAnimEndCallback: OnAnimEndCallback? = null) {
        if (null == view || time < 0) {
            return
        }
        val tag = view.getTag(KEY_TAG)
        if (null != tag && !(tag as Boolean)) {
            return
        }
        view.setTag(KEY_TAG, false)
        val showAnimation = AnimationUtils.loadAnimation(ContextHelp.context, animRes)
        showAnimation.duration = time.toLong()
        showAnimation.setAnimationListener(object : AnimationListenerAdapter() {
            override fun onAnimationStart(animation: Animation) {
                super.onAnimationStart(animation)
                view.visibility = View.VISIBLE
                onAnimEndCallback?.onAnimEnd()
            }
        })
        view.startAnimation(showAnimation)
    }
}


