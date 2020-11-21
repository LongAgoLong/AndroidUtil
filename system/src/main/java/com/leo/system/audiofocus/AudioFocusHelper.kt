package com.leo.system.audiofocus

import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import com.leo.system.audiofocus2.AudioFocusFactory


/**
 * 音频焦点辅助类
 */
class AudioFocusHelper private constructor() {
    /**
     * @param listener：申请成功后对AudioFocus的监听；
     * @param streamType：音频类型
     * STREAM_ALARM：手机闹铃，STREAM_MUSIC：手机音乐
     * STREAM_RING：电话铃声，STREAM_SYSTEAM：手机系统
     * STREAM_DTMF：音调，STREAM_NOTIFICATION：系统提示
     * STREAM_VOICE_CALL：语音电话
     * @param durationHint：
     * AudioManager.AUDIOFOCUS_GAIN:
     * 长时间获得AudioFocus
     * AudioManager.AUDIOFOCUS_LOSS:
     * 长时间失去了Audio Focus，需要停止Audio的播放，并且释放Media资源。为了避免再次自动获得AudioFocus而继续播放，不然突然冒出来的声音会让用户感觉莫名其妙，直接放弃AudioFocus，如果需要再次播放，用户要在界面上点击开始播放，才重新初始化Media，进行播放。
     * AudioManager.AUDIOFOCUS_LOSS_TRANSIENT：
     * 暂时失去AudioFocus，并会很快再次获得。必须停止Audio的播放，但是因为是暂时失去AudioFocus，可以不释放Media资源；
     * AUDIOFOCUS_GAIN_TRANSIENT:
     * 暂时获取焦点 适用于短暂的音频
     * AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
     * 应用跟其他应用共用焦点但播放的时候其他音频会降低音量
     */
    @JvmOverloads
    fun requestAudioFocus(
            streamType: Int = AUDIO_STREAM_TYPE,
            durationHint: Int = AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
    ): Int {
        return AudioFocusFactory.getInstance().request(streamType = streamType, focusGainType = durationHint)
    }

    fun abandonAudioFocus(): Int {
        return AudioFocusFactory.getInstance().abandon()
    }

    fun registerAudioFocusListener(mAudioFocusListener: OnAudioFocusChangeListener) {
        AudioFocusFactory.getInstance().addAudioFocusChangeListener(mAudioFocusListener)
    }

    fun unregisterAudioFocusListener(mAudioFocusListener: OnAudioFocusChangeListener) {
        AudioFocusFactory.getInstance().removeAudioFocusChangeListener(mAudioFocusListener)
    }

    companion object {
        var AUDIO_STREAM_TYPE = AudioManager.STREAM_MUSIC
        private var mInstance: AudioFocusHelper? = null

        fun getInstance(): AudioFocusHelper {
            return mInstance ?: synchronized(this) {
                mInstance ?: AudioFocusHelper().also { mInstance = it }
            }
        }
    }
}
