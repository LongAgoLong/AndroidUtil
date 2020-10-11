package com.leo.system.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Proxy
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.os.PowerManager
import android.os.Process
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.leo.system.callback.OnETClearFocusCallback
import com.leo.system.context.ContextHelp.context
import com.leo.system.util.WindowUtils.getAppHeight
import com.leo.system.util.WindowUtils.getScreenHeight
import com.leo.system.util.WindowUtils.getStatusBarHeight
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.UnsupportedEncodingException
import java.lang.ref.WeakReference
import java.util.*

object SystemUtils {
    /**
     * 键盘是否在显示
     *
     * @param paramActivity
     * @return
     */
    fun isSoftKeyboardShow(paramActivity: Activity?): Boolean {
        val height = (getScreenHeight(paramActivity!!)
                - getStatusBarHeight(paramActivity)
                - getAppHeight(paramActivity))
        return height != 0
    }

    /**
     * 显示键盘
     *
     * @param mContext
     * @param paramEditText
     */
    fun showSoftKeyboard(mContext: Context, paramEditText: EditText) {
        val weakReference = WeakReference(mContext)
        paramEditText.requestFocus()
        paramEditText.post {
            val context = weakReference.get() ?: return@post
            val imm = context.applicationContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(paramEditText, 0)
        }
    }

    /**
     * 关闭软键盘
     *
     * @param mContext  上下文
     * @param mEditText 获得焦点的输入框
     */
    fun hideSoftKeyboard(mContext: Context, mEditText: EditText) {
        try {
            mEditText.clearFocus()
            val imm = mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(mEditText.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 关闭软键盘
     *
     * @param activity
     */
    fun hideSoftKeyboard(activity: Activity) {
        try {
            val inputMethodManager = activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if (null != inputMethodManager) {
                val currentFocus = activity.currentFocus
                if (null != currentFocus) {
                    inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置外部点击隐藏软键盘,传入根布局.
     *
     * @param context
     * @param view
     */
    fun setCanceledOnTouchOutsideET(context: Context, view: View) {
        setCanceledOnTouchOutsideET(context, view, null)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setCanceledOnTouchOutsideET(context: Context, view: View,
                                    onETClearFocusCallback: OnETClearFocusCallback?) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v: View?, event: MotionEvent? ->
                hideSoftKeyboard(context as Activity)
                onETClearFocusCallback?.editTextClearFocusListener()
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setCanceledOnTouchOutsideET(context, innerView, onETClearFocusCallback)
            }
        }
    }

    /**
     * 设置scrollview中的Edittext可滚动
     *
     * @param mEditEt
     * @param event
     */
    fun setScrollEditText(mEditEt: EditText, event: MotionEvent) {
        if (canVerticalScroll(mEditEt)) {
            // 告诉父view，我的事件自己处理
            mEditEt.parent.requestDisallowInterceptTouchEvent(true)
            if (event.action == MotionEvent.ACTION_UP) {
                // 告诉父view，你可以处理了
                mEditEt.parent.requestDisallowInterceptTouchEvent(false)
            }
        }
    }

    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    fun canVerticalScroll(editText: EditText): Boolean {
        // 滚动的距离
        val scrollY = editText.scrollY
        // 控件内容的总高度
        val scrollRange = editText.layout.height
        // 控件实际显示的高度
        val scrollExtent = (editText.height
                - editText.compoundPaddingTop
                - editText.compoundPaddingBottom)
        // 控件内容总高度与实际显示高度的差值
        val scrollDifference = scrollRange - scrollExtent
        return if (scrollDifference == 0) {
            false
        } else scrollY > 0 || scrollY < scrollDifference - 1
    }

    /**
     * 逐个删除EditText的输入元素
     *
     * @param editText
     */
    fun editTextDel(editText: EditText) {
        val keyCode = KeyEvent.KEYCODE_DEL
        val keyEventDown = KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
        val keyEventUp = KeyEvent(KeyEvent.ACTION_UP, keyCode)
        editText.onKeyDown(keyCode, keyEventDown)
        editText.onKeyUp(keyCode, keyEventUp)
    }

    /**
     * 是否启用了代理
     *
     * @param context
     * @return
     */
    fun isWifiProxy(context: Context?): Boolean {
        val IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
        val proxyAddress: String
        val proxyPort: Int
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost")
            val portStr = System.getProperty("http.proxyPort")
            proxyPort = (portStr ?: "-1").toInt()
        } else {
            proxyAddress = Proxy.getHost(context)
            proxyPort = Proxy.getPort(context)
        }
        return !TextUtils.isEmpty(proxyAddress) && proxyPort != -1
    }

    /**
     * 移除所有代理
     */
    fun removeAllWifiProxy() {
        System.getProperties().remove("http.proxyHost")
        System.getProperties().remove("http.proxyPort")
        System.getProperties().remove("https.proxyHost")
        System.getProperties().remove("https.proxyPort")
    }

    /**
     * 更改状态栏
     *
     * @param activity
     * @param color
     */
    fun setStatuBarColorRes(activity: Activity?, @ColorRes color: Int) {
        if (null == activity) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            if (null != window) {
                window.statusBarColor = activity.resources.getColor(color)
            }
        }
    }

    fun setStatuBarColor(activity: Activity?, @ColorInt color: Int) {
        if (null == activity) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            if (null != window) {
                window.statusBarColor = color
            }
        }
    }

    /**
     * 安装APK
     */
    fun installApk(context: Context, file: File?) {
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        } else {
            val apkUri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file!!)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        }
        context.startActivity(intent)
    }

