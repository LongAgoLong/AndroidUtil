package com.leo.commonutil.media;

import android.app.Notification;
import android.app.NotificationManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.leo.commonutil.media.util.MediaUtils;
import com.leo.commonutil.media.util.TimeMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LEO
 * on 2018/12/24
 */
public class Mp3Player {
    public static final int PLAY = 1;
    public static final int PAUSE = 2;
    public static final int STOP = 3;

    private static Handler handler = new Handler();
    private int playState;

    private static Mp3Player player;
    private MediaPlayer mediaPlayer;

    private Params params;

    private List<OnMP3PlayListener> onMP3PlayListenerList;
    private int mediaDuration;
    private int bufferPercent;
    private String mediaDurationFormat;

    private final int notification_id = 19172501;
    private Notification notification;
    private NotificationManager notificationManager;

    private Mp3Player() {
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

    public synchronized Mp3Player initMp3Player() {
        if (null == mediaPlayer) {
            mediaPlayer = new MediaPlayer();
            initPlayerListener();
        }
        if (null == params) {
            params = new Params();
        }
        if (null == onMP3PlayListenerList) {
            onMP3PlayListenerList = new ArrayList<>();
        }
        return player;
    }

    private void initPlayerListener() {
        mediaPlayer.setOnCompletionListener(mp -> stop());
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            stop();
            return true;
        });
        mediaPlayer.setOnPreparedListener(mp -> play());
        mediaPlayer.setOnBufferingUpdateListener((mp, percent) -> bufferPercent = percent);
    }

    public List<OnMP3PlayListener> getOnMP3PlayListenerList() {
        return onMP3PlayListenerList;
    }

    public int getPlayState() {
        return playState;
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

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    private static class Params {
        public int position = 0;

        public long pauseTimeMill = 0L;//记录暂停播放时的系统时间
    }

    public interface OnMP3PlayListener {
        /**
         * @param musicDuration       音频总时长(单位：秒)
         * @param musicDurationFormat 音频总时长格式化
         */
        void onMP3Play(int musicDuration, String musicDurationFormat);

        void onMP3Pause();

        /**
         * @param musicDuration        音频总时长(单位：秒)
         * @param musicDurationFormat  音频总时长格式化
         * @param musicCurrentPosition 音频当前播放时长(单位：秒)
         * @param mediaRemainFormat    音频剩余时长格式化
         * @param bufferPercent        缓存进度百分比(0~100)
         */
        void onMP3Update(int musicDuration, String musicDurationFormat, int musicCurrentPosition, String mediaRemainFormat, int bufferPercent);

        void onMP3Stop();
    }

    public void addOnMP3PlayListener(OnMP3PlayListener onMP3PlayListener) {
        if (null == onMP3PlayListener) {
            return;
        }
        if (null != onMP3PlayListenerList) {
            onMP3PlayListenerList.add(onMP3PlayListener);
        }
    }

    public void removeOnMP3PlayListener(OnMP3PlayListener onMP3PlayListener) {
        if (null == onMP3PlayListener) {
            return;
        }
        if (null != onMP3PlayListenerList) {
            onMP3PlayListenerList.remove(onMP3PlayListener);
        }
    }

    public void removeAllOnMP3PlayListener() {
        if (null != onMP3PlayListenerList
                && !onMP3PlayListenerList.isEmpty()) {
            for (OnMP3PlayListener mp3PlayListener : onMP3PlayListenerList) {
                mp3PlayListener.onMP3Stop();
            }
            onMP3PlayListenerList.clear();
        }
    }

    /*设置播放路径*/
    public void initPlayer(@NonNull String url) {
        try {
            MediaPlayer mediaMsgPlayer = AudioMsgPlayer.getInstance().getMediaPlayer();
            if (null != mediaMsgPlayer) {
                AudioMsgPlayer.getInstance().stop();
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
        if (this.mediaPlayer != null) {
            this.mediaPlayer.seekTo(params.position);
            this.mediaPlayer.start();
            mediaDuration = Integer.parseInt(MediaUtils.timeFormat(this.mediaPlayer.getDuration(), TimeMode.MODE_SECOND));
            mediaDurationFormat = MediaUtils.timeFormat(this.mediaPlayer.getDuration(), TimeMode.MODE_FORMAT);
            if (null != onMP3PlayListenerList && !onMP3PlayListenerList.isEmpty()) {
                for (OnMP3PlayListener mp3PlayListener : onMP3PlayListenerList) {
                    mp3PlayListener.onMP3Play(mediaDuration, mediaDurationFormat);
                }
            }
            playState = PLAY;
            update();
        }
    }

    public void stopPlay() {
        boolean isPlaying = false;
        try {
            if (null != mediaPlayer)
                isPlaying = mediaPlayer.isPlaying();
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
        playState = STOP;
        handler.removeCallbacksAndMessages(null);
        if (null != notificationManager) {
            notificationManager.cancel(notification_id);
        }
    }

    /*停止*/
    public void stop() {
        stopPlay();
        removeAllOnMP3PlayListener();
    }

    /*暂停*/
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            params.position = mediaPlayer.getCurrentPosition();
            params.pauseTimeMill = System.currentTimeMillis();
            if (null != onMP3PlayListenerList && !onMP3PlayListenerList.isEmpty()) {
                for (OnMP3PlayListener mp3PlayListener : onMP3PlayListenerList) {
                    mp3PlayListener.onMP3Pause();
                }
            }
        }
        playState = PAUSE;
        handler.removeCallbacksAndMessages(null);
    }

    /*拖动进度设置*/
    public void setCurrentPosition(float progress) {
        if (null != params && progress >= 0) {
            params.position = (int) (progress * mediaPlayer.getDuration());
        }
    }

    /*定时更新*/
    private void update() {
        if (mediaPlayer.getCurrentPosition() < mediaPlayer.getDuration()) {
            int second = Integer.parseInt(MediaUtils.timeFormat(mediaPlayer.getCurrentPosition(), TimeMode.MODE_SECOND));
            int mediaRemainSecond = (mediaDuration - second) * 1000;//剩余时间（毫秒）
            String mediaRemainFormat = MediaUtils.timeFormat(mediaRemainSecond, TimeMode.MODE_FORMAT);
            if (null != onMP3PlayListenerList
                    && !onMP3PlayListenerList.isEmpty()) {
                for (OnMP3PlayListener mp3PlayListener : onMP3PlayListenerList) {
                    mp3PlayListener.onMP3Update(mediaDuration, mediaDurationFormat, second, mediaRemainFormat, bufferPercent);
                }
            }
            handler.postDelayed(this::update, 1000);
        }
    }
}
