package com.leo.system.audiofocus

import android.media.AudioAttributes
import android.media.AudioManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

object AudioOAttr {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun usageForStreamType(streamType: Int): Int {
        return when (streamType) {
            AudioManager.STREAM_VOICE_CALL -> AudioAttributes.USAGE_VOICE_COMMUNICATION
            AudioManager.STREAM_RING -> AudioAttributes.USAGE_NOTIFICATION_RINGTONE
            AudioManager.STREAM_MUSIC -> AudioAttributes.USAGE_MEDIA
            AudioManager.STREAM_ALARM -> AudioAttributes.USAGE_ALARM
            AudioManager.STREAM_NOTIFICATION -> AudioAttributes.USAGE_NOTIFICATION
            AudioManager.STREAM_DTMF -> AudioAttributes.USAGE_VOICE_COMMUNICATION_SIGNALLING
            AudioManager.STREAM_ACCESSIBILITY -> AudioAttributes.USAGE_ASSISTANCE_ACCESSIBILITY
            else -> AudioAttributes.USAGE_UNKNOWN
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setInternalLegacyStreamType(streamType: Int): Int {
        return when (streamType) {
            AudioManager.STREAM_VOICE_CALL -> AudioAttributes.CONTENT_TYPE_SPEECH
            AudioManager.STREAM_SYSTEM -> AudioAttributes.CONTENT_TYPE_SONIFICATION
            AudioManager.STREAM_RING -> AudioAttributes.CONTENT_TYPE_SONIFICATION
            AudioManager.STREAM_MUSIC -> AudioAttributes.CONTENT_TYPE_MUSIC
            AudioManager.STREAM_ALARM -> AudioAttributes.CONTENT_TYPE_SONIFICATION
            AudioManager.STREAM_NOTIFICATION -> AudioAttributes.CONTENT_TYPE_SONIFICATION
            AudioManager.STREAM_DTMF -> AudioAttributes.CONTENT_TYPE_SONIFICATION
            AudioManager.STREAM_ACCESSIBILITY -> AudioAttributes.CONTENT_TYPE_SPEECH
            else -> Log.e("AudioAttr", "Invalid stream type $streamType for AudioAttributes")
        }
    }
}