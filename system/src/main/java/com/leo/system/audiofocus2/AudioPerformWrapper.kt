package com.leo.system.audiofocus2

import android.media.AudioManager
import com.leo.system.log.ZLog

abstract class AudioPerformWrapper : IAudioPerform {
    private val TAG: String = "AudioPerformWrapper"
    protected var mAudioFocusChange: Int = 0
    protected val mAudioFocusListeners: MutableList<AudioManager.OnAudioFocusChangeListener> = mutableListOf()

    override fun addAudioFocusChangeListener(listener: AudioManager.OnAudioFocusChangeListener?) {
        listener.let {
            mAudioFocusListeners.add(it!!)
        }
    }

    override fun removeAudioFocusChangeListener(listener: AudioManager.OnAudioFocusChangeListener?) {
        listener.let {
            mAudioFocusListeners.remove(it!!)
        }
    }

    override fun isGain(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            mAudioFocusChange == AudioManager.AUDIOFOCUS_GAIN
                    || mAudioFocusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
                    || mAudioFocusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE
                    || mAudioFocusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
        } else {
            mAudioFocusChange == AudioManager.AUDIOFOCUS_GAIN
                    || mAudioFocusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
                    || mAudioFocusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
        }
    }

    val callback: AudioManager.OnAudioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
        mAudioFocusChange = focusChange
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> ZLog.i(TAG, "focusChange：AUDIOFOCUS_GAIN")
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT -> ZLog.i(TAG, "focusChange：AUDIOFOCUS_GAIN_TRANSIENT")
            AudioManager.AUDIOFOCUS_LOSS -> ZLog.i(TAG, "focusChange：AUDIOFOCUS_LOSS")
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> ZLog.i(TAG, "focusChange：AUDIOFOCUS_LOSS_TRANSIENT")
            else -> {
            }
        }
        mAudioFocusListeners.forEach {
            it.onAudioFocusChange(focusChange)
        }
    }
}