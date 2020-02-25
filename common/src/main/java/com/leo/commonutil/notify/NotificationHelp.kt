package com.leo.commonutil.notify

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.app.NotificationCompat
import java.util.*

/**
 * Created by LEO
 * on 2017/12/28.
 */
class NotificationHelp(context: Context) : ContextWrapper(context) {

    private var manager: NotificationManager? = null

    /**
     * 创建通知渠道
     *
     * @param isSound 是否带提示音（通过提高消息等级实现）
     */
    private fun createChannel(channelId: String, channelName: String, isSound: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel: NotificationChannel = NotificationChannel(channelId, channelName,
                    if (isSound) NotificationManager.IMPORTANCE_HIGH else NotificationManager.IMPORTANCE_LOW)
            if (isSound) {
                channel.importance = NotificationManager.IMPORTANCE_HIGH
                channel.enableLights(true)
                channel.enableVibration(true)
                // 设置在锁屏界面上显示这条通知
                channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            } else {
                channel.importance = NotificationManager.IMPORTANCE_LOW
                channel.enableLights(false)
                channel.enableVibration(false)
            }
            channel.setShowBadge(true)
            getManager()!!.createNotificationChannel(channel)
        }
    }

    private fun getManager(): NotificationManager? {
        return manager ?: synchronized(this) {
            manager ?: (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).also { manager = it }
        }
    }

    /**
     * 普通样式
     * bigContent不为空-宽视图文字样式
     * Class cls为空不跳转
     *
     * @param context
     * @param pendingIntent
     * @param title
     * @param content
     * @param iconBitmap
     * @param bigContent
     * @param isSound
     * @return
     */
    @JvmOverloads
    fun ordinaryNotification(context: Context, pendingIntent: PendingIntent?, title: String, content: String,
                             @DrawableRes iconBitmap: Int, bigContent: String,
                             channelId: String, channelName: String, isSound: Boolean = false): Notification {
        val notification: Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(channelId, channelName, isSound)
            val builder = Notification.Builder(context, channelId)
            builder.setContentText(content)//设置内容
                    .setContentTitle(title) //设置标题
                    .setTicker(content) //设置状态栏的信息
                    .setSmallIcon(iconBitmap)
            if (!TextUtils.isEmpty(bigContent)) {//bigContent不为空-宽视图文字样式
                val bigTextStyle = Notification.BigTextStyle()
                bigTextStyle.bigText(bigContent)
                bigTextStyle.setBigContentTitle(title)
                builder.style = bigTextStyle
                builder.setContentText(content)
            }
            if (null != pendingIntent) {
                builder.setContentIntent(pendingIntent)
            }
            notification = builder.build()
        } else {
            val builder = NotificationCompat.Builder(context, channelId)
            builder.setContentText(content)//设置内容
                    .setContentTitle(title) //设置标题
                    .setTicker(content) //设置状态栏的信息
                    .setSmallIcon(iconBitmap).priority = NotificationCompat.PRIORITY_MAX//设置广播的优先级
            if (isSound) {
                builder.setDefaults(Notification.DEFAULT_ALL)//设置通知的行为,例如声音,震动等
            }
            if (!TextUtils.isEmpty(bigContent)) {//bigContent不为空-宽视图文字样式
                val bigTextStyle = NotificationCompat.BigTextStyle()
                bigTextStyle.bigText(bigContent)
                bigTextStyle.setBigContentTitle(title)
                builder.setStyle(bigTextStyle)
                builder.setContentText(content)
            }
            if (null != pendingIntent) {
                builder.setContentIntent(pendingIntent)
            }
            notification = builder.build()
        }
        notification.flags = Notification.FLAG_AUTO_CANCEL
        return notification
    }

    @JvmOverloads
    fun bigImgNotification(context: Context, cls: Class<*>?, title: String, content: String,
                           @DrawableRes iconBitmap: Int, bigBitmap: Bitmap,
                           channelId: String, channelName: String, isSound: Boolean = false): Notification {
        var pendingIntent: PendingIntent? = null
        if (cls != null) {
            val jumpIntent = Intent(context, cls)
            jumpIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            pendingIntent = PendingIntent.getActivity(context, 0, jumpIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification: Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(channelId, channelName, isSound)
            val builder = Notification.Builder(context, channelId)
            builder.setContentText(content)
                    .setContentTitle(title)
                    .setTicker(content) // 设置状态栏的信息
                    .setSmallIcon(iconBitmap)
            val bigPictureStyle = Notification.BigPictureStyle()
            bigPictureStyle.bigPicture(bigBitmap)
            builder.style = bigPictureStyle
            if (cls != null && null != pendingIntent) {
                builder.setContentIntent(pendingIntent)
            }
            notification = builder.build()
        } else {
            val builder = NotificationCompat.Builder(context, channelId)
            builder.setContentText(content)
                    .setContentTitle(title)
                    .setTicker(content) // 设置状态栏的信息
                    .setSmallIcon(iconBitmap).priority = NotificationCompat.PRIORITY_MAX// 设置广播的优先级
            if (isSound) {
                // 设置通知的行为,例如声音,震动等
                builder.setDefaults(Notification.DEFAULT_ALL)
            }
            val bigPictureStyle = NotificationCompat.BigPictureStyle()
            bigPictureStyle.bigPicture(bigBitmap)
            builder.setStyle(bigPictureStyle)
            if (cls != null && null != pendingIntent) {
                builder.setContentIntent(pendingIntent)
            }
            notification = builder.build()
        }
        notification.flags = Notification.FLAG_AUTO_CANCEL
        return notification
    }

    @JvmOverloads
    fun customNotification(context: Context, cls: Class<*>?, title: String, content: String,
                           @DrawableRes iconBitmap: Int, @LayoutRes layoutId: Int,
                           channelId: String, channelName: String, isSound: Boolean = false): Notification {
        var pendingIntent: PendingIntent? = null
        if (cls != null) {
            val jumpIntent = Intent(context, cls)
            jumpIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            pendingIntent = PendingIntent.getActivity(context, 0, jumpIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        // 自定义布局
        val remoteViews = RemoteViews(context.packageName, layoutId)

        val notification: Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(channelId, channelName, isSound)
            val builder = Notification.Builder(applicationContext, channelId)
            builder.setContentText(content)
                    .setContentTitle(title)
                    .setTicker(content) // 设置状态栏的信息
                    .setSmallIcon(iconBitmap)
            if (null != cls && null != pendingIntent) {
                builder.setContentIntent(pendingIntent)
            }
            builder.setCustomContentView(remoteViews)
            notification = builder.build()
        } else {
            val builder = NotificationCompat.Builder(context, channelId)
            builder.setContentText(content)
                    .setContentTitle(title)
                    .setTicker(content) //设置状态栏的信息
                    .setSmallIcon(iconBitmap).priority = NotificationCompat.PRIORITY_MAX//设置广播的优先级
            if (isSound)
                builder.setDefaults(Notification.DEFAULT_ALL)//设置通知的行为,例如声音,震动等
            if (null != cls && null != pendingIntent) {
                builder.setContentIntent(pendingIntent)
            }
            builder.setCustomContentView(remoteViews)
            // 可以设置成折叠模式,但得判断一下sdk,大于16才能使用
            // if (Build.VERSION.SDK_INT > 16 && canCollapse)
            // builder.setCustomBigContentView(remoteViews);
            notification = builder.build()
        }

        notification.flags = Notification.FLAG_AUTO_CANCEL
        return notification
    }

    companion object {
        @Volatile
        private var instance: NotificationHelp? = null

        fun getInstance(context: Context): NotificationHelp {
            return instance ?: synchronized(this) {
                instance ?: NotificationHelp(context).also { instance = it }
            }
        }

        /**
         * 自定义布局根据通知栏背景色获取文本应适配颜色
         *
         * @param context
         * @return
         */
        fun getNotificationTextColor(context: Context): Int {
            return if (isSimilarColor(Color.BLACK, getNotificationThemeColor(context))) Color.BLACK else Color.WHITE
        }

        /**
         * 获取通知栏颜色
         */
        private fun getNotificationThemeColor(context: Context): Int {
            val builder = NotificationCompat.Builder(context)
            val notification = builder.build()
            return if (null != notification.contentView) {
                val layoutId = notification.contentView.layoutId
                val viewGroup = LayoutInflater.from(context).inflate(layoutId, null, false) as ViewGroup
                if (viewGroup.findViewById<View>(android.R.id.title) != null) {
                    (viewGroup.findViewById<View>(android.R.id.title) as TextView).currentTextColor
                } else findColor(viewGroup)
            } else {
                Color.BLACK
            }
        }

        private fun isSimilarColor(baseColor: Int, color: Int): Boolean {
            val simpleBaseColor = baseColor or -0x1000000
            val simpleColor = color or -0x1000000
            val baseRed = Color.red(simpleBaseColor) - Color.red(simpleColor)
            val baseGreen = Color.green(simpleBaseColor) - Color.green(simpleColor)
            val baseBlue = Color.blue(simpleBaseColor) - Color.blue(simpleColor)
            val value = Math.sqrt((baseRed * baseRed + baseGreen * baseGreen + baseBlue * baseBlue).toDouble())
            return value < 180.0
        }

        private fun findColor(viewGroupSource: ViewGroup): Int {
            var color = Color.TRANSPARENT
            val viewGroups = LinkedList<ViewGroup>()
            viewGroups.add(viewGroupSource)
            while (viewGroups.size > 0) {
                val viewGroup1 = viewGroups.first
                for (i in 0 until viewGroup1.childCount) {
                    if (viewGroup1.getChildAt(i) is ViewGroup) {
                        viewGroups.add(viewGroup1.getChildAt(i) as ViewGroup)
                    } else if (viewGroup1.getChildAt(i) is TextView) {
                        if ((viewGroup1.getChildAt(i) as TextView).currentTextColor != -1) {
                            color = (viewGroup1.getChildAt(i) as TextView).currentTextColor
                        }
                    }
                }
                viewGroups.remove(viewGroup1)
            }
            return color
        }
    }
}