package com.leo.commonutil.notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by LEO
 * on 2017/12/28.
 */
public class NotificationCompatUtil extends ContextWrapper {

    private NotificationManager manager;
    public static final String CHANNEL1_ID = "notify_1";
    public static final String CHANNEL1_NAME = "nofity_name_1";
    public static final String CHANNEL2_ID = "notify_2";
    public static final String CHANNEL2_NAME = "nofity_name_2";

    public NotificationCompatUtil(Context context) {
        super(context);
    }

    public static NotificationCompatUtil getInstance(Context context) {
        return new NotificationCompatUtil(context);
    }

    /**
     * 创建通知渠道
     *
     * @param sound 是否带提示音（通过提高消息等级实现）
     */
    public void createNotificationChannel(boolean sound) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel;
            if (sound) {
                channel = new NotificationChannel(CHANNEL2_ID, CHANNEL2_NAME, NotificationManager.IMPORTANCE_HIGH);
                channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
                channel.enableLights(true);
                channel.enableVibration(true);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);//设置在锁屏界面上显示这条通知
            } else {
                channel = new NotificationChannel(CHANNEL1_ID, CHANNEL1_NAME, NotificationManager.IMPORTANCE_LOW);
                channel.setImportance(NotificationManager.IMPORTANCE_LOW);
                channel.enableLights(false);
                channel.enableVibration(false);
            }
            channel.setShowBadge(true);
            getManager().createNotificationChannel(channel);
        }
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    //自定义布局根据通知栏背景色获取文本应适配颜色
    public static int getNotificationTextColor(Context context) {
        return isSimilarColor(Color.BLACK, getNotificationThemeColor(context)) ? Color.BLACK : Color.WHITE;
    }

    /**
     * 获取通知栏颜色
     */
    private static int getNotificationThemeColor(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Notification notification = builder.build();
        if (null != notification.contentView) {
            int layoutId = notification.contentView.getLayoutId();
            ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(layoutId, null, false);
            if (viewGroup.findViewById(android.R.id.title) != null) {
                return ((TextView) viewGroup.findViewById(android.R.id.title)).getCurrentTextColor();
            }
            return findColor(viewGroup);
        } else {
            return Color.BLACK;
        }
    }

    private static boolean isSimilarColor(int baseColor, int color) {
        int simpleBaseColor = baseColor | 0xff000000;
        int simpleColor = color | 0xff000000;
        int baseRed = Color.red(simpleBaseColor) - Color.red(simpleColor);
        int baseGreen = Color.green(simpleBaseColor) - Color.green(simpleColor);
        int baseBlue = Color.blue(simpleBaseColor) - Color.blue(simpleColor);
        double value = Math.sqrt(baseRed * baseRed + baseGreen * baseGreen + baseBlue * baseBlue);
        if (value < 180.0) {
            return true;
        }
        return false;
    }

    private static int findColor(ViewGroup viewGroupSource) {
        int color = Color.TRANSPARENT;
        LinkedList<ViewGroup> viewGroups = new LinkedList<>();
        viewGroups.add(viewGroupSource);
        while (viewGroups.size() > 0) {
            ViewGroup viewGroup1 = viewGroups.getFirst();
            for (int i = 0; i < viewGroup1.getChildCount(); i++) {
                if (viewGroup1.getChildAt(i) instanceof ViewGroup) {
                    viewGroups.add((ViewGroup) viewGroup1.getChildAt(i));
                } else if (viewGroup1.getChildAt(i) instanceof TextView) {
                    if (((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor() != -1) {
                        color = ((TextView) viewGroup1.getChildAt(i)).getCurrentTextColor();
                    }
                }
            }
            viewGroups.remove(viewGroup1);
        }
        return color;
    }

    /*
    * 普通样式
    * bigContent不为空-宽视图文字样式
    *
    * Class<?> cls为空不跳转
    * */
    public Notification createOrdinaryNotification(Context context, PendingIntent pendingIntent, @NonNull String title, @NonNull String content, @DrawableRes int iconBitmap, String bigContent, boolean sound) {
//        PendingIntent pendingIntent = null;
//        if (cls != null) {
//            Intent jumpIntent = new Intent(context, cls);
//            jumpIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            pendingIntent = PendingIntent.getActivity(context, 0, jumpIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        }
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(sound);
            Notification.Builder builder = new Notification.Builder(context, sound ? CHANNEL2_ID : CHANNEL1_ID);
            builder.setContentText(content)//设置内容
                    .setContentTitle(title) //设置标题
                    .setTicker(content) //设置状态栏的信息
                    .setSmallIcon(iconBitmap);
            if (!TextUtils.isEmpty(bigContent)) {//bigContent不为空-宽视图文字样式
                Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
                bigTextStyle.bigText(bigContent);
                bigTextStyle.setBigContentTitle(title);
                builder.setStyle(bigTextStyle);
                builder.setContentText(content);
            }
            if (null != pendingIntent) {
                builder.setContentIntent(pendingIntent);
            }
            notification = builder.build();
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, sound ? CHANNEL2_ID : CHANNEL1_ID);
            builder.setContentText(content)//设置内容
                    .setContentTitle(title) //设置标题
                    .setTicker(content) //设置状态栏的信息
                    .setSmallIcon(iconBitmap)
                    .setPriority(NotificationCompat.PRIORITY_MAX);//设置广播的优先级
            if (sound) {
                builder.setDefaults(Notification.DEFAULT_ALL);//设置通知的行为,例如声音,震动等
            }
            if (!TextUtils.isEmpty(bigContent)) {//bigContent不为空-宽视图文字样式
                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                bigTextStyle.bigText(bigContent);
                bigTextStyle.setBigContentTitle(title);
                builder.setStyle(bigTextStyle);
                builder.setContentText(content);
            }
            if (null != pendingIntent) {
                builder.setContentIntent(pendingIntent);
            }
            notification = builder.build();
        }
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        return notification;
    }

    /*
    * 宽视图图文样式
    * */
    public Notification createBigImgNotification(Context context, Class<?> cls, @NonNull String title, @NonNull String content, @DrawableRes int iconBitmap, @NonNull Bitmap bigBitmap) {
        return createBigImgNotification(context, cls, title, content, iconBitmap, bigBitmap, false);
    }

    public Notification createBigImgNotification(Context context, Class<?> cls, @NonNull String title, @NonNull String content, @DrawableRes int iconBitmap, @NonNull Bitmap bigBitmap, boolean sound) {
        PendingIntent pendingIntent = null;
        if (cls != null) {
            Intent jumpIntent = new Intent(context, cls);
            jumpIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, jumpIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(sound);
            Notification.Builder builder = new Notification.Builder(context, sound ? CHANNEL2_ID : CHANNEL1_ID);
            builder.setContentText(content)
                    .setContentTitle(title)
                    .setTicker(content) //设置状态栏的信息
                    .setSmallIcon(iconBitmap);
            Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle();
            bigPictureStyle.bigPicture(bigBitmap);
            builder.setStyle(bigPictureStyle);
            if (cls != null && null != pendingIntent) {
                builder.setContentIntent(pendingIntent);
            }
            notification = builder.build();
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, sound ? CHANNEL2_ID : CHANNEL1_ID);
            builder.setContentText(content)
                    .setContentTitle(title)
                    .setTicker(content) //设置状态栏的信息
                    .setSmallIcon(iconBitmap)
                    .setPriority(NotificationCompat.PRIORITY_MAX);//设置广播的优先级
            if (sound)
                builder.setDefaults(Notification.DEFAULT_ALL);//设置通知的行为,例如声音,震动等
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.bigPicture(bigBitmap);
            builder.setStyle(bigPictureStyle);
            if (cls != null && null != pendingIntent) {
                builder.setContentIntent(pendingIntent);
            }
            notification = builder.build();
        }
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        return notification;
    }

    /*
    * 自定义布局样式
    * */
    public Notification createCustomNotification(Context context, Class<?> cls, @NonNull String title, @NonNull String content, @DrawableRes int iconBitmap, @LayoutRes int layoutId) {
        return createCustomNotification(context, cls, title, content, iconBitmap, layoutId, false);
    }

    public Notification createCustomNotification(Context context, Class<?> cls, @NonNull String title, @NonNull String content, @DrawableRes int iconBitmap, @LayoutRes int layoutId, boolean sound) {
        PendingIntent pendingIntent = null;
        if (cls != null) {
            Intent jumpIntent = new Intent(context, cls);
            jumpIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, jumpIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layoutId);//自定义布局

        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(sound);
            Notification.Builder builder = new Notification.Builder(getApplicationContext(), sound ? CHANNEL2_ID : CHANNEL1_ID);
            builder.setContentText(content)
                    .setContentTitle(title)
                    .setTicker(content) //设置状态栏的信息
                    .setSmallIcon(iconBitmap);
            if (null != cls && null != pendingIntent) {
                builder.setContentIntent(pendingIntent);
            }
            builder.setCustomContentView(remoteViews);
            notification = builder.build();
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, sound ? CHANNEL2_ID : CHANNEL1_ID);
            builder.setContentText(content)
                    .setContentTitle(title)
                    .setTicker(content) //设置状态栏的信息
                    .setSmallIcon(iconBitmap)
                    .setPriority(NotificationCompat.PRIORITY_MAX);//设置广播的优先级
            if (sound)
                builder.setDefaults(Notification.DEFAULT_ALL);//设置通知的行为,例如声音,震动等
            if (null != cls && null != pendingIntent) {
                builder.setContentIntent(pendingIntent);
            }
            builder.setCustomContentView(remoteViews);
            //可以设置成折叠模式,但得判断一下sdk,大于16才能使用
            //if (Build.VERSION.SDK_INT > 16 && canCollapse)
            //builder.setCustomBigContentView(remoteViews);
            notification = builder.build();
        }

        notification.flags = Notification.FLAG_AUTO_CANCEL;
        return notification;
    }
}