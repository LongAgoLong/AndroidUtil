package com.leo.system.audiofocus2

import android.media.AudioManager
import android.os.Build

class AudioFocusFactory private constructor() {
    private var mAudioPerformWrapper: AudioPerformWrapper =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AudioFocusTaskO()
            } else {
                AudioFocusTask()
            }

    @JvmOverloads
    fun request(
            streamType: Int = AudioManager.STREAM_MUSIC,
            focusGainType: Int = AudioManager.AUDIOFOCUS_GAIN,
            acceptDelayFocus: Boolean = false,
            pauseWhenDucked: Boolean = false
    ): Int {
        return mAudioPerformWrapper.request(streamType, focusGainType, acceptDelayFocus, pauseWhenDucked)
    }

    fun abandon(): Int {
        return mAudioPerformWrapper.abandon()
    }

    fun addAudioFocusChangeListener(listener: AudioManager.OnAudioFocusChangeListener?) {
        mAudioPerformWrapper.addAudioFocusChangeListener(listener)
    }

    fun removeAudioFocusChangeListener(listener: AudioManager.OnAudioFocusChangeListener?) {
        mAudioPerformWrapper.removeAudioFocusChangeListener(listener)
    }

    fun isGain(): Boolean {
        return mAudioPerformWrapper.isGain()
    }

    companion object {
        private var mInstance: AudioFocusFactory? = null

        fun getInstance(): AudioFocusFactory {
            return mInstance ?: synchronized(this) {
                mInstance ?: AudioFocusFactory().also { mInstance = it }
            }
        }
    }
}