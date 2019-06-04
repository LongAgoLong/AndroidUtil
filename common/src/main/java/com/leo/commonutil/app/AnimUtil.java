package com.leo.commonutil.app;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.leo.commonutil.R;
import com.leo.commonutil.callback.AnimationListenerAdapter;
import com.leo.commonutil.callback.OnAnimEndCallback;
import com.leo.system.ContextHelp;

/**
 * Created by LEO
 * on 2018/12/24
 * view显示隐藏动画工具类
 */
public final class AnimUtil {
    private AnimUtil() {
    }

    /**
     * View 从底部滑出隐藏
     *
     * @param context
     * @param view
     * @param time
     */
    public static void hideSlideBottom(Context context, final View view, int time) {
        if (null == view || time < 0) {
            return;
        }
        Animation showAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_bottom_out);
        //设置动画时间
        showAnimation.setDuration(time);
        showAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
        view.startAnimation(showAnimation);
    }

    /**
     * View 从底部进入显示
     *
     * @param context
     * @param view
     * @param time
     */
    public static void showSlideBottom(Context context, final View view, int time) {
        if (null == view || time < 0) {
            return;
        }
        Animation showAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_bottom_in);
        //设置动画时间
        showAnimation.setDuration(time);
        showAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationStart(Animation animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }
        });
        view.startAnimation(showAnimation);
    }

    /**
     * View 从右部滑出隐藏
     *
     * @param context
     * @param view
     * @param time
     */
    public static void hideSlideRight(Context context, final View view, int time) {
        if (null == view || time < 0) {
            return;
        }
        Animation showAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_right_out);
        //设置动画时间
        showAnimation.setDuration(time);
        showAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
        view.startAnimation(showAnimation);
    }

    /**
     * View 从底部进入显示
     *
     * @param context
     * @param view
     * @param time
     */
    public static void showSlideRight(Context context, final View view, int time) {
        if (null == view || time < 0) {
            return;
        }
        Animation showAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_right_in);
        //设置动画时间
        showAnimation.setDuration(time);
        showAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationStart(Animation animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }
        });
        view.startAnimation(showAnimation);
    }

    /**
     * View 从淡出隐藏
     *
     * @param view
     * @param time
     */
    public static void hideAlpha(final View view, int time) {
        if (null == view || time < 0) {
            return;
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(time);
        alphaAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
        view.startAnimation(alphaAnimation);
    }

    /**
     * View 淡入显示
     *
     * @param view
     * @param time
     */
    public static void showAlpha(final View view, int time) {
        if (null == view || time < 0) {
            return;
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(time);
        alphaAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationStart(Animation animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }
        });
        view.startAnimation(alphaAnimation);
    }

    /**
     * View 隐藏
     *
     * @param view
     * @param time
     */
    public static void hide(final View view, int time, @AnimRes int animRes) {
        hide(view, time, animRes, null);
    }

    public static void hide(final View view, int time, @AnimRes int animRes, @Nullable OnAnimEndCallback onAnimEndCallback) {
        if (null == view || time < 0) {
            return;
        }
        Animation showAnimation = AnimationUtils.loadAnimation(ContextHelp.getContext(), animRes);
        showAnimation.setDuration(time);
        showAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
                if (null != onAnimEndCallback) {
                    onAnimEndCallback.onAnimEnd();
                }
            }
        });
        view.startAnimation(showAnimation);
    }

    /**
     * View 显示
     *
     * @param view
     * @param time
     */
    public static void show(final View view, int time, @AnimRes int animRes) {
        show(view, time, animRes, null);
    }

    public static void show(final View view, int time, @AnimRes int animRes, @Nullable OnAnimEndCallback onAnimEndCallback) {
        if (null == view || time < 0) {
            return;
        }
        Animation showAnimation = AnimationUtils.loadAnimation(ContextHelp.getContext(), animRes);
        showAnimation.setDuration(time);
        showAnimation.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationStart(Animation animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
                if (null != onAnimEndCallback) {
                    onAnimEndCallback.onAnimEnd();
                }
            }
        });
        view.startAnimation(showAnimation);
    }
}
