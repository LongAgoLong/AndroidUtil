package com.leo.system.audiofocus

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import com.leo.system.ContextHelp

class AudioOFocusTask
@JvmOverloads
constructor(val listener: AudioManager.OnAudioFocusChangeListener,
            val streamType: Int = AudioManager.STREAM_MUSIC,
            val durationHint: Int = AudioManager.AUDIOFOCUS_GAIN_TRANSIENT,
            val acceptDelaydFocus: Boolean = false,
            val pauseWhenDucked: Boolean = false) {
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
                    .setAcceptsDelayedFocusGain(acceptDelaydFocus)
                    .setWillPauseWhenDucked(pauseWhenDucked)
                    .setOnAudioFocusChangeListener(listener)
                    .build()
            mAudioManager.requestAudioFocus(mFocusRequest!!)
        } else {
            mAudioManager.requestAudioFocus(listener, streamType, durationHint)
        }
    }

    fun abandon(): Int {
        mFocusRequest ?: return AudioManager.AUDIOFOCUS_REQUEST_FAILED
        val mAudioManager = ContextHelp.context
                .getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mAudioManager.abandonAudioFocusRequest(mFocusRequest!!)
        } else {
            mAudioManager.abandonAudioFocus(listener)
        }
    }
}