    /**
     * 通知Media扫描
     *
     * @param context
     * @param file
     */
    fun notifyMediaScan(context: Context, file: File?) {
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE) //发送更新图片信息广播
        val uri = Uri.fromFile(file)
        intent.data = uri
        context.applicationContext.sendBroadcast(intent)
    }

    /**
     * 权限检查
     *
     * @param context     上下文
     * @param permissions 权限数组
     * @return 返回检查结果
     */
    @JvmStatic
    fun checkPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (null == permissions || permissions.size == 0) {
            return true
        }
        var isGranted = true
        for (permission in permissions) {
            isGranted = ActivityCompat.checkSelfPermission(context!!, permission!!) == PackageManager.PERMISSION_GRANTED
            if (!isGranted) break
        }
        return isGranted
    }

    private var lastClickTime: Long = 0

    //这里设置的时间间隔是2s-判断是否频繁点击
    val isFastClick: Boolean
        get() =//这里设置的时间间隔是2s-判断是否频繁点击
            isFastClick(1000)

    /**
     * @param intervalTime 时间间隔
     * @return
     */
    fun isFastClick(intervalTime: Int): Boolean { //判断是否频繁点击
        val time = System.currentTimeMillis()
        val timeD = time - lastClickTime
        if (0 < timeD && timeD < intervalTime) {
            return true
        }
        lastClickTime = time
        return false
    }

    /**
     * 是否主进程
     *
     * @param context
     * @return
     */
    fun isMainProcess(context: Context): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (null != am) {
            val processInfos = am.runningAppProcesses
            val mainProcessName = context.packageName
            val myPid = Process.myPid()
            for (info in processInfos) {
                if (info.pid == myPid && mainProcessName == info.processName) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 获取系统基带
     *
     * @return
     */
    @get:SuppressLint("PrivateApi")
    val systemBaseband: String
        get() {
            try {
                val cl = Class.forName("android.os.SystemProperties")
                val invoker = cl.newInstance()
                val m = cl.getMethod("get", *arrayOf<Class<*>>(String::class.java, String::class.java))
                val result = m.invoke(invoker, *arrayOf<Any>("gsm.version.baseband", ""))
                return result as String
            } catch (e: Exception) {
            }
            return ""
        }

    /**
     * 判断是否打开了允许虚拟位置
     */
    fun isAllowMockLocation(context: Activity): Boolean {
        var isOpen = Settings.Secure.getInt(context.contentResolver,
                Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0
        /**
         * 该判断API是androidM以下的API,由于Android M中已经没有了关闭允许模拟位置的入口,
         * 所以这里一旦检测到开启了模拟位置,并且是android M以上,则默认设置为未有开启模拟位置
         */
        if (isOpen && Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            isOpen = false
        }
        return isOpen
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    /**
     * 是否主线程
     *
     * @return
     */
    val isMainThread: Boolean
        get() = Looper.getMainLooper().thread === Thread.currentThread()

    /**
     * 获取服务是否开启
     */
    fun isServiceRunning(context: Context, className: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        // 获取正在运行的服务
        val runningServices = activityManager.getRunningServices(1000) //maxNum 返回正在运行的服务的上限个数,最多返回多少个服务
        for (runningServiceInfo in runningServices) {
            val service = runningServiceInfo.service
            // 获取正在运行的服务的全类名
            val className2 = service.className
            // 将获取到的正在运行的服务的全类名和传递过来的服务的全类名比较,
            // 一致表示服务正在运行返回true,不一致表示服务没有运行返回false
            if (className == className2) {
                return true
            }
        }
        return false
    }

    /**
     * 判断当前App是否处于后台
     *
     * @param context
     * @return
     */
    fun isAppBackground(context: Context): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.getRunningTasks(1)
        if (!tasks.isEmpty()) {
            val topActivity = tasks[0].topActivity
            if (null != topActivity
                    && topActivity.packageName != context.packageName) {
                return true
            }
        }
        return false
    }

    /**
     * 唤醒屏幕并解锁
     *
     * @param context
     */
    @SuppressLint("InvalidWakeLockTag", "WakelockTimeout")
    @RequiresPermission(Manifest.permission.DISABLE_KEYGUARD)
    fun wakeUpAndUnlock(context: Context) {
        try {
            val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            val kl = km.newKeyguardLock("unLock")
            //解锁
            kl.disableKeyguard()
            //获取电源管理器对象
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            val wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK, "bright")
            //点亮屏幕
            wl.acquire()
            //释放
            wl.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 获取UDID
     */
    const val PREFS_FILE = "gank_device_id.xml"
    const val PREFS_DEVICE_ID = "gank_device_id"
    var uuid: String? = null

    @SuppressLint("InvalidWakeLockTag", "HardwareIds")
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun getUDID(context: Context): String? {
        if (uuid == null) {
            synchronized(PREFS_FILE) {
                if (uuid == null) {
                    val prefs = context.applicationContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
                    val id = prefs.getString(PREFS_DEVICE_ID, null)
                    if (id != null) {
                        uuid = id
                    } else {
                        val androidId = Settings.Secure.getString(context.applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
                        uuid = try {
                            if ("9774d56d682e549c" != androidId) {
                                UUID.nameUUIDFromBytes(androidId.toByteArray(charset("utf8"))).toString()
                            } else {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                                    val deviceId = (context.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
                                    if (deviceId != null) UUID.nameUUIDFromBytes(deviceId.toByteArray(charset("utf8"))).toString() else UUID.randomUUID().toString()
                                } else {
                                    UUID.randomUUID().toString()
                                }
                            }
                        } catch (e: UnsupportedEncodingException) {
                            throw RuntimeException(e)
                        }
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid).commit()
                    }
                }
            }
        }
        return uuid
    }

    fun getStartAppWithPkgName(packagename: String?): String? {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        var packageinfo: PackageInfo? = null
        try {
            packageinfo = context.packageManager.getPackageInfo(packagename!!, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageinfo == null) {
            return null
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        resolveIntent.setPackage(packageinfo.packageName)

        // 通过getPackageManager()的queryIntentActivities方法遍历
        val resolveinfoList = context.packageManager
                .queryIntentActivities(resolveIntent, 0)
        val resolveinfo = resolveinfoList.iterator().next()
        if (resolveinfo != null) {
            // packagename = 参数packname
            val packageName = resolveinfo.activityInfo.packageName
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            return resolveinfo.activityInfo.name
        }
        return null
    }
}