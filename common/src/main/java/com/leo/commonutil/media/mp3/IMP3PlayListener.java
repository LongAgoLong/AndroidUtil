package com.leo.commonutil.media.mp3;

public interface IMP3PlayListener {
    /**
     * @param musicDuration       音频总时长(单位：秒)
     * @param musicDurationFormat 音频总时长格式化
     */
    void onPlay(int musicDuration, String musicDurationFormat);

    void onPause();

    /**
     * @param musicDuration        音频总时长(单位：秒)
     * @param musicDurationFormat  音频总时长格式化
     * @param musicCurrentPosition 音频当前播放时长(单位：秒)
     * @param mediaRemainFormat    音频剩余时长格式化
     * @param bufferPercent        缓存进度百分比(0~100)
     */
    void onInfoUpdate(int musicDuration, String musicDurationFormat, int musicCurrentPosition, String mediaRemainFormat, int bufferPercent);

    void onStop();
}
