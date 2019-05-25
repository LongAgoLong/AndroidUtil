package com.leo.commonutil.media.msg;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;

import com.leo.commonutil.media.mp3.Mp3Player;
import com.leo.system.AudioFocusHelp;


/**
 * Create by LEO
 * on 2018/9/11
 * at 10:11
 * in MoeLove Company
 * 语音消息播放器
 */
public class AudioMsgPlayer {
    private static AudioMsgPlayer player;
    private MediaPlayer mediaPlayer;

    private Params params;

    private OnAudioMsgPlayListener onAudioMsgPlayListener;

    private AudioMsgPlayer() {
        initPlayer();
    }

    public static AudioMsgPlayer getInstance() {
        if (null == player) {
            synchronized (AudioMsgPlayer.class) {
                if (null == player) {
                    player = new AudioMsgPlayer();
                }
            }
        }
        return player;
    }

    private void initPlayer() {
        if (null == mediaPlayer) {
            mediaPlayer = new MediaPlayer();
            initPlayerListener();
        }
        if (null == params) {
            params = new Params();
        }
    }

    private void initPlayerListener() {
        mediaPlayer.setOnCompletionListener(mp -> {
            reset();
        });
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            reset();
            return true;
        });
        mediaPlayer.setOnPreparedListener(mp -> play());
    }

    public OnAudioMsgPlayListener getPlayListener() {
        return onAudioMsgPlayListener;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    private static class Params {
        public int position = 0;

        public long pauseTimeMill = 0L;//记录暂停播放时的系统时间
    }

    public void setPlayListener(OnAudioMsgPlayListener onAudioMsgPlayListener) {
        if (null == onAudioMsgPlayListener) {
            return;
        }
        this.onAudioMsgPlayListener = onAudioMsgPlayListener;
    }

    /**
     * 开始播放
     *
     * @param url
     */
    public void startPlay(@NonNull String url) {
        try {
            MediaPlayer mediaPlayer = Mp3Player.getInstance().getMediaPlayer();
            if (null != mediaPlayer) {
                Mp3Player.getInstance().stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            stop();
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer();
            initPlayerListener();
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

    /*播放*/
    public void play() {
        if (mediaPlayer != null) {
            AudioFocusHelp.getInstance().requestAudioFocus();
            mediaPlayer.seekTo(params.position);
            mediaPlayer.start();
        }
    }

    public void stop() {
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
            params.position = 0;
        }
        if (null != onAudioMsgPlayListener) {
            onAudioMsgPlayListener.onStop();
        }
    }

    /**
     * 停止
     */
    public void reset() {
        AudioFocusHelp.getInstance().abandonAudioFocus();
        stop();
        setPlayListener(null);
    }

    public synchronized void destroy() {
        reset();
        if (null != mediaPlayer) {
            mediaPlayer = null;
        }
        if (null != player) {
            player = null;
        }
    }
}
