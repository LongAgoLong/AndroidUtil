package com.leo.system.audiofocus

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import com.leo.system.context.ContextHelp

/**
 * 兼容androidO以上的延时焦点请求封装，只支持以下streamType
 * 如需接受延迟焦点获取，使用此类；
 * 否则AudioFocusHelp已经可满足需求
 *
 * AudioManager.STREAM_VOICE_CALL
 * AudioManager.STREAM_RING
 * AudioManager.STREAM_MUSIC
 * AudioManager.STREAM_ALARM
 * AudioManager.STREAM_NOTIFICATION
 * AudioManager.STREAM_DTMF
 * AudioManager.STREAM_ACCESSIBILITY
 */
class AudioOFocusTask
@JvmOverloads
constructor(private val listener: AudioManager.OnAudioFocusChangeListener,
            private val streamType: Int = AudioManager.STREAM_MUSIC,
            private val durationHint: Int = AudioManager.AUDIOFOCUS_GAIN_TRANSIENT,
            private val acceptDelayFocus: Boolean = false,
            private val pauseWhenDucked: Boolean = false) {
    private var mFocusRequest: AudioFocusRequest? = null

    /**
     * @return
     * AudioManager.AUDIOFOCUS_REQUEST_FAILED -> 获取焦点失败：mPlaybackDelayed = false
     * AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> 获取焦点成功：mPlaybackDelayed = false
     * AudioManager.AUDIOFOCUS_REQUEST_DELAYED -> 获取延迟焦点成功：mPlaybackDelayed = true
     *
     * {@link #https://developer.android.google.cn/reference/android/media/AudioFocusRequest}
     */
    fun request(): Int {
        val mAudioManager = ContextHelp.context
                .getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val contentType = AudioOAttr.setInternalLegacyStreamType(streamType)
            val usage = AudioOAttr.usageForStreamType(streamType)
            val mPlaybackAttributes = AudioAttributes.Builder()
                    .setUsage(usage)
                    .setContentType(contentType)
                    .build()
            mFocusRequest = AudioFocusRequest.Builder(durationHint)
                    .setAudioAttributes(mPlaybackAttributes)
                    .setAcceptsDelayedFocusGain(acceptDelayFocus)
                    .setWillPauseWhenDucked(pauseWhenDucked)
                    .setOnAudioFocusChangeListener(listener)
                    .build()
            mAudioManager.requestAudioFocus(mFocusRequest!!)
        } else {
            mAudioManager.requestAudioFocus(listener, streamType, durationHint)
        }
    }

    fun abandon(): Int {
        val mAudioManager = ContextHelp.context
                .getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mFocusRequest ?: return AudioManager.AUDIOFOCUS_REQUEST_FAILED
            mAudioManager.abandonAudioFocusRequest(mFocusRequest!!)
        } else {
            mAudioManager.abandonAudioFocus(listener)
        }
    }
}