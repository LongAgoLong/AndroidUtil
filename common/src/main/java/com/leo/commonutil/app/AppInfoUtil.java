package com.leo.commonutil.app;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.os.Process;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

/**
 * Create by LEO
 * on 2016/3/21
 * at 16:55
 */
public class AppInfoUtil {
    private AppInfoUtil() {
    }

    /**
     * 获取app名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        PackageManager pm = context.getPackageManager();
        return context.getApplicationInfo().loadLabel(pm).toString();
    }

    //获取当前版本号
    public static String getAppVersionName(Context context) {
        String versionName = "0";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getPhoneInfo(Context context) {
        return getPhoneInfo(context, true);
    }

    public static String getPhoneInfo(Context context, boolean withAppName) {
        if (withAppName) {
            return "手机型号:" + Build.MODEL + ",SDK版本:" + Build.VERSION.SDK_INT + ",系统版本:" + Build.VERSION.RELEASE + ",APP版本:" + getAppVersionName(context) + ",APP名称:" + getAppName(context);
        } else {
            return "手机型号:" + Build.MODEL + ",SDK版本:" + Build.VERSION.SDK_INT + ",系统版本:" + Build.VERSION.RELEASE + ",APP版本:" + getAppVersionName(context);
        }
    }

    //获取App安装包信息
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    //获取UDID
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

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /*
     * 判断对应包名app是否已经安装
     * */
    public final static String SINA_PACKAGE_NAME = "com.sina.weibo";//新浪微博包名
    public final static String QQ_PACKAGE_NAME = "com.tencent.mobileqq";//QQ包名
    public final static String TIM_PACKAGE_NAME = "com.tencent.tim";//TIM包名
    public final static String EIM_PACKAGE_NAME = "com.tencent.eim";//EIM(企业QQ)包名
    public final static String WECHAT_PACKAGE_NAME = "com.tencent.mm";//微信包名
    public final static String GAODEMAPAGENAME = "com.autonavi.minimap";//高德地图包名
    public final static String TENTEXMAPAGENAME = "com.tencent.map";//腾讯地图包名
    public final static String BAIDUMAPAGENAME = "com.baidu.BaiduMap";//百度地图包名
    public final static String GOOGLEMAPAGENAME = "com.google.android.apps.maps";//谷歌地图包名

    public static boolean isInstallAPP(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isInstallQQ(Context context) {
        return isInstallAPP(context, QQ_PACKAGE_NAME) || isInstallAPP(context, TIM_PACKAGE_NAME) || isInstallAPP(context, EIM_PACKAGE_NAME);
    }

    public static boolean goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean goToWechat(Context context) {
        try {
            Intent intent = new Intent();
            // 组件名称 用于打开其他应用程序中的Activity或服务
            ComponentName cmp = new ComponentName(WECHAT_PACKAGE_NAME, "com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据邮箱账号打开写邮件界面
     *
     * @param emailAccount 邮件账号
     * @param emailTitle   邮件标题
     * @param emailContent 邮件内容
     */
    public static boolean goToEmail(Context context, String emailAccount, String emailTitle, String emailContent) {
        try {
            // 必须明确使用mailto前缀来修饰邮件地址,如果使用
            Uri uri = Uri.parse("mailto:" + emailAccount);
            String[] email = {emailAccount};
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
            intent.putExtra(Intent.EXTRA_SUBJECT, !TextUtils.isEmpty(emailTitle) ? emailTitle : "请填写邮件主题"); // 主题
            intent.putExtra(Intent.EXTRA_TEXT, !TextUtils.isEmpty(emailContent) ? emailContent : "请填写邮件正文"); // 正文
            context.startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据QQ号打开聊天
     *
     * @param qqAccount 对方QQ号
     */
    public static boolean goToQQChat(Context context, String qqAccount) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqAccount + "&version=1")));
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
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
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /*
     * 是否主进程
     * */
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

    /*
     * 获取系统基带
     * */
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
        boolean isOpen = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
        /*
         * 该判断API是androidM以下的API,由于Android M中已经没有了关闭允许模拟位置的入口,所以这里一旦检测到开启了模拟位置,并且是android M以上,则
         * 默认设置为未有开启模拟位置
         * */
        if (isOpen && Build.VERSION.SDK_INT > 22) {
            isOpen = false;
        }
        return isOpen;
    }
}
