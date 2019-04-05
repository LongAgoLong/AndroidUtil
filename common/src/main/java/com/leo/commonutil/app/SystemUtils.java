package com.leo.commonutil.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.leo.commonutil.callback.OnEditTextClearFocusCallback;
import com.leo.commonutil.storage.SharedPreferencesUril;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by xy on 15/12/23.
 */
public final class SystemUtils {
    private SystemUtils() {
    }

    public static int getKeyboardHeight(Activity paramActivity) {
        int height = SystemUtils.getScreenHeight(paramActivity)
                - SystemUtils.getStatusBarHeight(paramActivity)
                - SystemUtils.getAppHeight(paramActivity);
        if (height == 0) {
            height = SharedPreferencesUril.getInstance().getInt(paramActivity, "KeyboardHeight",
                    dp2px(paramActivity, 295));//295dp-787为默认软键盘高度 基本差不离
        } else {
            SharedPreferencesUril.getInstance().put(paramActivity, "KeyboardHeight", height);
        }
        return height;
    }

    /**
     * 得到设备屏幕的高度
     **/
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * statusBar高度
     **/
    public static int getStatusBarHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;

    }

    /**
     * 可见屏幕高度
     **/
    public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }

    // below actionbar, above softkeyboard
    public static int getAppContentHeight(Activity paramActivity) {
        return SystemUtils.getScreenHeight(paramActivity) - SystemUtils.getStatusBarHeight(paramActivity)
                - SystemUtils.getActionBarHeight(paramActivity) - SystemUtils.getKeyboardHeight(paramActivity);
    }

    /**
     * 获取actiobar高度
     **/
    public static int getActionBarHeight(Activity paramActivity) {
        if (true) {
            return SystemUtils.dp2px(paramActivity, 48);
        }
        int[] attrs = new int[]{android.R.attr.actionBarSize};
        TypedArray ta = paramActivity.obtainStyledAttributes(attrs);
        return ta.getDimensionPixelSize(0, SystemUtils.dp2px(paramActivity, 48));
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 键盘是否在显示
     **/
    public static boolean isKeyBoardShow(Activity paramActivity) {
        int height = SystemUtils.getScreenHeight(paramActivity)
                - SystemUtils.getStatusBarHeight(paramActivity)
                - SystemUtils.getAppHeight(paramActivity);
        return height != 0;
    }

    /**
     * 显示键盘
     **/
    public static void showKeyBoard(Context mContext, final EditText paramEditText) {
        WeakReference<Context> weakReference = new WeakReference<>(mContext);
        paramEditText.requestFocus();
        paramEditText.post(() -> {
            Context context = weakReference.get();
            if (null == context) {
                return;
            }
            InputMethodManager imm = (InputMethodManager) context.getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != imm) {
                imm.showSoftInput(paramEditText, 0);
            }
        });
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 获得焦点的输入框
     * @param mContext  上下文
     */
    public static void hideSoftKeyboard(EditText mEditText, Context mContext) {
        try {
            mEditText.clearFocus();
            InputMethodManager imm = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != imm) {
                imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (null != inputMethodManager) {
                View currentFocus = activity.getCurrentFocus();
                if (null != currentFocus) {
                    inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置外部点击隐藏软键盘,传入根布局.
     *
     * @param context
     * @param view
     */
    public static void setCanceledOnTouchOutsideET(final Context context, View view) {
        setCanceledOnTouchOutsideET(context, view, null);
    }

    public static void setCanceledOnTouchOutsideET(final Context context, View view,
                                                   @Nullable final OnEditTextClearFocusCallback onEditTextClearFocusCallback) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                SystemUtils.hideSoftKeyboard((Activity) context);
                if (null != onEditTextClearFocusCallback) {
                    onEditTextClearFocusCallback.editTextClearFocusListener();
                }
                return false;
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setCanceledOnTouchOutsideET(context, innerView, onEditTextClearFocusCallback);
            }
        }
    }

    /**
     * 设置scrollview中的Edittext可滚动
     *
     * @param mEditEt
     * @param event
     */
    public static void setScrollEditText(EditText mEditEt, MotionEvent event) {
        if (canVerticalScroll(mEditEt)) {
            mEditEt.getParent().requestDisallowInterceptTouchEvent(true);//告诉父view，我的事件自己处理
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mEditEt.getParent().requestDisallowInterceptTouchEvent(false);//告诉父view，你可以处理了
            }
        }
    }

    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    public static boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight()
                - editText.getCompoundPaddingTop()
                - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;
        if (scrollDifference == 0) {
            return false;
        }
        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    /**
     * 逐个删除EditText的输入元素
     *
     * @param editText
     */
    public static void editTextDel(EditText editText) {
        int keyCode = KeyEvent.KEYCODE_DEL;
        KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        KeyEvent keyEventUp = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
        editText.onKeyDown(keyCode, keyEventDown);
        editText.onKeyUp(keyCode, keyEventUp);
    }

    /**
     * 是否启用了代理
     *
     * @param context
     * @return
     */
    public static boolean isWifiProxy(Context context) {
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    /**
     * 移除所有代理
     */
    public static void removeAllWifiProxy() {
        System.getProperties().remove("http.proxyHost");
        System.getProperties().remove("http.proxyPort");
        System.getProperties().remove("https.proxyHost");
        System.getProperties().remove("https.proxyPort");
    }

    /**
     * 更改状态栏
     *
     * @param activity
     * @param color
     */
    public static void setStatuBarColorRes(Activity activity, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && null != activity) {
            Window window = activity.getWindow();
            if (null != window) {
                window.setStatusBarColor(activity.getResources().getColor(color));
            }
        }
    }

    public static void setStatuBarColor(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && null != activity) {
            Window window = activity.getWindow();
            if (null != window) {
                window.setStatusBarColor(color);
            }
        }
    }

    /**
     * 安装APK
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        } else {
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 通知Media扫描
     * @param context
     * @param file
     */
    public static void notifyMediaScan(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//发送更新图片信息广播
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.getApplicationContext().sendBroadcast(intent);
    }

    /**
     * 权限检查
     *
     * @param context     上下文
     * @param permissions 权限数组
     * @return 返回检查结果
     */
    public static boolean checkPermissions(Context context, String... permissions) {
        if (null == permissions || permissions.length == 0)
            return true;
        boolean isGranted = true;
        for (String permission : permissions) {
            isGranted = ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
            if (!isGranted)
                break;
        }
        return isGranted;
    }
}
