package com.leo.commonutil.app;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.leo.commonutil.enume.PkgName;
import com.leo.system.ContextHelp;

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
     * @return
     */
    public static String getAppName() {
        Context context = ContextHelp.INSTANCE.getContext();
        PackageManager pm = context.getPackageManager();
        return context.getApplicationInfo().loadLabel(pm).toString();
    }

    /**
     * 获取当前版本号
     *
     * @return
     */
    public static String getAppVersionName() {
        Context context = ContextHelp.INSTANCE.getContext();
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

    public static String getInfo() {
        return getInfo(true);
    }

    public static String getInfo(boolean withAppName) {
        if (withAppName) {
            return "手机型号:" + Build.MODEL
                    + ",SDK版本:" + Build.VERSION.SDK_INT
                    + ",系统版本:" + Build.VERSION.RELEASE
                    + ",APP版本:" + getAppVersionName()
                    + ",APP名称:" + getAppName();
        } else {
            return "手机型号:" + Build.MODEL
                    + ",SDK版本:" + Build.VERSION.SDK_INT
                    + ",系统版本:" + Build.VERSION.RELEASE
                    + ",APP版本:" + getAppVersionName();
        }
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public static PackageInfo getPackageInfo() {
        Context context = ContextHelp.INSTANCE.getContext();
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        return info;
    }

    /**
     * 判断对应包名app是否已经安装
     *
     * @param packageName
     * @return
     */
    public static boolean isInstallAPP(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            ContextHelp.INSTANCE.getContext().getPackageManager()
                    .getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 是否安装了QQ
     *
     * @return
     */
    public static boolean isInstallQQ() {
        return isInstallAPP(PkgName.QQ_PACKAGE_NAME)
                || isInstallAPP(PkgName.TIM_PACKAGE_NAME)
                || isInstallAPP(PkgName.EIM_PACKAGE_NAME);
    }

    /**
     * 前往应用市场某个应用界面
     *
     * @param context
     * @param packageName
     * @return
     */
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

    /**
     * 打开微信
     *
     * @param context
     * @return
     */
    public static boolean goToWechat(Context context) {
        try {
            Intent intent = new Intent();
            // 组件名称 用于打开其他应用程序中的Activity或服务
            ComponentName cmp = new ComponentName(PkgName.WECHAT_PACKAGE_NAME,
                    "com.tencent.mm.ui.LauncherUI");
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
    public static boolean goToEmail(Context context, String emailAccount, String emailTitle,
                                    String emailContent) {
        try {
            // 必须明确使用mailto前缀来修饰邮件地址,如果使用
            Uri uri = Uri.parse("mailto:" + emailAccount);
            String[] email = {emailAccount};
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
            intent.putExtra(Intent.EXTRA_SUBJECT, !TextUtils.isEmpty(emailTitle)
                    ? emailTitle : "请填写邮件主题"); // 主题
            intent.putExtra(Intent.EXTRA_TEXT, !TextUtils.isEmpty(emailContent)
                    ? emailContent : "请填写邮件正文"); // 正文
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
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqAccount + "&version=1")));
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
