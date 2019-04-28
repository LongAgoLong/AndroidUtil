package com.leo.commonutil.media.mp3;

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
