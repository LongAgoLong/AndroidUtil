package com.leo.system.audiofocus

import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import com.leo.system.log.LogUtil
import com.leo.system.context.ContextHelp


/**
 * 音频焦点辅助类
 */
class AudioFocusHelp private constructor() : OnAudioFocusChangeListener {
    private var mAudioManager: AudioManager? = null
    private var mRegisterAudioFocusListener: OnAudioFocusChangeListener? = null
    private var audioFocus: Int = 0

    init {
        if (mAudioManager == null) {
            mAudioManager = ContextHelp.context
                    .getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }
    }

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
    fun requestAudioFocus(listener: OnAudioFocusChangeListener = this,
                          streamType: Int = AUDIO_STREAM_TYPE,
                          durationHint: Int = AudioManager.AUDIOFOCUS_GAIN_TRANSIENT): Int {
        mAudioManager ?: return AudioManager.AUDIOFOCUS_REQUEST_FAILED
        if (audioFocus == AudioManager.AUDIOFOCUS_GAIN) {
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }
        return if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager!!.requestAudioFocus(listener,
                        streamType, durationHint)) {
            audioFocus = AudioManager.AUDIOFOCUS_GAIN
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            AudioManager.AUDIOFOCUS_REQUEST_FAILED
        }
    }

    fun abandonAudioFocus(): Int {
        return if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager!!.abandonAudioFocus(this)) {
            audioFocus = AudioManager.AUDIOFOCUS_LOSS
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            AudioManager.AUDIOFOCUS_REQUEST_FAILED
        }
    }

    fun registerAudioFocusListener(mTtsAudioFocusListener: OnAudioFocusChangeListener) {
        mRegisterAudioFocusListener = mTtsAudioFocusListener
    }

    fun unregisterAudioFocusListener() {
        mRegisterAudioFocusListener = null
    }

    override fun onAudioFocusChange(focusChange: Int) {
        mRegisterAudioFocusListener?.onAudioFocusChange(focusChange)
        audioFocus = focusChange
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> LogUtil.i(TAG, "focusChange：AUDIOFOCUS_GAIN")
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT -> LogUtil.i(TAG, "focusChange：AUDIOFOCUS_GAIN_TRANSIENT")
            AudioManager.AUDIOFOCUS_LOSS -> LogUtil.i(TAG, "focusChange：AUDIOFOCUS_LOSS")
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> LogUtil.i(TAG, "focusChange：AUDIOFOCUS_LOSS_TRANSIENT")
            else -> {
            }
        }
    }

    companion object {
        private val TAG: String = AudioFocusHelp::class.java.simpleName
        var AUDIO_STREAM_TYPE = AudioManager.STREAM_MUSIC
        private var mInstance: AudioFocusHelp? = null

        fun getInstance(): AudioFocusHelp {
            return mInstance ?: synchronized(this) {
                mInstance ?: AudioFocusHelp().also { mInstance = it }
            }
        }
    }
}
