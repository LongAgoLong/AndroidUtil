package com.leo.commonutil.media.msg

import android.media.MediaPlayer
import com.leo.commonutil.media.IPlayer
import com.leo.commonutil.media.mp3.Mp3PlayState
import com.leo.commonutil.media.mp3.Mp3Player
import com.leo.system.audiofocus.AudioFocusHelper

/**
 * Create by LEO
 * on 2018/9/11
 * at 10:11
 *
 *
 * 语音消息播放器
 */
class AudioMsgPlayer private constructor() : IPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var iAudioMsgPlayListener: IAudioMsgPlayListener? = null
    override fun initMediaPlayer() {
        mediaPlayer = null
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setOnCompletionListener { mp: MediaPlayer? -> reset() }
        mediaPlayer!!.setOnErrorListener { mp: MediaPlayer?, what: Int, extra: Int ->
            reset()
            true
        }
        mediaPlayer!!.setOnPreparedListener { mp: MediaPlayer? -> toggle(Mp3PlayState.PLAY) }
    }

    fun setPlayListener(iAudioMsgPlayListener: IAudioMsgPlayListener?) {
        if (null == iAudioMsgPlayListener) {
            return
        }
        this.iAudioMsgPlayListener = iAudioMsgPlayListener
    }

    /**
     * 开始播放
     *
     * @param url
     */
    override fun start(url: String) {
        Mp3Player.getInstance().toggle(Mp3PlayState.STOP)
        try {
            toggle(Mp3PlayState.STOP)
            initMediaPlayer()
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(url)
            mediaPlayer!!.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
            if (null != mediaPlayer) {
                reset()
            }
        }
    }

    override fun toggle(state: Mp3PlayState) {
        when (state) {
            Mp3PlayState.PLAY -> {
                if (mediaPlayer != null) {
                    AudioFocusHelper.getInstance().requestAudioFocus()
                    mediaPlayer!!.start()
                }
            }
            Mp3PlayState.NONE, Mp3PlayState.PAUSE, Mp3PlayState.STOP -> {
                var isPlaying = false
                try {
                    if (null != mediaPlayer) {
                        isPlaying = mediaPlayer!!.isPlaying
                    }
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                }
                if (mediaPlayer != null) {
                    if (isPlaying) {
                        mediaPlayer!!.stop()
                        mediaPlayer!!.release()
                    }
                }
                if (null != iAudioMsgPlayListener) {
                    iAudioMsgPlayListener!!.onStop()
                }
            }
            else -> {
            }
        }
    }

    /**
     * 停止
     */
    override fun reset() {
        AudioFocusHelper.getInstance().abandonAudioFocus()
        toggle(Mp3PlayState.STOP)
        setPlayListener(null)
    }

    @Synchronized
    override fun destroy() {
        reset()
        if (null != mediaPlayer) {
            mediaPlayer = null
        }
        mInstance = null
    }

    companion object {
        private var mInstance: AudioMsgPlayer? = null
        fun getInstance(): AudioMsgPlayer {
            return mInstance ?: synchronized(this) {
                mInstance ?: AudioMsgPlayer().also { mInstance = it }
            }
        }
    }

    init {
        initMediaPlayer()
    }
}