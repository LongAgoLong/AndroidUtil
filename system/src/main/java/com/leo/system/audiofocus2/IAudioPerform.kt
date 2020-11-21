package com.leo.system.audiofocus2

import android.media.AudioManager

interface IAudioPerform {

    fun request(
            streamType: Int,
            focusGainType: Int,
            acceptDelayFocus: Boolean,
            pauseWhenDucked: Boolean): Int

    fun abandon(): Int

    fun addAudioFocusChangeListener(listener: AudioManager.OnAudioFocusChangeListener?)

    fun removeAudioFocusChangeListener(listener: AudioManager.OnAudioFocusChangeListener?)

    fun isGain(): Boolean
}