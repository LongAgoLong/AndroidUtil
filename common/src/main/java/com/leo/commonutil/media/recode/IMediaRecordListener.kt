package com.leo.commonutil.media.recode

interface IMediaRecordListener {
    /**
     * 录音开始
     */
    fun onStart()

    /**
     * 录音中...
     *
     * @param db   当前声音分贝
     * @param time 录音时长
     */
    fun onProgress(db: Double, time: Long)

    /**
     * 停止录音
     *
     * @param filePath 保存路径
     */
    fun onStop(filePath: String?, time: Long)

    /**
     * 取消录音
     */
    fun onCancel()
}