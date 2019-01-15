package com.leo.commonutil.media;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;

import com.leo.commonutil.media.util.MediaUtils;
import com.leo.commonutil.media.util.TimeMode;


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
    private int mediaDuration;
    private String mediaDurationFormat;

    private AudioMsgPlayer() {
    }

    public interface OnAudioMsgPlayListener {
        void onStop();
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

    public synchronized AudioMsgPlayer initPlayer() {
        if (null == mediaPlayer) {
            mediaPlayer = new MediaPlayer();
            initPlayerListener();
        }
        if (null == params) {
            params = new Params();
        }
        return player;
    }

    private void initPlayerListener() {
        mediaPlayer.setOnCompletionListener(mp -> {
            stop();
        });
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            stop();
            return true;
        });
        mediaPlayer.setOnPreparedListener(mp -> play());
    }

    public OnAudioMsgPlayListener getOnMP3PlayListenerList() {
        return onAudioMsgPlayListener;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    private static class Params {
        public int position = 0;

        public long pauseTimeMill = 0L;//记录暂停播放时的系统时间
    }

    public void setOnAudioMsgPlayListener(OnAudioMsgPlayListener onAudioMsgPlayListener) {
        if (null == onAudioMsgPlayListener) {
            return;
        }
        this.onAudioMsgPlayListener = onAudioMsgPlayListener;
    }

    /*设置播放路径*/
    public void start(@NonNull String url) {
        try {
            MediaPlayer mediaPlayer = Mp3Player.getInstance().getMediaPlayer();
            if (null != mediaPlayer) {
                Mp3Player.getInstance().stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            stopPlay();
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer();
            initPlayerListener();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            if (null != mediaPlayer) {
                stop();
            }
        }
    }

    /*播放*/
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(params.position);
            mediaPlayer.start();
            mediaDuration = Integer.parseInt(MediaUtils.timeFormat(mediaPlayer.getDuration(), TimeMode.MODE_SECOND));
            mediaDurationFormat = MediaUtils.timeFormat(mediaPlayer.getDuration(), TimeMode.MODE_FORMAT);
            mediaDuration = Integer.parseInt(MediaUtils.timeFormat(this.mediaPlayer.getDuration(), TimeMode.MODE_SECOND));
            mediaDurationFormat = MediaUtils.timeFormat(this.mediaPlayer.getDuration(), TimeMode.MODE_FORMAT);
        }
    }

    public void stopPlay() {
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

    /*停止*/
    public void stop() {
        stopPlay();
        setOnAudioMsgPlayListener(null);
    }

    public synchronized void destroy() {
        stop();
        if (null != mediaPlayer) {
            mediaPlayer = null;
        }
        if (null != player) {
            player = null;
        }
    }
}
