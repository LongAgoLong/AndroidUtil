package com.leo.commonutil.media.mp3

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Looper
import androidx.annotation.FloatRange
import com.leo.commonutil.asyn.WeakHandler
import com.leo.commonutil.media.IPlayer
import com.leo.commonutil.media.msg.AudioMsgPlayer
import com.leo.commonutil.media.util.MediaUtils
import com.leo.commonutil.media.util.TimeMode
import com.leo.system.audiofocus.AudioFocusHelper
import java.util.*

/**
 * Created by LEO
 * on 2018/12/24
 */
class Mp3Player private constructor() : IPlayer {
    private val params = Mp3PlayerParams()
    private val imp3PlayListeners: MutableList<IMP3PlayListener> = ArrayList()
    private var mediaPlayer: MediaPlayer? = null
    override fun initMediaPlayer() {
        mediaPlayer = null
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setOnCompletionListener { mp: MediaPlayer? -> reset() }
        mediaPlayer!!.setOnErrorListener { mp: MediaPlayer?, what: Int, extra: Int ->
            reset()
            true
        }
        mediaPlayer!!.setOnPreparedListener { mp: MediaPlayer? -> toggle(Mp3PlayState.PLAY) }
        mediaPlayer!!.setOnBufferingUpdateListener { mp: MediaPlayer?, percent: Int -> params.bufferPercent = percent }
    }

    /**
     * 设置播放完成回调
     *
     * @param listener
     */
    fun setOnCompletionListener(listener: OnCompletionListener) {
        if (null != mediaPlayer) {
            mediaPlayer!!.setOnCompletionListener(listener)
        }
    }

    @get:Mp3PlayState
    val playState: Int
        get() = params.playState

    fun addPlayListener(iMP3PlayListener: IMP3PlayListener?) {
        if (null == iMP3PlayListener) {
            return
        }
        imp3PlayListeners.add(iMP3PlayListener)
    }

    fun removePlayListener(iMP3PlayListener: IMP3PlayListener?) {
        if (null == iMP3PlayListener) {
            return
        }
        imp3PlayListeners.remove(iMP3PlayListener)
    }

    fun removeAllPlayListener() {
        imp3PlayListeners.clear()
    }

    /**
     * 设置播放url并开始播放
     *
     * @param url
     */
    override fun start(url: String) {
        AudioMsgPlayer.getInstance().reset()
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

    override fun toggle(@Mp3PlayState state: Int) {
        when (state) {
            Mp3PlayState.PLAY -> {
                mediaPlayer?.run {
                    AudioFocusHelper.getInstance().requestAudioFocus()
                    seekTo(params.position)
                    start()
                    params.duration = duration
                    if (imp3PlayListeners.isNotEmpty()) {
                        for (mp3PlayListener in imp3PlayListeners) {
                            mp3PlayListener.onPlay(
                                    params.duration / 1000,
                                    params.durationFormat)
                        }
                    }
                    params.playState = Mp3PlayState.PLAY
                    update()
                }
            }
            Mp3PlayState.PAUSE -> {
                mediaPlayer?.run {
                    AudioFocusHelper.getInstance().abandonAudioFocus()
                    pause()
                    params.position = currentPosition
                    for (mp3PlayListener in imp3PlayListeners) {
                        mp3PlayListener.onPause()
                    }
                }
                params.playState = Mp3PlayState.PAUSE
                mHandler.removeCallbacksAndMessages(null)
            }
            Mp3PlayState.STOP -> {
                mediaPlayer?.run {
                    if (isPlaying) {
                        stop()
                        release()
                    }
                }
                params.position = 0
                params.playState = Mp3PlayState.STOP
                mHandler.removeCallbacksAndMessages(null)
            }
            else -> {
            }
        }
    }

    /**
     * 重置——停止播放并移除所有监听
     */
    override fun reset() {
        toggle(Mp3PlayState.STOP)
        if (imp3PlayListeners.isNotEmpty()) {
            for (mp3PlayListener in imp3PlayListeners) {
                mp3PlayListener.onStop()
            }
        }
        removeAllPlayListener()
    }

    /**
     * 重置
     * 资源回收
     */
    @Synchronized
    override fun destroy() {
        reset()
        if (null != mediaPlayer) {
            mediaPlayer = null
        }
        mInstance = null
    }

    /**
     * 拖动进度设置
     *
     * @param progress
     */
    fun setCurrentPosition(@FloatRange(from = 0.0, to = 1.0) progress: Float) {
        params.position = (progress * mediaPlayer!!.duration).toInt()
    }

    /**
     * 定时更新
     */
    private fun update() {
        mHandler.removeCallbacks { update() }
        if (mediaPlayer!!.currentPosition < mediaPlayer!!.duration) {
            val currentPosition = MediaUtils
                    .timeFormat(mediaPlayer!!.currentPosition, TimeMode.MODE_SECOND).toInt()
            val mediaRemainSecond = (params.duration / 1000 - currentPosition) * 1000 //剩余时间（毫秒）
            val mediaRemainFormat = MediaUtils.timeFormat(mediaRemainSecond, TimeMode.MODE_FORMAT)
            for (mp3PlayListener in imp3PlayListeners) {
                mp3PlayListener.onInfoUpdate(params.duration / 1000,
                        params.durationFormat,
                        currentPosition,
                        mediaRemainFormat,
                        params.bufferPercent)
            }
            mHandler.postDelayed({ update() }, 1000)
        }
    }

    companion object {
        private var mInstance: Mp3Player? = null
        private val mHandler = WeakHandler(Looper.getMainLooper())

        fun getInstance(): Mp3Player {
            return mInstance ?: synchronized(this) {
                mInstance ?: Mp3Player().also { mInstance = it }
            }
        }
    }

    init {
        initMediaPlayer()
    }
}