package com.leo.commonutil.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 
 * @author sunny TODO activity 之间跳转类
 */
public final class IntentUtil {
	/**
	 * void IntentUtil redirect TODO 执行跳转的方法
	 * 
	 * @param context
	 * @param cls
	 * @param finishSelf
	 *            跳转后是否结束自己
	 * @param bundle
	 *            要带到下一个activity的数据
	 */
	public static void redirect(Context context, Class<?> cls, boolean finishSelf, Bundle bundle) {
		redirect(context, cls, finishSelf, bundle, false, false);
	}

	/**
	 * 跳转到主页MainActivity
	 * 
	 * @param context
	 * @param cls
	 * @param finishSelf
	 * @param bundle
	 * @param clear_top
	 */
	public static void redirect(Context context, Class<?> cls, boolean finishSelf, Bundle bundle, boolean clear_top) {
		redirect(context, cls, finishSelf, bundle, true, true);
	}
	
	/**
	 * 
	 * @param context
	 * @param cls
	 * @param finishSelf
	 * @param bundle
	 * @param clear_top
	 *            是否clearTop
	 */
	public static void redirect(Context context, Class<?> cls, boolean finishSelf, Bundle bundle, boolean clear_top,
                                boolean single_top) {
		Intent it = new Intent();
		it.setClass(context, cls);
		if (clear_top) {
			it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		if (single_top) {
			it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		}
		if (bundle != null) {
			it.putExtras(bundle);
		}
		context.startActivity(it);
		if (finishSelf) {
			Activity activity = (Activity) context;
			activity.finish();
		}
		// 模仿大众点评网动画跳转效果
		// ((Activity) context).overridePendingTransition(R.anim.anim_enter,
		// R.anim.anim_exit);
		// ((Activity) context).overridePendingTransition(R.anim.slide_in_right,
		// R.anim.slide_out_right);
	}

	/**
	 * startActivityForResult
	 * 
	 * @param context
	 * @param cls
	 * @param bundle
	 * @param request_code
	 */
	public static void startActivityForResut(Context context, Class<?> cls, Bundle bundle, int request_code) {
		Intent intentRecharge = new Intent();
		if (bundle != null) {
			intentRecharge.putExtras(bundle);
		}
		intentRecharge.setClass(context, cls);
		((Activity) context).startActivityForResult(intentRecharge, request_code);
	}
}