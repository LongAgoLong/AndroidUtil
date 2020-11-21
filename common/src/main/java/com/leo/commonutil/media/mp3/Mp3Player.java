package com.leo.commonutil.media.mp3;

import android.media.MediaPlayer;
import android.os.Looper;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

import com.leo.commonutil.asyn.WeakHandler;
import com.leo.commonutil.media.msg.AudioMsgPlayer;
import com.leo.commonutil.media.util.MediaUtils;
import com.leo.commonutil.media.util.TimeMode;
import com.leo.system.audiofocus.AudioFocusHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LEO
 * on 2018/12/24
 */
public class Mp3Player {
    private static Mp3Player player;
    private static final WeakHandler mHandler = new WeakHandler(Looper.getMainLooper());
    private final Mp3PlayerParams params = new Mp3PlayerParams();
    private final List<IMP3PlayListener> imp3PlayListeners = new ArrayList<>();

    private MediaPlayer mediaPlayer;

    private Mp3Player() {
        initMp3Player();
    }

    public static Mp3Player getInstance() {
        if (null == player) {
            synchronized (Mp3Player.class) {
                if (null == player) {
                    player = new Mp3Player();
                }
            }
        }
        return player;
    }

    private void initMp3Player() {
        mediaPlayer = null;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> reset());
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            reset();
            return true;
        });
        mediaPlayer.setOnPreparedListener(mp -> toggle(Mp3PlayState.PLAY));
        mediaPlayer.setOnBufferingUpdateListener((mp, percent) -> params.setBufferPercent(percent));
    }

    /**
     * 设置播放完成回调
     *
     * @param listener
     */
    public void setOnCompletionListener(@NonNull MediaPlayer.OnCompletionListener listener) {
        if (null != mediaPlayer) {
            mediaPlayer.setOnCompletionListener(listener);
        }
    }

    @Mp3PlayState
    public int getPlayState() {
        return params.getPlayState();
    }

    /**
     * 重置
     * 资源回收
     */
    public synchronized void destroy() {
        reset();
        if (null != mediaPlayer) {
            mediaPlayer = null;
        }
        if (null != player) {
            player = null;
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void addPlayListener(IMP3PlayListener iMP3PlayListener) {
        if (null == iMP3PlayListener) {
            return;
        }
        imp3PlayListeners.add(iMP3PlayListener);
    }

    public void removePlayListener(IMP3PlayListener iMP3PlayListener) {
        if (null == iMP3PlayListener) {
            return;
        }
        imp3PlayListeners.remove(iMP3PlayListener);
    }

    public void removeAllPlayListener() {
        if (null != imp3PlayListeners
                && !imp3PlayListeners.isEmpty()) {
            for (IMP3PlayListener mp3PlayListener : imp3PlayListeners) {
                mp3PlayListener.onStop();
            }
            imp3PlayListeners.clear();
        }
    }

    /**
     * 设置播放url并开始播放
     *
     * @param url
     */
    public void start(@NonNull String url) {
        try {
            MediaPlayer mediaMsgPlayer = AudioMsgPlayer.getInstance().getMediaPlayer();
            if (null != mediaMsgPlayer) {
                AudioMsgPlayer.getInstance().reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            toggle(Mp3PlayState.STOP);
            initMp3Player();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            if (null != mediaPlayer) {
                reset();
            }
        }
    }

    public void toggle(@Mp3PlayState int state) {
        switch (state) {
            case Mp3PlayState.PLAY: {
                if (this.mediaPlayer != null) {
                    AudioFocusHelper.Companion.getInstance().requestAudioFocus();
                    this.mediaPlayer.seekTo(params.getPosition());
                    this.mediaPlayer.start();
                    params.setDuration(this.mediaPlayer.getDuration());
                    if (null != imp3PlayListeners && !imp3PlayListeners.isEmpty()) {
                        for (IMP3PlayListener mp3PlayListener : imp3PlayListeners) {
                            mp3PlayListener.onPlay(params.getDuration() / 1000, params.getDurationFormat());
                        }
                    }
                    params.setPlayState(Mp3PlayState.PLAY);
                    update();
                }
                break;
            }
            case Mp3PlayState.PAUSE: {
                if (mediaPlayer != null) {
                    AudioFocusHelper.Companion.getInstance().abandonAudioFocus();
                    mediaPlayer.pause();
                    params.setPosition(mediaPlayer.getCurrentPosition());
                    for (IMP3PlayListener mp3PlayListener : imp3PlayListeners) {
                        mp3PlayListener.onPause();
                    }
                }
                params.setPlayState(Mp3PlayState.PAUSE);
                mHandler.removeCallbacksAndMessages(null);
                break;
            }
            case Mp3PlayState.STOP: {
                boolean isPlaying = false;
                try {
                    if (null != mediaPlayer) {
                        isPlaying = mediaPlayer.isPlaying();
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                if (mediaPlayer != null) {
                    if (isPlaying) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                    }
                }
                params.setPosition(0)
                        .setPlayState(Mp3PlayState.STOP);
                mHandler.removeCallbacksAndMessages(null);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 重置——停止播放并移除所有监听
     */
    public void reset() {
        toggle(Mp3PlayState.STOP);
        removeAllPlayListener();
    }

    /**
     * 拖动进度设置
     *
     * @param progress
     */
    public void setCurrentPosition(@FloatRange(from = 0.0, to = 1.0) float progress) {
        params.setPosition((int) (progress * mediaPlayer.getDuration()));
    }

    /**
     * 定时更新
     */
    private void update() {
        mHandler.removeCallbacks(this::update);
        if (mediaPlayer.getCurrentPosition() < mediaPlayer.getDuration()) {
            int currentPosition = Integer.parseInt(MediaUtils
                    .timeFormat(mediaPlayer.getCurrentPosition(), TimeMode.MODE_SECOND));
            int mediaRemainSecond = (params.getDuration() / 1000 - currentPosition) * 1000;//剩余时间（毫秒）
            String mediaRemainFormat = MediaUtils.timeFormat(mediaRemainSecond, TimeMode.MODE_FORMAT);
            for (IMP3PlayListener mp3PlayListener : imp3PlayListeners) {
                mp3PlayListener.onInfoUpdate(params.getDuration() / 1000,
                        params.getDurationFormat(),
                        currentPosition,
                        mediaRemainFormat,
                        params.getBufferPercent());
            }
            mHandler.postDelayed(this::update, 1000);
        }
    }
}
