package com.leo.commonutil.app

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.TextUtils

import com.leo.commonutil.enume.PkgName
import com.leo.system.context.ContextHelper

/**
 * Create by LEO
 * on 2016/3/21
 * at 16:55
 */
object AppInfoUtil {

    /**
     * 获取app名称
     *
     * @return
     */
    val appName: String
        get() {
            val context = ContextHelper.context
            val pm = context.packageManager
            return context.applicationInfo.loadLabel(pm).toString()
        }

    /**
     * 获取当前版本号
     *
     * @return
     */
    val appVersionName: String
        get() {
            val context = ContextHelper.context
            var versionName = "0"
            try {
                val packageManager = context.packageManager
                val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
                versionName = packageInfo.versionName
                if (TextUtils.isEmpty(versionName)) {
                    return ""
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return versionName
        }

    val info: String
        get() = getInfo(true)

    /**
     * 获取App安装包信息
     *
     * @return
     */
    val packageInfo: PackageInfo?
        get() {
            val context = ContextHelper.context
            var info: PackageInfo? = null
            try {
                info = context.packageManager.getPackageInfo(context.packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace(System.err)
            }

            return info
        }

    /**
     * 是否安装了QQ
     *
     * @return
     */
    val isInstallQQ: Boolean
        get() = (isInstallAPP(PkgName.PKG_QQ)
                || isInstallAPP(PkgName.PKG_TIM)
                || isInstallAPP(PkgName.PKG_EIM))

    fun getInfo(withAppName: Boolean): String {
        return if (withAppName) {
            ("手机型号:" + Build.MODEL
                    + ",SDK版本:" + Build.VERSION.SDK_INT
                    + ",系统版本:" + Build.VERSION.RELEASE
                    + ",APP版本:" + appVersionName
                    + ",APP名称:" + appName)
        } else {
            ("手机型号:" + Build.MODEL
                    + ",SDK版本:" + Build.VERSION.SDK_INT
                    + ",系统版本:" + Build.VERSION.RELEASE
                    + ",APP版本:" + appVersionName)
        }
    }

    /**
     * 判断对应包名app是否已经安装
     *
     * @param packageName
     * @return
     */
    fun isInstallAPP(packageName: String): Boolean {
        if (TextUtils.isEmpty(packageName)) {
            return false
        }
        return try {
            ContextHelper.context.packageManager
                    .getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /**
     * 前往应用市场某个应用界面
     *
     * @param context
     * @param packageName
     * @return
     */
    fun goToMarket(context: Context, packageName: String): Boolean {
        val uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        return try {
            context.startActivity(goToMarket)
            true
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 打开微信
     *
     * @param context
     * @return
     */
    fun goToWechat(context: Context): Boolean {
        try {
            val intent = Intent()
            // 组件名称 用于打开其他应用程序中的Activity或服务
            val cmp = ComponentName(PkgName.PKG_WECHAT,
                    "com.tencent.mm.ui.LauncherUI")
            intent.action = Intent.ACTION_MAIN
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.component = cmp
            context.startActivity(intent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 根据邮箱账号打开写邮件界面
     *
     * @param emailAccount 邮件账号
     * @param emailTitle   邮件标题
     * @param emailContent 邮件内容
     */
    fun goToEmail(context: Context, emailAccount: String, emailTitle: String,
                  emailContent: String): Boolean {
        try {
            // 必须明确使用mailto前缀来修饰邮件地址,如果使用
            val uri = Uri.parse("mailto:$emailAccount")
            val email = arrayOf(emailAccount)
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra(Intent.EXTRA_CC, email) // 抄送人
            intent.putExtra(Intent.EXTRA_SUBJECT, if (!TextUtils.isEmpty(emailTitle))
                emailTitle
            else
                "请填写邮件主题") // 主题
            intent.putExtra(Intent.EXTRA_TEXT, if (!TextUtils.isEmpty(emailContent))
                emailContent
            else
                "请填写邮件正文") // 正文
            context.startActivity(Intent.createChooser(intent, "请选择书写邮件应用"))
            return true
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            return false
        }

    }

    /**
     * 根据QQ号打开聊天
     *
     * @param qqAccount 对方QQ号
     */
    fun goToQQChat(context: Context, qqAccount: String): Boolean {
        return try {
            context.startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=$qqAccount&version=1")))
            true
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
            false
        }
    }
}
