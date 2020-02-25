package com.leo.commonutil.notify

import android.app.Service
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Vibrator
import androidx.annotation.RawRes
import androidx.annotation.RequiresPermission
import com.leo.system.ContextHelp
import com.leo.system.LogUtil
import java.io.IOException

/**
 * Create by LEO
 * on 2018/8/20
 * at 18:15
 * 消息提醒
 */
object NotifyHelp {
    private val TAG = NotifyHelp::class.java.simpleName

    /**
     * @param milliseconds 震动时长 , 单位毫秒
     */
    @RequiresPermission(android.Manifest.permission.VIBRATE)
    fun vibrate(milliseconds: Long) {
        val vibrator = ContextHelp.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(milliseconds)
    }

    /**
     * @param pattern  自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]单位是毫秒
     * @param isRepeat true-> 反复震动，false-> 只震动一次
     */
    @RequiresPermission(android.Manifest.permission.VIBRATE)
    @JvmOverloads
    fun vibrate(pattern: LongArray, isRepeat: Boolean = false) {
        val vibrator = ContextHelp.context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(pattern, if (isRepeat) 1 else -1)
    }

    @JvmOverloads
    fun playBee(@RawRes mediaId: Int, listener: OnCompleteListener? = null) {
        val audioService = ContextHelp.context
                .getSystemService(Context.AUDIO_SERVICE) as AudioManager ?: return
        // 检查当前是否是静音模式
        if (audioService.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            LogUtil.d(TAG, "静音模式")
            return
        }
        var mediaPlayer: MediaPlayer? = MediaPlayer()
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mediaPlayer.setOnCompletionListener { player -> player.seekTo(0) }

        val file = ContextHelp.context.resources.openRawResourceFd(mediaId)
        try {
            mediaPlayer.setDataSource(file.fileDescriptor, file.startOffset, file.length)
            file.close()
            mediaPlayer.setVolume(0f, 1f)
            mediaPlayer.prepare()
        } catch (ioe: IOException) {
            mediaPlayer = null
        }
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { mp ->
            mp.stop()
            listener?.onCompletion(mp)
        }
    }

    @RequiresPermission(android.Manifest.permission.VIBRATE)
    @JvmOverloads
    fun playBeeAndVibrate(milliseconds: Long, @RawRes mediaId: Int, listener: OnCompleteListener? = null) {
        //震动
        vibrate(milliseconds)
        //提示音
        playBee(mediaId, listener)
    }
}
