package com.leo.system.audiofocus2

import android.content.Context
import android.media.AudioManager
import com.leo.system.context.ContextHelper

class AudioFocusTask : AudioPerformWrapper() {
    override fun request(
            streamType: Int,
            focusGainType: Int,
            acceptDelayFocus: Boolean,
            pauseWhenDucked: Boolean
    ): Int {
        return if (isGain()) {
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            val mAudioManager = ContextHelper.context
                    .getSystemService(Context.AUDIO_SERVICE) as AudioManager
            mAudioManager.requestAudioFocus(callback, streamType, focusGainType)
        }
    }

    override fun abandon(): Int {
        return if (!isGain()) {
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            val mAudioManager = ContextHelper.context
                    .getSystemService(Context.AUDIO_SERVICE) as AudioManager
            mAudioManager.abandonAudioFocus(callback)
        }
    }
}