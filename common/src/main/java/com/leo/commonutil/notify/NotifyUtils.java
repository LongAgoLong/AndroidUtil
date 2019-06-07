package com.leo.commonutil.notify;

import android.app.Service;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.annotation.RequiresPermission;

import com.leo.system.ContextHelp;
import com.leo.system.LogUtil;

import java.io.IOException;

/**
 * Create by LEO
 * on 2018/8/20
 * at 18:15
 * 消息提醒
 */
public final class NotifyUtils {
    private static final String TAG = NotifyUtils.class.getSimpleName();

    private NotifyUtils() {
    }

    /**
     * @param milliseconds 震动时长 , 单位毫秒
     */
    @RequiresPermission(android.Manifest.permission.VIBRATE)
    public static void vibrate(long milliseconds) {
        Vibrator vibrator = (Vibrator) ContextHelp.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (null != vibrator) {
            vibrator.vibrate(milliseconds);// 参数是震动时间(long类型)
        }
    }

    /**
     * @param pattern  自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]单位是毫秒
     * @param isRepeat true-> 反复震动，false-> 只震动一次
     */
    @RequiresPermission(android.Manifest.permission.VIBRATE)
    public static void vibrate(long[] pattern, boolean isRepeat) {
        Vibrator vibrator = (Vibrator) ContextHelp.getContext().getSystemService(Service.VIBRATOR_SERVICE);
        if (null != vibrator) {
            vibrator.vibrate(pattern, isRepeat ? 1 : -1);
        }
    }

    public static void playBee(@RawRes int mediaId, @Nullable OnCompleteListener listener) {
        AudioManager audioService = (AudioManager) ContextHelp.getContext()
                .getSystemService(Context.AUDIO_SERVICE);
        if (null == audioService) {
            return;
        }
        //检查当前是否是静音模式
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            LogUtil.i(TAG, "静音模式");
            return;
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(player -> player.seekTo(0));

        AssetFileDescriptor file = ContextHelp.getContext().getResources().openRawResourceFd(mediaId);
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
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp -> {
            mp.stop();
            if (null != listener) {
                listener.onCompletion(mp);
            }
        });
    }

    @RequiresPermission(android.Manifest.permission.VIBRATE)
    public static void playBeeAndVibrate(long milliseconds, @RawRes int mediaId, @Nullable OnCompleteListener listener) {
        //震动
        vibrate(milliseconds);
        //提示音
        playBee(mediaId, listener);
    }
}
