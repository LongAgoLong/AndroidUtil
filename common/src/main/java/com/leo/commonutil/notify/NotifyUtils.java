package com.leo.commonutil.notify;

import android.app.Service;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.annotation.RequiresPermission;

import java.io.IOException;

/**
 * Create by LEO
 * on 2018/8/20
 * at 18:15
 * 消息提醒
 */
public final class NotifyUtils {
    private NotifyUtils() {
    }

    /**
     * @param context      Context实例
     * @param milliseconds 震动时长 , 单位毫秒
     */
    @RequiresPermission(android.Manifest.permission.VIBRATE)
    public static void vibrate(Context context, long milliseconds) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (null != vibrator) {
            vibrator.vibrate(milliseconds);// 参数是震动时间(long类型)
        }
    }

    /**
     * @param context  Context实例
     * @param pattern  自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]单位是毫秒
     * @param isRepeat true-> 反复震动，false-> 只震动一次
     */
    @RequiresPermission(android.Manifest.permission.VIBRATE)
    public static void vibrate(Context context, long[] pattern, boolean isRepeat) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        if (null != vibrator) {
            vibrator.vibrate(pattern, isRepeat ? 1 : -1);
        }
    }

    public static void playBee(final Context context, @RawRes int mediaId, @Nullable OnPlayerCompleteListener listener) {
        AudioManager audioService = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (null == audioService) {
            return;
        }
        boolean shouldPlayBeep = true;
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            shouldPlayBeep = false;//检查当前是否是静音模式
        }

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(player -> player.seekTo(0));

        AssetFileDescriptor file = context.getResources().openRawResourceFd(mediaId);
        try {
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            file.close();
            mediaPlayer.setVolume(0, 1);
            mediaPlayer.prepare();
        } catch (IOException ioe) {
            mediaPlayer = null;
        }
        if (null == mediaPlayer) {
            return;
        }
        if (shouldPlayBeep) {
            mediaPlayer.start();
        }
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.stop();
            if (null != listener) {
                listener.onCompletion(mp);
            }
        });
    }

    @RequiresPermission(android.Manifest.permission.VIBRATE)
    public static void playBeeAndVibrate(final Context context, long milliseconds, @RawRes int mediaId, @Nullable OnPlayerCompleteListener listener) {
        //震动
        vibrate(context, milliseconds);
        //提示音
        playBee(context, mediaId, listener);
    }
}
