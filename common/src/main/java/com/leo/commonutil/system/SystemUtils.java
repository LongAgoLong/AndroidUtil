package com.leo.commonutil.system;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.leo.commonutil.callback.OnETClearFocusCallback;
import com.leo.commonutil.storage.IOUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import static com.leo.commonutil.system.WindowUtils.getAppHeight;
import static com.leo.commonutil.system.WindowUtils.getScreenHeight;
import static com.leo.commonutil.system.WindowUtils.getStatusBarHeight;

public final class SystemUtils {
    private SystemUtils() {
    }

    /**
     * 键盘是否在显示
     **/
    public static boolean isKeyBoardShow(Activity paramActivity) {
        int height = getScreenHeight(paramActivity)
                - getStatusBarHeight(paramActivity)
                - getAppHeight(paramActivity);
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
     * @param mContext  上下文
     * @param mEditText 获得焦点的输入框
     */
    public static void hideSoftKeyboard(Context mContext, EditText mEditText) {
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
        } catch (Exception e) {
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
                                                   @Nullable final OnETClearFocusCallback onETClearFocusCallback) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                SystemUtils.hideSoftKeyboard((Activity) context);
                if (null != onETClearFocusCallback) {
                    onETClearFocusCallback.editTextClearFocusListener();
                }
                return false;
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setCanceledOnTouchOutsideET(context, innerView, onETClearFocusCallback);
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
     *
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

    private static long lastClickTime;

    public static boolean isFastClick() {//这里设置的时间间隔是2s-判断是否频繁点击
        return isFastClick(1000);
    }

    /**
     * @param intervalTime 时间间隔
     * @return
     */
    public static boolean isFastClick(int intervalTime) {//判断是否频繁点击
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < intervalTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 是否主进程
     *
     * @param context
     * @return
     */
    public static boolean isMainProcess(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        if (null != am) {
            List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
            String mainProcessName = context.getPackageName();
            int myPid = Process.myPid();
            for (ActivityManager.RunningAppProcessInfo info : processInfos) {
                if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取系统基带
     *
     * @return
     */
    public static String getSystemBaseband() {
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[]{String.class, String.class});
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", ""});
            return (String) result;
        } catch (Exception e) {

        }
        return "";
    }

    /**
     * 判断是否打开了允许虚拟位置
     */
    public static boolean isAllowMockLocation(final Activity context) {
        boolean isOpen = Settings.Secure.getInt(context.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
        /*
         * 该判断API是androidM以下的API,由于Android M中已经没有了关闭允许模拟位置的入口,所以这里一旦检测到开启了模拟位置,并且是android M以上,则
         * 默认设置为未有开启模拟位置
         * */
        if (isOpen && Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            isOpen = false;
        }
        return isOpen;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            IOUtil.closeQuietly(reader);
        }
        return null;
    }

    /**
     * 是否主线程
     *
     * @return
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /**
     * 获取服务是否开启
     */
    public static boolean isServiceRunning(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取正在运行的服务
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);//maxNum 返回正在运行的服务的上限个数,最多返回多少个服务
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            ComponentName service = runningServiceInfo.service;
            //获取正在运行的服务的全类名
            String className2 = service.getClassName();
            //将获取到的正在运行的服务的全类名和传递过来的服务的全类名比较,一直表示服务正在运行  返回true,不一致表示服务没有运行  返回false
            if (className.equals(className2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前App是否处于后台
     *
     * @param context
     * @return
     */
    public static boolean isApplicationBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 唤醒屏幕并解锁
     *
     * @param context
     */
    @RequiresPermission(Manifest.permission.DISABLE_KEYGUARD)
    public static void wakeUpAndUnlock(Context context) {
        try {
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
            //解锁
            kl.disableKeyguard();
            //获取电源管理器对象
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
            //点亮屏幕
            wl.acquire();
            //释放
            wl.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取UDID
     */
    protected static final String PREFS_FILE = "gank_device_id.xml";
    protected static final String PREFS_DEVICE_ID = "gank_device_id";
    protected static String uuid;

    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getUDID(Context context) {
        if (uuid == null) {
            synchronized (PREFS_FILE) {
                if (uuid == null) {
                    final SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {
                        uuid = id;
                    } else {
                        final String androidId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
                            } else {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                                    final String deviceId = ((TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                    uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString() : UUID.randomUUID().toString();
                                } else {
                                    uuid = UUID.randomUUID().toString();
                                }
                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid).commit();
                    }
                }
            }
        }
        return uuid;
    }
}
