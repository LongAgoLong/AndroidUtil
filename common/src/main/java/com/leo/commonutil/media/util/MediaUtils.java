package com.leo.commonutil.media.util;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

public final class MediaUtils {
    public static String timeFormat(int millisecond, @TimeMode String formatType) {
        int minute = millisecond / (1000 * 60);
        int second = millisecond % (1000 * 60) / 1000;
        switch (formatType) {
            case TimeMode.MODE_SECOND:
                //转换成秒数
                return minute * 60 + second + "";
            case TimeMode.MODE_FORMAT:
                //转换成时间格式
                String secondStr;
                if (second < 10) {
                    secondStr = "0" + second;
                } else {
                    secondStr = String.valueOf(second);
                }
                String minuteStr;
                if (minute < 10) {
                    minuteStr = "0" + minute;
                } else {
                    minuteStr = String.valueOf(minute);
                }
                return minuteStr + ":" + secondStr;
            default:
                return "";
        }
    }

    /*
     * 切换音频播放模式
     * */
    public static void switchAudioPlayAction(Context context, @PlayAction int playAction) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (null == audioManager) {
            return;
        }
        switch (playAction) {
            case PlayAction.SPEAKER:
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.setSpeakerphoneOn(true);
                break;
            case PlayAction.RECEIVER:
                audioManager.setSpeakerphoneOn(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                } else {
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                }
                break;
            case PlayAction.HEADSET:
                audioManager.setSpeakerphoneOn(false);
                break;
            default:
                break;
        }
    }
}
