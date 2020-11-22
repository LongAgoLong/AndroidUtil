package com.leo.commonutil.media.util

import android.content.Context
import android.media.AudioManager
import android.os.Build

object MediaUtils {
    /**
     * 时间格式转换
     *
     * @param millisecond
     * @param formatType
     * @return
     */
    fun timeFormat(millisecond: Int, @TimeMode formatType: String?): String {
        val minute = millisecond / (1000 * 60)
        val second = millisecond % (1000 * 60) / 1000
        return when (formatType) {
            TimeMode.MODE_SECOND ->  //转换成秒数
                "${minute * 60 + second}"
            TimeMode.MODE_FORMAT -> {
                //转换成时间格式
                val secondStr = if (second < 10) {
                    "0$second"
                } else {
                    second.toString()
                }
                val minuteStr = if (minute < 10) {
                    "0$minute"
                } else {
                    minute.toString()
                }
                "$minuteStr:$secondStr"
            }
            else -> ""
        }
    }

    /**
     * 切换音频播放模式
     *
     * @param context
     * @param playAction
     */
    fun switchAudioPlayAction(context: Context, @PlayAction playAction: Int) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                ?: return
        when (playAction) {
            PlayAction.SPEAKER -> {
                audioManager.mode = AudioManager.MODE_NORMAL
                audioManager.isSpeakerphoneOn = true
            }
            PlayAction.RECEIVER -> {
                audioManager.isSpeakerphoneOn = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
                } else {
                    audioManager.mode = AudioManager.MODE_IN_CALL
                }
            }
            PlayAction.HEADSET -> audioManager.isSpeakerphoneOn = false
            else -> {
            }
        }
    }
}