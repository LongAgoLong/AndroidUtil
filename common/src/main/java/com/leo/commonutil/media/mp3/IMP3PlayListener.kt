package com.leo.commonutil.media.mp3

interface IMP3PlayListener {
    /**
     * @param musicDuration       音频总时长(单位：秒)
     * @param musicDurationFormat 音频总时长格式化
     */
    fun onPlay(musicDuration: Int, musicDurationFormat: String?)
    fun onPause()

    /**
     * @param musicDuration        音频总时长(单位：秒)
     * @param musicDurationFormat  音频总时长格式化
     * @param musicCurrentPosition 音频当前播放时长(单位：秒)
     * @param mediaRemainFormat    音频剩余时长格式化
     * @param bufferPercent        缓存进度百分比(0~100)
     */
    fun onInfoUpdate(musicDuration: Int, musicDurationFormat: String?, musicCurrentPosition: Int, mediaRemainFormat: String?, bufferPercent: Int)
    fun onStop()
}