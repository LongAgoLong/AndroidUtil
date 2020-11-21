package com.leo.system.audiofocus2

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.leo.system.context.ContextHelper

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
@RequiresApi(Build.VERSION_CODES.O)
class AudioFocusTaskO : AudioPerformWrapper() {
    private var mFocusRequest: AudioFocusRequest? = null

    /**
     * @return
     * AudioManager.AUDIOFOCUS_REQUEST_FAILED -> 获取焦点失败：mPlaybackDelayed = false
     * AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> 获取焦点成功：mPlaybackDelayed = false
     * AudioManager.AUDIOFOCUS_REQUEST_DELAYED -> 获取延迟焦点成功：mPlaybackDelayed = true
     *
     * {@link #https://developer.android.google.cn/reference/android/media/AudioFocusRequest}
     */
    override fun request(
            streamType: Int,
            focusGainType: Int,
            acceptDelayFocus: Boolean,
            pauseWhenDucked: Boolean
    ): Int {
        val mAudioManager = ContextHelper.context
                .getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (isGain()) {
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }
        val contentType = AudioOAttr.setInternalLegacyStreamType(streamType)
        val usage = AudioOAttr.usageForStreamType(streamType)
        val mPlaybackAttributes = AudioAttributes.Builder()
                .setUsage(usage)
                .setContentType(contentType)
                .build()
        mFocusRequest = AudioFocusRequest.Builder(focusGainType)
                .setAudioAttributes(mPlaybackAttributes)
                .setAcceptsDelayedFocusGain(acceptDelayFocus)
                .setWillPauseWhenDucked(pauseWhenDucked)
                .setOnAudioFocusChangeListener(callback)
                .build()
        return mAudioManager.requestAudioFocus(mFocusRequest!!)
    }

    override fun abandon(): Int {
        val mAudioManager = ContextHelper.context
                .getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mFocusRequest ?: return AudioManager.AUDIOFOCUS_REQUEST_FAILED
        return if (!isGain()) {
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            mAudioManager.abandonAudioFocusRequest(mFocusRequest!!)
        }
    }
}