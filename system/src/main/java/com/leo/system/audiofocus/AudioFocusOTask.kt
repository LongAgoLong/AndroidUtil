package com.leo.system.audiofocus

import android.annotation.TargetApi
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import com.leo.system.ContextHelp

@TargetApi(Build.VERSION_CODES.O)
class AudioFocusOTask constructor(val listener: AudioManager.OnAudioFocusChangeListener,
                                  val usage: Int = AudioAttributes.USAGE_MEDIA,
                                  val contentType: Int = AudioAttributes.CONTENT_TYPE_MUSIC,
                                  val durationHint: Int = AudioManager.AUDIOFOCUS_GAIN_TRANSIENT,
                                  val acceptDelaydFocus: Boolean = true,
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
        mAudioManager ?: return AudioManager.AUDIOFOCUS_REQUEST_FAILED
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
        return mAudioManager.requestAudioFocus(mFocusRequest!!)
    }

    fun abandon(): Int {
        mFocusRequest ?: return AudioManager.AUDIOFOCUS_REQUEST_FAILED
        val mAudioManager = ContextHelp.context
                .getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mAudioManager ?: return AudioManager.AUDIOFOCUS_REQUEST_FAILED
        return mAudioManager.abandonAudioFocusRequest(mFocusRequest!!)
    }
}