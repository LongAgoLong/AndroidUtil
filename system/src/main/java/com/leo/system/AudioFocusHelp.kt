/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.leo.system

import android.content.Context
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener

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

    fun requestAudioFocus(): Int {
        mAudioManager ?: return AudioManager.AUDIOFOCUS_REQUEST_FAILED
        if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager!!.requestAudioFocus(this,
                        AUDIO_STREAM_TYPE, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)) {
            audioFocus = AudioManager.AUDIOFOCUS_GAIN
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            return AudioManager.AUDIOFOCUS_REQUEST_FAILED
        }
    }

    fun abandonAudioFocus(): Int {
        if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager!!.abandonAudioFocus(this)) {
            audioFocus = AudioManager.AUDIOFOCUS_LOSS
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            return AudioManager.AUDIOFOCUS_REQUEST_FAILED
        }
    }

    fun setAudioStreamType(type: Int) {
        AUDIO_STREAM_TYPE = type
    }

    fun registerAudioFocusListener(mTtsAudioFocusListener: OnAudioFocusChangeListener) {
        mRegisterAudioFocusListener = mTtsAudioFocusListener
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (mRegisterAudioFocusListener != null) {
            mRegisterAudioFocusListener!!.onAudioFocusChange(focusChange)
        }
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
