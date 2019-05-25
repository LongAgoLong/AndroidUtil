/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.leo.system;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;

/**
 * 音频焦点辅助类
 */
public class AudioFocusHelp implements OnAudioFocusChangeListener {
    public static final String TAG = AudioFocusHelp.class.getSimpleName();
    public static int AUDIO_STREAM_TYPE = AudioManager.STREAM_MUSIC;
    private static AudioFocusHelp mInstance;
    private AudioManager mAudioManager;
    private OnAudioFocusChangeListener mRegisterAudioFocusListener;
    private int mAudioFocus;

    private AudioFocusHelp() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) ContextHelp.getContext()
                    .getSystemService(Context.AUDIO_SERVICE);
        }
    }

    public static AudioFocusHelp getInstance() {
        if (mInstance == null) {
            synchronized (AudioFocusHelp.class) {
                if (mInstance == null) {
                    mInstance = new AudioFocusHelp();
                }
            }
        }
        return mInstance;
    }

    public int requestAudioFocus() {
        if (mAudioManager == null) {
            return AudioManager.AUDIOFOCUS_REQUEST_FAILED;
        }
        if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager.requestAudioFocus(this,
                AUDIO_STREAM_TYPE, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)) {
            mAudioFocus = AudioManager.AUDIOFOCUS_GAIN;
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        } else {
            return AudioManager.AUDIOFOCUS_REQUEST_FAILED;
        }
    }

    public int abandonAudioFocus() {
        if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager.abandonAudioFocus(this)) {
            mAudioFocus = AudioManager.AUDIOFOCUS_LOSS;
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        } else {
            return AudioManager.AUDIOFOCUS_REQUEST_FAILED;
        }
    }

    public void setAudioStreamType(int type) {
        this.AUDIO_STREAM_TYPE = type;
    }

    public int getAudioFocus() {
        return mAudioFocus;
    }

    public void registerAudioFocusListener(OnAudioFocusChangeListener mTtsAudioFocusListener) {
        mRegisterAudioFocusListener = mTtsAudioFocusListener;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (mRegisterAudioFocusListener != null) {
            mRegisterAudioFocusListener.onAudioFocusChange(focusChange);
        }
        mAudioFocus = focusChange;
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                break;
            default:
                break;
        }
    }
}
